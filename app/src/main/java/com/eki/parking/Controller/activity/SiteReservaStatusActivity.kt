package com.eki.parking.Controller.activity

import androidx.appcompat.widget.Toolbar
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.SiteReservaStatus.ManagerReservaStatusFrag
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.extension.color
import com.eki.parking.extension.sqlDataListAsync
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/05/07
 */

class SiteReservaStatusActivity:TitleBarActivity(),TitleBarActivity.SetToolbar{

    private lateinit var loc:ArrayList<ManagerLocation>

    override fun initActivityView() {

        sqlDataListAsync<ManagerLocation> {
            loc=it
            replaceFragment(FragLevelSet(1).setFrag(ManagerReservaStatusFrag().apply {setData(loc)}),FragSwitcher.NON)
        }

    }
    override fun setUpResumeComponent() {}

    override fun setUpActivityView(): Int =R.layout.activity_title_bar

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        toolbar.setBackgroundColor(color(R.color.Eki_green_2))
    }

}