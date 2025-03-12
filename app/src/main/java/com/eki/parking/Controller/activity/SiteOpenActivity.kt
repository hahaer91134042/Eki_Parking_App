package com.eki.parking.Controller.activity

import androidx.appcompat.widget.Toolbar
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.SiteOpen.OpenCalendarFrag
import com.eki.parking.Controller.activity.frag.SiteOpen.SiteOpenListFrag
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.extension.sqlDataFromArgs
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/06/24
 */
class SiteOpenActivity:TitleBarActivity(),TitleBarActivity.SetToolbar{

    private var siteFrag=SiteOpenListFrag()

    override fun initActivityView() {

        replaceFragment(FragLevelSet(1)
                .setFrag(siteFrag.also { it.onSelectLoc=onSelectLoc }),FragSwitcher.NON)

    }

    private var onSelectLoc:(ManagerLocation)->Unit={loc->
        Log.w("Select loc->${loc.Info?.Content}")
        //這邊改成去抓sql裡面的新loc 因為有新增open set會直接更新sql 所以一定是最新
        replaceFragment(FragLevelSet(2)
                .setFrag(OpenCalendarFrag()
                        .also { frag->frag.setData(sqlDataFromArgs(loc)) }),
                FragSwitcher.RIGHT_IN)
    }


    override fun setUpResumeComponent() {

    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        toolbar.setBackgroundColor(getColor(R.color.Eki_green_2))
    }

}