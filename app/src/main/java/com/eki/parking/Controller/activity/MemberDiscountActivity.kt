package com.eki.parking.Controller.activity

import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.MemberDiscount.DiscountListFrag
import com.eki.parking.R
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/06/20
 */
class MemberDiscountActivity:TitleBarActivity(){

    override fun initActivityView() {
        replaceFragment(FragLevelSet(1)
                .setFrag(DiscountListFrag()),
                FragSwitcher.NON)
    }

    override fun setUpResumeComponent() {

    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }
}