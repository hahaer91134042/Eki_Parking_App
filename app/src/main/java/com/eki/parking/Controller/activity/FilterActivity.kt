package com.eki.parking.Controller.activity

import android.content.Intent
import com.eki.parking.AppFlag
import com.eki.parking.AppResultCode
import com.eki.parking.Controller.activity.abs.BaseActivity
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.Filter.FilterFrag
import com.eki.parking.Model.sql.LoadLocationConfig
import com.eki.parking.R
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet
import com.hill.devlibs.model.ValueObjContainer

class FilterActivity: TitleBarActivity(),
                      BaseActivity.ActivityBackResult{
//                      TitleBarActivity.SetToolbar {

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    private lateinit var filterFrag:FilterFrag
    private lateinit var config:LoadLocationConfig

    private var backData:Intent?=null

    override fun initActivityView() {

        config=intent.getParcel(AppFlag.DATA_FLAG)

//        config.printValue()

        filterFrag = FilterFrag().also { frag ->
            frag.setData(config)
            frag.onFinishSearch = { config ->
                backData = Intent().apply {
                    putExtra(AppFlag.DATA_FLAG, ValueObjContainer<LoadLocationConfig>().also {
                        it.setValueObjData(config)
                    })
                }
                toMain()
            }
        }
        replaceFragment(FragLevelSet(1)
            .setFrag(filterFrag),FragSwitcher.NON)

    }

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

//    override fun setUpToolbar(toolbar: Toolbar) {
//        toolbar.setBackgroundColor(color(R.color.Eki_orange_4))
//    }

    override fun setUpResumeComponent() {

    }

    override fun activityBackCode(): Int =AppResultCode.OnFilterResult
    override fun backData(): Intent?=backData
//    override fun backData(): Intent?=Intent().apply {
//        putExtra(AppFlag.DATA_FLAG,ValueObjContainer<LoadLocationConfig>().also {
//            it.setValueObjData(filterFrag.searchResult)
//        })
//    }

//    override fun onBackPressed() {
//
//        if(sP?.getString("MainPage","") == "SteamLocomotiveSearch" || sP?.getString("MainPage","") == "CarSearch" || sP?.getString("MainPage","") == "LocomotiveSearch") {
//            val intentFlag = Intent(AppFlag.OnDateSearch)
//            val intentPage = Intent(this, MainActivity::class.java)
//            this.startActivity(intentPage)
//            this.sendBroadcast(intentFlag)
//        }else if((sP?.getString("MainPage","") == "SteamLocomotiveClick" || sP?.getString("MainPage","") == "CarClick")){
//            resetFilter()
//            editor?.remove("RbName")?.apply()
//            super.onBackPressed()
//        }else{
//            editor?.clear()?.apply()
//            super.onBackPressed()
//        }
//
//        //print("取得Filter " + sP?.getString("MainPage",""))
//
//    }

}