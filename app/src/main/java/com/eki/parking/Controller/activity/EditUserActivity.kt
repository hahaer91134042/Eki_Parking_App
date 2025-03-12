package com.eki.parking.Controller.activity

import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.EditUser.EditPwdFrag
import com.eki.parking.Controller.activity.frag.EditUser.EditUserFrag
import com.eki.parking.Controller.activity.frag.EditUser.ResetPhoneFrag
import com.eki.parking.R
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/05/29
 */
class EditUserActivity:TitleBarActivity(){


    override fun initActivityView() {
        replaceFragment(FragLevelSet(1)
                .setFrag(EditUserFrag().also {
                    it.onEditPhone=onEditPhone
                    it.onEditPwd=onEditPwd
                }),FragSwitcher.NON)
    }

    private val onEditPwd={
        replaceFragment(FragLevelSet(2)
                .setFrag(EditPwdFrag().also {

                }),FragSwitcher.RIGHT_IN)
    }

    private val onEditPhone={
        replaceFragment(FragLevelSet(2)
                .setFrag(ResetPhoneFrag().also {

                }),FragSwitcher.RIGHT_IN)
    }

    override fun setUpResumeComponent() {

    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }
}