package com.eki.parking.Controller.activity

import androidx.appcompat.widget.Toolbar
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.LandRule.LandRuleFrag
import com.eki.parking.R
import com.eki.parking.extension.drawable
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/07/22
 */
class LandRuleActivity:TitleBarActivity(),
                       TitleBarActivity.SetToolbar{
    override fun initActivityView() {

        replaceFragment(FragLevelSet(1).setFrag(LandRuleFrag()),FragSwitcher.NON)

    }
    override fun setUpResumeComponent() {

    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        toolbar.background= drawable(R.color.Eki_green_2)
    }


}