package com.eki.parking.Controller.activity

import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.AboutMe.AboutMeFrag
import com.eki.parking.R
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/10/12
 */
class AboutMeActivity:TitleBarActivity() {


    override fun initActivityView() {
        replaceFragment(FragLevelSet(1).setFrag(AboutMeFrag()),FragSwitcher.NON)
    }

    override fun setUpResumeComponent() {

    }


    override fun setUpActivityView(): Int = R.layout.activity_title_bar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }
}