package com.eki.parking.Controller.activity

import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.ForgetPwd.ForgetPwdStep1Frag
import com.eki.parking.Controller.activity.frag.ForgetPwd.ForgetPwdStep2Frag
import com.eki.parking.Controller.activity.frag.ForgetPwd.ForgetPwdStep3Frag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.ForgetPwdBody
import com.eki.parking.Model.request.body.SmsCheckBody
import com.eki.parking.Model.response.MemberEditResponse
import com.eki.parking.Model.response.SmsConfirmResponse
import com.eki.parking.R
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.string
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/10/07
 */
class ForgetPwdActivity: TitleBarActivity() {

    private var phoneNum=""

    override fun initActivityView() {
        replaceFragment(FragLevelSet(1)
                .setFrag(ForgetPwdStep1Frag().also { it.onNext=onStep1Finish }),
                FragSwitcher.NON)

    }

    private val onStep1Finish:(String)->Unit={phone->

        phoneNum=phone

        EkiRequest<SmsCheckBody>().apply {
            body=SmsCheckBody().also {
                it.lan= string(R.string.lan)
//                it.countryCode="886"
                it.phone=phone

                it.isForget=true
            }
        }.sendRequest(this,true,object : OnResponseListener<SmsConfirmResponse> {
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {

            }

            override fun onTaskPostExecute(result: SmsConfirmResponse) {

                replaceFragment(FragLevelSet(1)
                        .setFrag(ForgetPwdStep2Frag().apply {
                            setData(result.checkCode)
                            onNext=onStep2Finish
                        }),FragSwitcher.RIGHT_IN)
            }
        },showErrorDialog = false)
    }
    private val onStep2Finish={
        replaceFragment(FragLevelSet(1)
                .setFrag(ForgetPwdStep3Frag().apply {
                    onEditPwd=onStep3Finish
                }),FragSwitcher.RIGHT_IN)
    }

    private val onStep3Finish:(String)->Unit={pwd->
//        Log.d("Edit forget pwd->$pwd")
        EkiRequest<ForgetPwdBody>().apply {
            body= ForgetPwdBody(RequestBody.ForgetPwd().also {
                it.phone=phoneNum
                it.pwd=pwd
            })
        }.sendRequest(this, showProgress = true, listener = object:OnResponseListener<MemberEditResponse> {
            override fun onReTry() {
            }

            override fun onFail(errorMsg: String, code: String) {
                showToast("密碼修改失敗")
                toMain()
            }

            override fun onTaskPostExecute(result: MemberEditResponse) {
                showToast("密碼已更新")
                toMain()
            }
        })
    }



    override fun setUpResumeComponent() {

    }

    override fun setUpActivityView(): Int =R.layout.activity_title_bar

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }
}