package com.eki.parking.Controller.activity

import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.Html.WebPageFrag
import com.eki.parking.R
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/10/23
 */
class HtmlActivity:TitleBarActivity() {

    override fun initActivityView() {
        val url=intent.getStringExtra(AppFlag.DATA_FLAG)
        replaceFragment(FragLevelSet(1).setFrag(WebPageFrag().also { it.setData(url) }),
        FragSwitcher.NON)
    }

    override fun setUpResumeComponent() {

    }


    override fun setUpActivityView(): Int =R.layout.activity_title_bar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }
}