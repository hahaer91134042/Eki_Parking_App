package com.eki.parking.Controller.activity.frag.EditBank

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.child.BankCodeSelectFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.EditBankBody
import com.eki.parking.Model.request.body.GetBankBody
import com.eki.parking.Model.response.BankResponse
import com.eki.parking.Model.sql.MemberBank
import com.eki.parking.R
import com.eki.parking.View.viewControl.GreenArrowDownClick
import com.eki.parking.databinding.FragEditBankBinding
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.show
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/06/20
 */

class EditBankFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragEditBankBinding
    private lateinit var bankInfo: RequestBody.BankInfo
    private var bankAddress = RequestBody.Address()

    override fun initFragView() {

        toolBarTitle = getString(R.string.Bank_account)

        loadBankInfo()

        binding.determinBtn.setOnClickListener {
            EkiRequest<EditBankBody>().also {
                it.body = EditBankBody(bankInfo).apply {
                    address = bankAddress
                }
            }.sendRequest(context, true, object : OnResponseListener<BankResponse> {
                override fun onReTry() {

                }

                override fun onFail(errorMsg: String, code: String) {

                }

                override fun onTaskPostExecute(result: BankResponse) {
                    val memberBank = result.info.toSql()
                    memberBank.sqlSaveOrUpdate()
                    showToast(getString(R.string.Successfully_modified))
                    backFrag()
                }

            })
        }
    }

    override fun onResumeFragView() {
        loadBankInfo()
        super.onResumeFragView()
    }

    private fun loadBankInfo() {
        EkiRequest<GetBankBody>().also {
            it.body = GetBankBody()
        }.sendRequest(
            context,
            showProgress = true,
            listener = object : OnResponseListener<BankResponse> {
                override fun onReTry() {

                }

                override fun onFail(errorMsg: String, code: String) {

                }

                override fun onTaskPostExecute(result: BankResponse) {
                    val memberBank = result.info.toSql()
                    setUpBankInfo(memberBank)
                    memberBank.sqlSaveOrUpdate()
                }
            })

    }

    private fun setUpBankInfo(bank: MemberBank) {
        bankInfo = bank.toData()
        bankAddress = bank.Address.toData()

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

        binding.bankCode.tailControl = object : GreenArrowDownClick() {
            override fun viewAfterClick(clickView: ImageView) {
                BankCodeSelectFrag().also { frag ->
                    frag.onBankSelect = { bank ->
                        binding.bankCode.input = bank.code
                    }
                }.show(childFragmentManager)
            }
        }

        binding.accountName.whenInputChange {
            bankInfo.name = it
            checkBankInfo()
        }
        binding.bankCode.whenInputChange {
//            Log.i("input code->$it")//設定input也會觸發這個function
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
            //setAddress(it)
            bankAddress.detail = it.cleanTex
            checkBankInfo()
        }

        if (bankInfo.isPerson) {
            binding.bankTypeSelect.check(R.id.personBtn)
        } else {
            binding.bankTypeSelect.check(R.id.companyBtn)
        }

        binding.accountName.input = bankInfo.name
        binding.serialText.input = bankInfo.serial
        binding.bankCode.input = bankInfo.code
        binding.subCode.input = bankInfo.sub
        binding.bankAccount.input = bankInfo.account

        binding.accountAddress.input = bankAddress.detail
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

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragEditBankBinding.inflate(inflater, container, false)
        return binding
    }
}