package com.eki.parking.Controller.activity

import com.eki.parking.Controller.activity.abs.IActivityBackEvent
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.reserve_detail.ReserveDetailFragment
import com.eki.parking.Model.model.EkiOrder2
import com.eki.parking.R
import com.google.gson.Gson
import com.hill.devlibs.frag.FragController
import com.hill.devlibs.impl.IActivityFeatureSet
import org.json.JSONObject


/**
 * Created by Linda
 * */
class ReserveDetailActivity : TitleBarActivity(), IActivityBackEvent {

    private val reserveDetailFrag = ReserveDetailFragment()
    private var orderData:EkiOrder2? = null

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun initActivityView() {
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).title =
            getString(R.string.Appointment_detail)

        try {
            val data = JSONObject(intent.getStringExtra("order"))

            orderData = Gson().fromJson(data.toString(),EkiOrder2::class.java)
        } catch (e:Exception) {

        }

        replaceFragment(FragLevelSet(1)
            .setFrag(reserveDetailFrag.also {
                it.toMain = {
                    toMain()
                }
            })
            , FragController.FragSwitcher.SWITCH_FADE)
        reserveDetailFrag.setData(orderData)
    }

    override fun setUpResumeComponent() {
    }

    override fun setActivityFeature(): IActivityFeatureSet = object : IActivityFeatureSet() {
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun onBackPress(): Boolean {
        toMain()
        return false
    }
}