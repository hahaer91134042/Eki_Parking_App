package com.eki.parking.Controller.activity.frag.Login

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppConfig
import com.eki.parking.Controller.activity.frag.Login.child.CountrySelectFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnMemberFragSwitch
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.BaseProcess
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SmsCheckBody
import com.eki.parking.Model.response.SmsConfirmResponse
import com.eki.parking.Model.sql.CountryCode
import com.eki.parking.R
import com.eki.parking.View.widget.UnderLineSmsView
import com.eki.parking.databinding.FragSmsCheck2Binding
import com.eki.parking.extension.color
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.show
import com.eki.parking.extension.sqlDataList
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.tools.Log
import com.hill.devlibs.tools.MobileInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Hill on 2019/6/24
 */
class SmsCheckFrag : SearchFrag(), IFragViewBinding {

    var memberSwitch: OnMemberFragSwitch? = null
    private lateinit var binding: FragSmsCheck2Binding

    private lateinit var smsBody: SmsCheckBody
    private var checkCode = AppConfig.Value.smsCheck

    private var countryList = sqlDataList<CountryCode>()
    private var mobilCountry = MobileInfo.country

    //這邊是因為 dialog回來之後會讓view變成null kotlin extension的問題
    private val smsInput: UnderLineSmsView by lazy { binding.smsTexView }
    private var smsProcess = SendSmsProcess()

    override fun initFragView() {
        toolBarTitle = getString(R.string.Phone_verification)
        smsBody = SmsCheckBody().apply {
            lan = getString(R.string.lan)
            countryCode = when (countryList.any { it.short2 == mobilCountry }) {
                true -> countryList.first { it.short2 == mobilCountry }.code
                else -> "0"
            }
        }

        smsInput.contryCode = smsBody.countryCode
        smsInput.onSelectCountry = {
            CountrySelectFrag().also {
                it.setData(countryList)
                it.onSelectCode = { code ->
                    smsBody.countryCode = code
                    smsInput.contryCode = code

                }
            }.show(childFragmentManager)
        }
        smsInput.onSendClick = { phone ->
            if (phone.isNullOrEmpty()) {
                showToast(getString(R.string.Please_enter_phone_number))
            } else {
                smsInput.lockSendBtn()
                smsBody.phone = phone
                smsProcess.run()
            }

        }

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

        binding.toNextBtn.setOnClickListener {
//            Log.w("register phone->${smsBody.phone}")
            memberSwitch?.onRegister(smsBody.countryCode, smsBody.phone)
        }
    }

    override fun onDestroyView() {
        smsProcess.closeTimer()
        Log.i("$TAG onDestroyView")
        super.onDestroyView()
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
                            smsInput.showNormal()
                            binding.smsMsg.text = ""
                            smsInput.unLockSendBtn()
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
            EkiRequest<SmsCheckBody>().also {
                it.body = smsBody
            }.sendRequest(
                context,
                showProgress = false,
                listener = object : OnResponseListener<SmsConfirmResponse> {
                    override fun onReTry() {

                    }

                    override fun onTaskPostExecute(result: SmsConfirmResponse) {
                        checkCode = result?.checkCode
                        smsMsgTexColor = color(R.color.light_gray5)
                        errorMsg = ""
                    }

                    override fun onFail(errorMsg: String, code: String) {
                        smsMsgTexColor = Color.RED
                        this@SendSmsProcess.errorMsg = errorMsg
                        smsInput.showError()
                    }
                },
                showErrorDialog = false
            )//正式上線要開啟
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragSmsCheck2Binding.inflate(inflater, container, false)
        return binding
    }
}