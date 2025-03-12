package com.eki.parking.Controller.activity

import android.view.View
import com.eki.parking.Controller.activity.abs.IActivityBackEvent
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.Main.ChargeParkingSpaceFragment
import com.eki.parking.Controller.activity.intent.CheckOutProcessIntent
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.extension.sqlDataListAsync
import com.hill.devlibs.extension.startActivityAnim
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.frag.FragController
import com.hill.devlibs.impl.IActivityFeatureSet


/**
 * Created by Linda
 * */
class ChargeParkingSpaceActivity : TitleBarActivity(), IActivityBackEvent {

    private var toMain: () -> Unit = {
        toMain()
    }
    private var toCheckout: (order: EkiOrder) -> Unit = { order ->
        startActivityAnim(CheckOutProcessIntent(this,order))
    }
    private val chargeParkingFrag = ChargeParkingSpaceFragment()
    private lateinit var orderList:List<EkiOrder>

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun initActivityView() {
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).visibility = View.GONE

        sqlDataListAsync<EkiOrder> { list->
            orderList = list.filter{
                (it.isBeSettle()|| it.isBeCheckOut()) && (it.Cp != null ||it.Cp?.Serial != "") }
                .sortedBy { it.ReservaTime.startDateTime().toStamp() }

            if (orderList.isEmpty()) {
                toMain()
            } else {
                chargeParkingFrag.setData(orderList.toArrayList())

                replaceFragment(FragLevelSet(1)
                    .setFrag(chargeParkingFrag.also {
                        it.toMain = toMain
                    })
                    , FragController.FragSwitcher.SWITCH_FADE)
            }
        }
    }

    override fun setUpResumeComponent() {
    }

    override fun setActivityFeature(): IActivityFeatureSet = object : IActivityFeatureSet() {
        override val menuRes: Int
            get() = R.menu.menu_order_overview_activity
    }

    override fun onBackPress(): Boolean {
        toMain()
        return false
    }
}