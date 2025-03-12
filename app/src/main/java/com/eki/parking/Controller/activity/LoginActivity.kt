package com.eki.parking.Controller.activity

import com.eki.parking.AppFlag
import com.eki.parking.R
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.Login.LoginFrag
import com.eki.parking.Controller.activity.frag.Login.RegisterFrag
import com.eki.parking.Controller.activity.frag.Login.SmsCheckFrag
import com.eki.parking.Controller.listener.OnMemberFragSwitch
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2019/6/18
 */
class LoginActivity:TitleBarActivity(),OnMemberFragSwitch {
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun toolBarRes(): Int =R.id.toolbar

//    override fun getTitleBarClass(): ActivityClassEnum = ActivityClassEnum.Login

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun initActivityView() {
        val loginFrag=LoginFrag().apply {
            memberSwitch=this@LoginActivity
        }
        replaceFragment(FragLevelSet(1).setFrag(loginFrag),FragSwitcher.NON);

    }

    override fun setUpResumeComponent() {
        //registerReceiver(AppFlag.ToRegister,AppFlag.ToLogin,AppFlag.ToSmsCheck)
    }

    override fun onSmsCheck() {
        replaceFragment(FragLevelSet(2).setFrag(SmsCheckFrag().apply { memberSwitch=this@LoginActivity }),FragSwitcher.RIGHT_IN)

//        replaceFragment(FragLevelSet(2).setFrag(RegisterFrag().apply {
//            memberSwitch=this@LoginActivity
//            countryCode="country"
//            this.phone=phone
//        }),FragSwitcher.RIGHT_IN)
    }

    override fun onRegister(country:String,phone:String) {
        replaceFragment(FragLevelSet(2).setFrag(RegisterFrag().apply {
            memberSwitch=this@LoginActivity
            countryCode=country
            this.phone=phone
        }),FragSwitcher.RIGHT_IN)
    }

    override fun onFinish() {
        sendBroadcast(AppFlag.OnMemberCheck)
        toMain()
    }


}