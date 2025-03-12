package com.eki.parking.Controller.activity

import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.NotifyDetail.NotifyDetailFrag
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.R
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/07/27
 */
class NotifyDetailActivity:TitleBarActivity(){

    override fun initActivityView() {
        val data=intent.getParcel<ResponseInfo.EkiNotify>(AppFlag.DATA_FLAG)

//        Log.w("Notify data->$data")
        replaceFragment(FragLevelSet(1).setFrag(NotifyDetailFrag().also { it.setData(data) }),FragSwitcher.NON)

    }

    override fun setUpResumeComponent() {

    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }
}