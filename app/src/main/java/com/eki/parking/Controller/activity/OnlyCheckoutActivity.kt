package com.eki.parking.Controller.activity

import com.eki.parking.Controller.activity.abs.IActivityBackEvent
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.Main.OrderFragment
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.extension.hasCheckoutOrder
import com.eki.parking.extension.showProgress
import com.eki.parking.extension.sqlDataListAsync
import com.eki.parking.extension.string
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/09/14
 */
class OnlyCheckoutActivity : TitleBarActivity(), IActivityBackEvent {

    private lateinit var orderList: ArrayList<EkiOrder>
    private var orderFragment = OrderFragment()

    override fun initActivityView() {
        replaceFragment(
            FragLevelSet(1)
                .setFrag(orderFragment.also {
                }),
            FragSwitcher.NON
        )
    }

    override fun setUpResumeComponent() {
        var progress = showProgress()

        sqlDataListAsync<EkiOrder> { list ->
            progress?.dismiss()
            orderList = list

            orderFragment.setData(orderList)
        }
    }

    override fun onBackPress(): Boolean {
        if (orderList.hasCheckoutOrder()) {
            showToast(string(R.string.You_have_unsettled_paid_orders))
        } else {
            toMain()
        }

        return false
    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun setActivityFeature(): IActivityFeatureSet = object : IActivityFeatureSet() {
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }
}