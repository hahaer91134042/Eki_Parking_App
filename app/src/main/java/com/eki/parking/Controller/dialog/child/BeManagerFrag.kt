package com.eki.parking.Controller.dialog.child

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialEvent
import com.eki.parking.Controller.process.BeManagerProcess
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.request.body.BeManagerBody
import com.eki.parking.Model.sql.CountryCode
import com.eki.parking.R
import com.eki.parking.View.viewControl.OrangeArrowDownClick
import com.eki.parking.databinding.FragBeManagerBankBinding
import com.eki.parking.extension.show
import com.eki.parking.extension.showProgress
import com.eki.parking.extension.sqlDataList
import com.eki.parking.extension.string
import com.hill.devlibs.extension.showToast
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.tools.MobileInfo

/**
 * Created by Hill on 2020/04/20
 */
class BeManagerFrag : DialogChildFrag<BeManagerFrag>(), ISerialDialog, IFragViewBinding {

    private lateinit var binding:FragBeManagerBankBinding
    private lateinit var serialEvent: ISerialEvent

    private var bankInfo = RequestBody.BankInfo().also { it.isPerson = true }

    private var skip = false

    private val bankAddress = RequestBody.Address()

    override fun initFragView() {
        binding.bankTypeSelect.setOnCheckedChangeListener { _, checkedId ->
            binding.serialFieldName.text = when (checkedId) {
                R.id.companyBtn -> getString(R.string.Company_uniform_number)
                else -> getString(R.string.ID_number)
            }
            bankInfo.isPerson = when (checkedId) {
                R.id.companyBtn -> false
                else -> true
            }
            checkBankInfo()
        }

        binding.accountName.whenInputChange {
            bankInfo.name = it
            checkBankInfo()
        }
        binding.serialText.whenInputChange {
            bankInfo.serial = it
            checkBankInfo()
        }
        binding.bankCode.tailControl = object : OrangeArrowDownClick() {
            override fun viewAfterClick(clickView: ImageView) {
                BankCodeSelectFrag().also { frag ->
                    frag.onBankSelect = { bank ->
                        binding.bankCode.input = bank.code
                    }
                }.show(childFragmentManager)
            }
        }
        binding.bankCode.whenInputChange {
            //Log.i("input code->$it")//設定input也會觸發這個function
            bankInfo.code = it
            checkBankInfo()
        }

        binding.subCode.whenInputChange {
            bankInfo.sub = it
            checkBankInfo()
        }

        binding.bankAccount.whenInputChange {
            bankInfo.account = it
            checkBankInfo()
        }

        binding.accountAddress.whenInputChange {
            bankAddress.detail = it
            //setAddress(it)
            checkBankInfo()
        }

        binding.determinBtn.setOnClickListener {
            BeManager().apply {
                val process = from?.showProgress()
                onSuccess = {
                    process?.dismiss()
                    //這邊會去call加入地點
                    EkiMsgDialog().also {
                        it.btnSet = EkiMsgDialog.BtnSet.Skip
                        it.msg = string(R.string.Parking_space_information_will_be_set)
                        it.determinClick = {
                            //Log.d("determin click")
                            skip = false
                            serialEvent.onNext()
                        }
                        it.cancelClick = {
                            //Log.w("cancel click")
                            skip = true
                            serialEvent.onNext()
                        }
                    }.show(childFragmentManager)
                }
                onFail = {
                    process?.dismiss()
                    from?.showToast("成為地主失敗!")
                }
            }.run()


        }
    }

    private fun checkBankInfo() {
        binding.determinBtn.isEnabled = when {
            bankInfo.name.isNotEmpty() &&
                    bankInfo.serial.isNotEmpty() &&
                    bankInfo.code.isNotEmpty() &&
                    bankInfo.sub.isNotEmpty() &&
                    bankInfo.account.isNotEmpty() &&
                    bankAddress.detail.isNotEmpty() -> true
            else -> false
        }

    }

    private fun country() {
        val countryList = sqlDataList<CountryCode>()

        val name = when (MobileInfo.language) {
            "zh" -> countryList[216].fullCn
            else -> countryList[216].fullEn
        }

        bankAddress.state = name
        bankAddress.country = name

    }

    private inner class BeManager : BeManagerProcess(context) {
        override val body: BeManagerBody
            get() = BeManagerBody(bankInfo).apply {
                address = bankAddress
            }
    }

    override val frag: DialogChildFrag<*>
        get() = this
    override val title: String
        get() = string(R.string.Bank_account)

    override fun setEvent(event: ISerialEvent) {
        serialEvent = event
    }

    override fun next(): ISerialDialog = when (skip) {
        true -> TeachingStep1Frag()
        else -> AddLocStep2Frag()
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragBeManagerBankBinding.inflate(inflater,container,false)
        return binding
    }
}