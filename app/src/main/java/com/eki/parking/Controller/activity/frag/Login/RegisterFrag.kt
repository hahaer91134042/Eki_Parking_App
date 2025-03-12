package com.eki.parking.Controller.activity.frag.Login

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.MsgDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.ICameraFileSet
import com.eki.parking.Controller.listener.OnMemberFragSwitch
import com.eki.parking.Controller.listener.OnPhotoEventListener
import com.eki.parking.Controller.manager.SysCameraManager
import com.eki.parking.Controller.process.RegisterProcess
import com.eki.parking.EkiAccountCheck
import com.eki.parking.Model.request.body.RegisterBody
import com.eki.parking.R
import com.eki.parking.View.widget.UnderLineEditTextMsgView
import com.eki.parking.databinding.FragRegisterBinding
import com.eki.parking.extension.showMsgDialog
import com.hill.devlibs.extension.isNullOrEmpty
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.onFalse
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.listener.OnMsgDialogBtnListener
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.tools.InputCheck
import com.hill.devlibs.tools.Log
import com.hill.devlibs.tools.ValidCheck
import java.io.File

/**
 * Created by Hill on 2019/6/26
 */
class RegisterFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragRegisterBinding
    private var validCheck = EkiAccountCheck()

    var memberSwitch: OnMemberFragSwitch? = null

    var countryCode = ""
    var phone = ""
    private var userIcon: File? = null
    private var inputCheck = UserInputCheck()

    private var registerBody = RegisterBody().apply {
    }


    override fun initFragView() {
        toolBarTitle = getString(R.string.member_setting)
        registerBody.let {
            it.lan = getString(R.string.lan)
            it.pushToken = app.preferenceManager.settingPreference?.fcmToken ?: ""
            it.phone = phone
            it.info.countryCode = countryCode
            it.info.phone = phone
        }
        binding.registerProcessBar.toBaseDataImg()

        setUpUserIcon()

        binding.smsTexView.contryCode = countryCode
        binding.smsTexView.input = phone
        binding.smsTexView.unEditMode()

        setUpNickNameInput()
        setUpPwdInput()
        setUpPwdConfirm()
        setUpMailInput()

        binding.policyCheck.onCheckClick {
            inputCheck.policyOK = it
        }

        binding.toNextBtn.setOnClickListener {
            object : RegisterProcess(context, userIcon) {
                override val body: RegisterBody
                    get() = registerBody

                override fun onProcessOver() {
                    showMsgDialog(context!!, MsgDialog.MsgDialogSet().apply {
                        title = getString(R.string.Notice)
                        msg = getString(R.string.Congratulations_on_becoming_a_member)
                        pBtnTex = getString(R.string.Determine)
                    }, object : OnMsgDialogBtnListener {
                        override fun onPostiveBtn() {
                            memberSwitch?.onFinish()
                        }

                        override fun onNegativeBtn() {

                        }
                    })
                }

                override fun onProcessFail() {
                    cleanInputData()
                }
            }.run()
        }

        Log.w("$TAG country->$countryCode phone->$phone")
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

    private fun setUpMailInput() {
        binding.mailInputView.inputView.whenInputNoFocuse {
            var mail = binding.mailInputView.input
            registerBody.mail = mail
            var mailCheck = ValidCheck.mail(mail)
            inputCheck.mailOK = mailCheck.valid
            showValidMsg(binding.mailInputView, mailCheck)
        }
        binding.mailInputView.inputView.whenInputChange { mail ->
            registerBody.mail = mail
            var mailCheck = ValidCheck.mail(mail)
            inputCheck.mailOK = mailCheck.valid
            showValidMsg(binding.mailInputView, mailCheck)
        }
    }

    private fun setUpPwdConfirm() {
        binding.pwdCheckView.inputView.tailControl = PwdTailControll()
        binding.pwdCheckView.inputView.whenInputChange { confirm ->
            pwdConfirm(registerBody.pwd, confirm)
        }
        binding.pwdCheckView.inputView.whenInputNoFocuse {
            var confirm = binding.pwdCheckView.input
            pwdConfirm(registerBody.pwd, confirm)
        }
    }

    private fun pwdConfirm(pwd: String, confirm: String) {
        if (pwd.isNotEmpty() && confirm.isNotEmpty()) {
            if (pwd != confirm) {
                inputCheck.confirmPwdOK = false
                binding.pwdCheckView.showError(getString(R.string.Confirmation_password_does_not_match))
            } else {
                inputCheck.pwdOK = true
                inputCheck.confirmPwdOK = true
                binding.pwdCheckView.showNormal()
            }
        } else {
            inputCheck.pwdOK = false
            inputCheck.confirmPwdOK = false
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
            registerBody.pwd = pwd
            var pwdValidResult = validCheck.checkPwdValid(pwd)
            showValidMsg(binding.pwdInputView, pwdValidResult)
            inputCheck.pwdOK = pwdValidResult.valid
        }

        binding.pwdInputView.inputView.whenInputChange { pwd ->
            registerBody.pwd = pwd
            var pwdValidResult = validCheck.checkPwdValid(pwd)
            showValidMsg(binding.pwdInputView, pwdValidResult)
            inputCheck.pwdOK = pwdValidResult.valid
        }
    }

    private fun setUpNickNameInput() {
        binding.nickNameInputView.inputView.whenInputChange { nickName ->
            Log.d("nickName change->$nickName")
            registerBody.info.nickName = nickName
            nickName.isNullOrEmpty {
                inputCheck.nickNameOK = false
                binding.nickNameInputView.showError(getString(R.string.This_name_field_is_required))
            }.onFalse {
                binding.nickNameInputView.showNormal()
                inputCheck.nickNameOK = true
            }
        }
        binding.nickNameInputView.inputView.whenInputNoFocuse {
            registerBody.info.nickName = binding.nickNameInputView.input
            binding.nickNameInputView.input.isNullOrEmpty {
                inputCheck.nickNameOK = false
                binding.nickNameInputView.showError(getString(R.string.This_name_field_is_required))
            }.onFalse { inputCheck.nickNameOK = true }
        }
    }

    private val cameraResult = object : SysCameraManager.CameraResultListener {
        override fun onPicture(pic: File) {
            userIcon = pic
            binding.userIconCameraView.icon.setImageBitmap(pic.toBitMap())
        }

        override fun onPictureError() {

        }
    }

    private fun setUpUserIcon() {
        app.sysCamera.addCameraListenerFrom(cameraResult)

        binding.userIconCameraView.listener = object : OnPhotoEventListener {
            override fun onCameraEvent() {
                app.sysCamera.startCamera(object : ICameraFileSet() {
                    override val fileName: String
                        get() = "user_${DateTime.now().toStamp()}"
                    override val scaleX: Int
                        get() = binding.userIconCameraView.width
                    override val scaleY: Int
                        get() = binding.userIconCameraView.height

                })
            }

            override fun onPhotoEvent() {
                app.sysCamera.startPhoto("user_${DateTime().toStamp()}")
            }
        }

    }

    override fun onDestroyView() {
        app.sysCamera.removeCameraListener(cameraResult)
        super.onDestroyView()
    }


    private fun cleanInputData() {
//        nickNameInputView.input=""
        binding.pwdInputView.input = ""
        binding.pwdCheckView.input = ""
        binding.mailInputView.input = ""
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
        var nickNameOK = false
            set(value) {
                field = value
                start()
            }
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
        var mailOK = false
            set(value) {
                field = value
                start()
            }
        var policyOK = false
            set(value) {
                field = value
                start()
            }

        init {
            add(object : InputCheck.CallBack() {
                override fun check(): Boolean = nickNameOK
            })
            add(object : InputCheck.CallBack() {
                override fun check(): Boolean = pwdOK
            })
            add(object : InputCheck.CallBack() {
                override fun check(): Boolean = confirmPwdOK
            })
            add(object : InputCheck.CallBack() {
                override fun check(): Boolean = mailOK
            })
            add(object : InputCheck.CallBack() {
                override fun check(): Boolean = policyOK
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
        binding = FragRegisterBinding.inflate(inflater, container, false)
        return binding
    }

}