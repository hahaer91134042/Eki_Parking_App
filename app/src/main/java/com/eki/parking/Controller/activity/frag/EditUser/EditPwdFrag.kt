package com.eki.parking.Controller.activity.frag.EditUser

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.EkiAccountCheck
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.EditPwdBody
import com.eki.parking.Model.response.MemberEditResponse
import com.eki.parking.R
import com.eki.parking.View.widget.UnderLineEditTextMsgView
import com.eki.parking.databinding.FragEditPwdBinding
import com.eki.parking.extension.sendRequest
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IViewControl

/**
 * Created by Hill on 2020/06/02
 */
class EditPwdFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragEditPwdBinding
    private var validCheck = EkiAccountCheck()
    private var body = RequestBody.EditPwd()

    override fun initFragView() {

        binding.oldPwdView.inputView.tailControl = SeePwdCtrl()
        binding.pwdInputView.inputView.tailControl = SeePwdCtrl()
        binding.pwdCheckView.inputView.tailControl = SeePwdCtrl()
        setUpPwdInput(binding.oldPwdView)
        setUpPwdInput(binding.pwdInputView)
        setUpPwdInput(binding.pwdCheckView)

        binding.toNextBtn.setOnClickListener {
            EkiRequest<EditPwdBody>().also {
                it.body = EditPwdBody(body)
            }.sendRequest(
                context,
                showProgress = true,
                listener = object : OnResponseListener<MemberEditResponse> {
                    override fun onReTry() {

                    }

                    override fun onFail(errorMsg: String, code: String) {

                    }

                    override fun onTaskPostExecute(result: MemberEditResponse) {

                        showToast(getString(R.string.Successfully_modified))
                        backFrag()
                    }
                })
        }
    }

    private fun setUpPwdInput(input: UnderLineEditTextMsgView) {
        var inputView = input.inputView
        inputView.whenInputOnFocuse {
            inputView.whenInputChange { str ->
                var pwdResult = validCheck.checkPwdValid(str)
                if (!pwdResult.valid) {
                    input.showError(getString(pwdResult.value.errorStr))
                } else {
                    input.showNormal()
                }
                checkPwdToSetNextBtn()
            }
        }
        inputView.whenInputNoFocuse {
            inputView.whenInputChange(null)
            input.showNormal()
        }
    }

    private fun checkPwdToSetNextBtn() {
        var oldPwd = binding.oldPwdView.input
        var newPwd = binding.pwdInputView.input
        var checkPwd = binding.pwdCheckView.input

        var oldValid = validCheck.checkPwdValid(oldPwd)
        var newValid = validCheck.checkPwdValid(newPwd)
        var checkValid = validCheck.checkPwdValid(checkPwd)
        when (oldValid.valid && newValid.valid && checkValid.valid) {
            true -> when ((newPwd == checkPwd) && (newPwd != oldPwd)) {
                true -> {
                    body.oldPwd = oldPwd
                    body.newPwd = newPwd
                    binding.toNextBtn.isEnabled = true
                }
                else -> binding.toNextBtn.isEnabled = false
            }
            else -> binding.toNextBtn.isEnabled = false
        }
    }

    private class SeePwdCtrl : IViewControl<ImageView>(),
        IViewControl.IActionView<EditText> {
        var isClick = false
        override fun clickViewSet(clickView: ImageView) {
            clickView.setImageResource(R.drawable.icon_invisible)
        }

        override fun viewAfterClick(clickView: ImageView) {
            isClick = !isClick
            if (isClick) {
                clickView.setImageResource(R.drawable.icon_visible)
            } else {
                clickView.setImageResource(R.drawable.icon_invisible)
            }
        }

        override fun onClickActionView(actionView: EditText) {
            if (isClick) {
                actionView.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            } else {
                actionView.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragEditPwdBinding.inflate(inflater, container, false)
        return binding
    }
}