package com.eki.parking.View.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.eki.parking.Controller.activity.ForgetPwdActivity
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.LoginProcess
import com.eki.parking.EkiAccountCheck
import com.eki.parking.Model.request.body.LoginBody
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.Model.sql.MemberLoginInfo
import com.eki.parking.R
import com.eki.parking.View.abs.RelativeCustomView
import com.eki.parking.View.libs.StateButton
import com.eki.parking.extension.startActivityAnim
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.listener.OnSuccessListener
import com.hill.devlibs.tools.ValidCheck

/**
 * Created by Hill on 2019/12/04
 */
class PapayaLoginPanel(context: Context?, attrs: AttributeSet?) : RelativeCustomView(context, attrs) {

    var listener: OnSuccessListener<EkiMember>?=null

    private var validCheck=EkiAccountCheck()
    private val accountInputView: UnderLineEditTextView = findViewById(R.id.accountInputView)
    private val accountErrorView: TextView = findViewById(R.id.accountErrorView)
    private val pwdTextNum: TextView = findViewById(R.id.pwdTextNum)
    private val pwdInputView: UnderLineEditTextView = findViewById(R.id.pwdInputView)
    private val pwdErrorView: TextView = findViewById(R.id.pwdErrorView)
    private val determinBtn: StateButton = findViewById(R.id.determinBtn)
    private val forgetPwd: TextView = findViewById(R.id.forgetPwd)

    init {

        accountInputView.whenInputOnFocuse {
            accountInputView.showNormal()
            accountErrorView.text=""
        }
        accountInputView.whenInputNoFocuse {
            if (!accountInputView.input.isNullOrEmpty())
                showAccResult(validCheck.checkAccountValid(accountInputView.input))
        }

        pwdTextNum.text=getString(R.string.pwd_num_format).messageFormat(pwdInputView.input.length)

        pwdInputView.whenInputOnFocuse {
            pwdInputView.whenInputChange { str->
                showPwdResult(validCheck.checkPwdValid(str))
                pwdTextNum.text=getString(R.string.pwd_num_format).messageFormat(str.length)
            }
        }
        pwdInputView.whenInputNoFocuse {
            pwdInputView.whenInputChange(null)
            pwdInputView.showNormal()
            pwdErrorView.text=""
        }
        pwdInputView.tailControl=object:IViewControl<ImageView>(),
                                        IViewControl.IActionView<EditText>{
            var isClick=false
            override fun clickViewSet(clickView: ImageView) {
                clickView.setImageResource(R.drawable.icon_invisible)
            }

            override fun viewAfterClick(clickView: ImageView) {
                isClick=!isClick
                if (isClick){
                    clickView.setImageResource(R.drawable.icon_visible)
                }else{
                    clickView.setImageResource(R.drawable.icon_invisible)
                }
            }

            override fun onClickActionView(actionView: EditText) {
                if (isClick){
                    actionView.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
                }else{
                    actionView.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }

        determinBtn.setOnClickListener {
            checkLogin()
        }

        forgetPwd.setOnClickListener {
            startActivityAnim(ForgetPwdActivity::class.java)
        }
    }

    fun setAccount(acc:String){
        accountInputView.input=acc
    }

    private fun checkLogin() {
        var accResult=validCheck.checkAccountValid(accountInputView.input)
        showAccResult(accResult)

        var pwdResult=validCheck.checkPwdValid(pwdInputView.input)
        showPwdResult(pwdResult)


        if (accResult.valid&&pwdResult.valid)
            startLogin()
    }

    private fun showAccResult(accResult: ValidCheck.CheckResult) {
        if (!accResult.valid){
            accountInputView.showError()
            accountErrorView.text=getString(accResult.value.errorStr)
        }else{
            accountInputView.showNormal()
            accountErrorView.text=""
        }
    }

    private fun showPwdResult(pwdResult: ValidCheck.CheckResult) {
        if (!pwdResult.valid){
            pwdInputView.showError()
            pwdErrorView.text=getString(pwdResult.value.errorStr)
        }else{
            pwdInputView.showNormal()
            pwdErrorView.text=""
        }
    }

    private fun startLogin() {
        var account=accountInputView.input
        object :LoginProcess(context){
            override val body: LoginBody
                get() = LoginBody().apply {
                    acc=account
                    pwd=pwdInputView.input
                    lan=getString(R.string.lan)
                    pushToken=app.preferenceManager.settingPreference?.fcmToken?:""
                }
            override val onResponse: OnResponseListener<EkiMember>
                get() = object : OnResponseListener<EkiMember> {
                    override fun onReTry() {
                    }

                    override fun onFail(errorMsg: String,code:String) {
                        pwdInputView.input=""
                        accountInputView.input=""
                    }

                    override fun onTaskPostExecute(result: EkiMember) {
                        MemberLoginInfo.save(account)
                        listener?.onSuccess(result)
                    }
                }
        }.run()
    }

    override fun setInflatView(): Int = R.layout.item_papaya_login_panel

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}