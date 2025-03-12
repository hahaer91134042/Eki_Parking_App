package com.eki.parking.Controller.activity.frag.EditUser

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.Login.child.CountrySelectFrag
import com.eki.parking.Controller.asynctask.task.server.RequestTask
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.BaseProcess
import com.eki.parking.Controller.process.MemberEditPrecess
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.MemberEditBody
import com.eki.parking.Model.request.body.SmsCheckBody
import com.eki.parking.Model.response.SmsConfirmResponse
import com.eki.parking.Model.sql.CountryCode
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.databinding.FragResetPhoneBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.tools.Log
import com.hill.devlibs.tools.MobileInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Hill on 2020/06/01
 */
class ResetPhoneFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragResetPhoneBinding
    private lateinit var smsBody: SmsCheckBody
    private var mobilCountry = MobileInfo.country
    private var countryList = sqlDataList<CountryCode>()
    private var smsProcess = SendSmsProcess()
    private var checkCode = ""

    override fun initFragView() {

        smsBody = SmsCheckBody().apply {
            lan = getString(R.string.lan)
            countryCode = when (countryList.any { it.short2 == mobilCountry }) {
                true -> countryList.first { it.short2 == mobilCountry }.code
                else -> "0"
            }
        }

        setUpSmsView()
        setUpCheckCodeView()

        binding.toNextBtn.setOnClickListener {
            sqlData<EkiMember>().notNull { member ->
                var body = member.toData()
                PhoneEdit(body.apply {
                    phone = smsBody.phone
                    info.notNull { info ->
                        info.phone = smsBody.phone
                        info.countryCode = smsBody.countryCode
                    }
                }).also {
                    it.onSuccess = {
                        member.phoneNum = smsBody.phone
                        member?.info?.CountryCode = smsBody.countryCode
                        member?.info?.PhoneNum = smsBody.phone
                        member.sqlSaveOrUpdate()
                        showToast(getString(R.string.Successfully_modified))
                        backFrag()
                    }
                }.run()
            }
        }
    }

    private fun setUpCheckCodeView() {
        binding.checkCodeView.whenInputChange {
//            Log.i("check code change->$it")
            if (checkCode.isNotEmpty()) {
                if (it.isNotEmpty()) {
//                memberSwitch?.onRegister(smsBody.countryCode,ValidUtils.getCleanPhoneNum(phoneEditText.text.toString()))
                    if (it == checkCode) {
                        binding.toNextBtn.isEnabled = true
                        binding.checkCodeView.showNormal()
                        binding.checkErrorMsg.text = ""
                    } else {
                        binding.toNextBtn.isEnabled = false
                        binding.checkCodeView.showError()
                        binding.checkErrorMsg.text = getString(R.string.SMS_verification_code_error)
                    }
                } else {
                    binding.toNextBtn.isEnabled = false
                    binding.checkCodeView.showNormal()
                    binding.checkErrorMsg.text = ""
                }
            } else {
                binding.toNextBtn.isEnabled = false
                binding.checkCodeView.showError()
                binding.checkErrorMsg.text = getString(R.string.Please_start_with_SMS_authentication)
            }
        }
    }

    private fun setUpSmsView() {
        binding.smsTexView.contryCode = smsBody.countryCode
        binding.smsTexView.onSelectCountry = {
            CountrySelectFrag().also {
                it.setData(countryList)
                it.onSelectCode = { code ->
                    smsBody.countryCode = code
                    binding.smsTexView.contryCode = code

                }
            }.show(childFragmentManager)
        }

        binding.smsTexView.onSendClick = { phone ->
            Log.w("send click")
            if (phone.isNullOrEmpty()) {
                showToast(getString(R.string.Please_enter_phone_number))
            } else {
                binding.smsTexView.lockSendBtn()
                smsBody.phone = phone
                smsProcess.run()
            }
        }

    }

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.Reset_phone)
    }

    override fun onDestroyView() {
        smsProcess.closeTimer()
        super.onDestroyView()
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragResetPhoneBinding.inflate(inflater, container, false)
        return binding
    }

    private inner class PhoneEdit(var b: MemberEditBody) : MemberEditPrecess(context) {
        override val body: MemberEditBody
            get() = b
    }

    private inner class SendSmsProcess : BaseProcess(),
        CoroutineScope by MainScope() {

        private var countSec = 30
        private var timer = Timer()
        private var errorMsg = ""
        private var smsMsgTexColor = color(R.color.light_gray5)

        override fun run() {
            timer.schedule(object : TimerTask() {
                private var sec = countSec
                override fun run() {
                    if (sec > 0) {
                        launch(Dispatchers.Main) {
                            binding.smsMsg.setTextColor(smsMsgTexColor)
                            binding.smsMsg.text =
                                "$errorMsg ${getString(R.string.Intervals).messageFormat(sec)}"
                        }
                    } else {
                        launch {
                            binding.smsTexView.showNormal()
                            binding.smsMsg.text = ""
                            binding.smsTexView.unLockSendBtn()
//                            checkCode=""
                        }
                        cancel()
                        timer.purge()
                    }
                    sec--
                }
            }, 0, 1000)

            sendSmsToPhone()
        }

        fun closeTimer() {
            timer.cancel()
            timer.purge()
        }

        private fun sendSmsToPhone() {


            RequestTask<SmsConfirmResponse>(context!!, EkiRequest<SmsCheckBody>().also {
                it.body = smsBody
            }, isDialog = false, showErrorDialog = false).setExecuteListener(object :
                OnResponseListener<SmsConfirmResponse> {
                override fun onReTry() {

                }

                override fun onTaskPostExecute(result: SmsConfirmResponse) {
                    checkCode = result?.checkCode
                    smsMsgTexColor = color(R.color.light_gray5)
                    errorMsg = ""
//                    Log.i("get check code->$checkCode")
                }

                override fun onFail(errorMsg: String, code: String) {
//                    smsMsg.setTextColor(Color.RED)
                    smsMsgTexColor = Color.RED
                    this@SendSmsProcess.errorMsg = errorMsg

                    binding.smsTexView.showError()
                }
            }).start()//正式上線要開啟

        }
    }
}