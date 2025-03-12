package com.eki.parking.Controller.activity.frag.ForgetPwd

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.EkiAccountCheck
import com.eki.parking.R
import com.eki.parking.View.widget.UnderLineEditTextMsgView
import com.eki.parking.databinding.FragForgetPwdStep3Binding
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.tools.InputCheck
import com.hill.devlibs.tools.ValidCheck

/**
 * Created by Hill on 2020/10/07
 */
class ForgetPwdStep3Frag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragForgetPwdStep3Binding
    private var validCheck = EkiAccountCheck()
    private var inputCheck = UserInputCheck()
    private var inputPwd = ""
    var onEditPwd: ((String) -> Unit)? = null

    override fun initFragView() {

        setUpPwdInput()
        setUpPwdConfirm()

        binding.toNextBtn.setOnClickListener {
            onEditPwd?.invoke(inputPwd)
        }
    }

    private fun setUpPwdConfirm() {
        binding.pwdCheckView.inputView.tailControl = PwdTailControll()
        binding.pwdCheckView.inputView.whenInputChange { confirm ->
            pwdConfirm(inputPwd, confirm)
        }
        binding.pwdCheckView.inputView.whenInputNoFocuse {
            var confirm = binding.pwdCheckView.input
            pwdConfirm(inputPwd, confirm)
        }
    }

    private fun pwdConfirm(pwd: String, confirm: String) {
//        Log.w("PwdConfirm pwd->$pwd confirm->$confirm")
        if (pwd.isNotEmpty() && confirm.isNotEmpty()) {
            if (pwd != confirm) {
                inputCheck.confirmPwdOK = false
                binding.pwdCheckView.showError(getString(R.string.Confirmation_password_does_not_match))
            } else {
                inputCheck.confirmPwdOK = true
                binding.pwdCheckView.showNormal()
            }
        } else {
            inputCheck.confirmPwdOK = false
            inputCheck.pwdOK = false
        }
    }

    private fun setUpPwdInput() {
        binding.pwdInputView.inputView.tailControl = PwdTailControll()

        binding.pwdInputView.inputView.whenInputOnFocuse {
            binding.pwdInputView.showNormal()
            binding.pwdCheckView.clearStatus()
        }
        binding.pwdInputView.inputView.whenInputNoFocuse {
            var pwd = binding.pwdInputView.inputView.input
            inputPwd = pwd
            var pwdValidResult = validCheck.checkPwdValid(pwd)
            showValidMsg(binding.pwdInputView, pwdValidResult)
            inputCheck.pwdOK = pwdValidResult.valid
        }

        binding.pwdInputView.inputView.whenInputChange { pwd ->
            inputPwd = pwd
            var pwdValidResult = validCheck.checkPwdValid(pwd)
            showValidMsg(binding.pwdInputView, pwdValidResult)
            inputCheck.pwdOK = pwdValidResult.valid
        }
    }

    private fun showValidMsg(msgView: UnderLineEditTextMsgView?, result: ValidCheck.CheckResult) {
        msgView.notNull { view ->
            if (result.valid) {
                view.showNormal()
            } else {
                view.showError(getString(result.value.errorStr))
            }
        }
    }

    private class PwdTailControll : IViewControl<ImageView>(),
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

    private inner class UserInputCheck : InputCheck() {

        var pwdOK = false
            set(value) {
                field = value
                start()
            }
        var confirmPwdOK = false
            set(value) {
                field = value
                start()
            }

        init {
//            Log.i("UserInputCheck pwdOK->$pwdOK confirmOK->$confirmPwdOK")
            add(object : InputCheck.CallBack() {
                override fun check(): Boolean = pwdOK
            })
            add(object : InputCheck.CallBack() {
                override fun check(): Boolean = confirmPwdOK
            })
        }

        override fun whenAllTrue() {
            binding.toNextBtn.isEnabled = true
        }

        override fun whenCheckFalse() {
            binding.toNextBtn.isEnabled = false
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragForgetPwdStep3Binding.inflate(inflater, container, false)
        return binding
    }
}