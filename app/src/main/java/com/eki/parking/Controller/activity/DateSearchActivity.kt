package com.eki.parking.Controller.activity

import android.content.Intent
import android.view.MenuItem
import com.eki.parking.AppFlag
import com.eki.parking.AppResultCode
import com.eki.parking.Controller.activity.abs.BaseActivity
import com.eki.parking.Controller.activity.abs.IToolBarItemSet
import com.eki.parking.R
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.DateSearch.DateSearchFrag
import com.eki.parking.Model.DTO.LocationSearchTime
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.extension.printValue
import com.hill.devlibs.frag.FragController
import com.hill.devlibs.impl.IActivityFeatureSet
import com.hill.devlibs.model.ValueObjContainer
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 02,10,2019
 */
class DateSearchActivity: TitleBarActivity(),
                          BaseActivity.ActivityBackResult,
                          IToolBarItemSet{


    private var backData:Intent?=null
    private var dateFrag=DateSearchFrag()

    override fun initActivityView() {

        var data=intent.getParcel<LocationSearchTime>(AppFlag.DATA_FLAG)

        data.printValue()

        replaceFragment(FragLevelSet(1)
            .setFrag(dateFrag.also { frag->
                frag.setData(data)
                frag.onSelectSearchTime={sTime->
                    Log.w("select time finish")
//                    sTime.printValue()
                    backData= Intent().apply {
                        putExtra(AppFlag.DATA_FLAG,ValueObjContainer<LocationSearchTime>().also {
                            it.setValueObjData(sTime)
                        })
                    }
                    toMain()
                }
            }),FragController.FragSwitcher.NON)
    }

    override fun setUpResumeComponent() {

    }
    //用來監聽menu的按鈕
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun toolBarRes(): Int =R.id.toolbar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_date_search

    }
//    override fun getTitleBarClass(): ActivityClassEnum =ActivityClassEnum.DateSearch
    override fun setUpActivityView(): Int = R.layout.activity_title_bar
    override fun activityBackCode(): Int =AppResultCode.OnDateSearchResult
    override fun backData(): Intent? =backData
    override fun itemRes(): Int =R.id.date_clean

    override fun initMenuItem(item: MenuItem) {
        item.setOnMenuItemClickListener {
            Log.d("clean search date")
            dateFrag.cleanSeach()
            true
        }
    }
}