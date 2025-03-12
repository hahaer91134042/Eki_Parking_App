package com.eki.parking.Controller.activity

import android.content.Intent
import android.view.MenuItem
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.IToolBarItemSet
import com.eki.parking.Controller.activity.abs.IToolBarItemSetList
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.CarSetting.AddCarFrag
import com.eki.parking.Controller.activity.frag.CarSetting.CarDeleteFrag
import com.eki.parking.Controller.activity.frag.CarSetting.CarDetailFrag
import com.eki.parking.Controller.activity.frag.CarSetting.CarListFrag
import com.eki.parking.Model.DTO.VehicleInfo
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.extension.sqlData
import com.hill.devlibs.extension.isNull
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.onNotNull
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2019/12/25
 */
class CarSettingActivity: TitleBarActivity(),
                          IToolBarItemSetList{

    private var deleteMenuItem:MenuItem?=null
    private var itemVisible:Boolean=false
        set(value) {
            field=value
            deleteMenuItem?.isVisible=value
        }

    override fun setUpActivityView(): Int =R.layout.activity_title_bar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_car_set_activity
    }

    override fun initActivityView() {
        onCarList.invoke()
    }

    private var onCarList:()->Unit={
        val member= sqlData<EkiMember>()
//        member?.vehicle?.forEach {
//            Log.w("vehicle name->${it.Name}")
//        }
        member?.vehicle.isNull {
            replaceFragment(FragLevelSet(1).setFrag(CarListFrag().also {
                it.isNoData = true
                itemVisible=it.visible
                it.onAddCarClick=onAddCarClick
                it.onCarDetail=onCarDetail
            }),FragSwitcher.SWITCH_FADE)
        }.onNotNull {vehicleList->
            if (vehicleList.isEmpty()) {
                replaceFragment(FragLevelSet(1).setFrag(CarListFrag().also {
                    it.isNoData = true
                    itemVisible=it.visible
                    it.onAddCarClick=onAddCarClick
                    it.onCarDetail=onCarDetail
                }),FragSwitcher.SWITCH_FADE)
            }
            else {
                replaceFragment(FragLevelSet(1).setFrag(CarListFrag().also {
                    it.isNoData = false
                    itemVisible=it.visible
                    it.onAddCarClick=onAddCarClick
                    it.onCarDetail=onCarDetail
                }),FragSwitcher.SWITCH_FADE)
            }
        }
    }

    private var onAddCarClick:()->Unit={
        replaceFragment(FragLevelSet(2).setFrag(AddCarFrag().also {
            itemVisible=it.visible
            }),FragSwitcher.RIGHT_IN)
    }

    private var onCarDetail:(VehicleInfo)->Unit={info->
        replaceFragment(FragLevelSet(2).setFrag(
                CarDetailFrag().also {
                    itemVisible=it.visible
                    it.setData(info)}),FragSwitcher.RIGHT_IN)
    }

    private var onCarDelete={
        replaceFragment(FragLevelSet(2).setFrag(
                CarDeleteFrag().also {
                    itemVisible=it.visible
                    }),FragSwitcher.SWITCH_FADE)
    }

    override fun setUpResumeComponent() {
        registerReceiver(AppFlag.CarDelete)
    }

    override fun onCatchReceive(action: String?, intent: Intent?) {
        super.onCatchReceive(action, intent)
        if(action == AppFlag.CarDelete){
            menuItemClick()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        sqlData<EkiMember>()?.vehicle.notNull {
            itemVisible=it.size>0
        }
    }

    override fun itemSetList(): ArrayList<IToolBarItemSet> =
            ArrayList<IToolBarItemSet>().apply {
                add(object :IToolBarItemSet{
                    override fun itemRes(): Int =R.id.deleteItem

                    override fun initMenuItem(item: MenuItem) {
                        deleteMenuItem=item
                        item.setOnMenuItemClickListener {
                            //Log.d("On Delete click")
                            menuItemClick()
                            return@setOnMenuItemClickListener true
                        }

                        deleteMenuItem?.isVisible=itemVisible
                    }
                })
            }

    private fun menuItemClick(){
        onCarDelete.invoke()
    }
}