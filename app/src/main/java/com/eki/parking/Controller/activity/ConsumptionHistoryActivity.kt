package com.eki.parking.Controller.activity

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.IToolBarItemSet
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.ConsumptionHistory.ConsumptionFragment
import com.eki.parking.Controller.activity.frag.ConsumptionHistory.child.ConsumptionDetailFragment
import com.eki.parking.Controller.activity.frag.OrderDetail.OrderArgueFrag
import com.eki.parking.Controller.process.LoadOrderProcess
import com.eki.parking.Model.EnumClass.ArgueSource
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 14,11,2019
 */

class ConsumptionHistoryActivity : TitleBarActivity(), IToolBarItemSet {

    private var consumptionFrag = ConsumptionFragment()
    private var consumptionDetailFrag = ConsumptionDetailFragment()
    private lateinit var filterIcon: FilterIcon
    private lateinit var orderList: ArrayList<EkiOrder>

    private var isDescend = true

    override fun initActivityView() {
        getEkiOrderData()
        replaceFragment(FragLevelSet(1).setFrag(consumptionFrag.apply {
            toDetail = { order ->
                filterIcon.setVisible(false)
                replaceFragment(FragLevelSet(2).setFrag(consumptionDetailFrag), FragSwitcher.NON)
                consumptionDetailFrag.setData(order)
            }
        }), FragSwitcher.NON)
    }

    override fun setUpResumeComponent() {
        registerReceiver(AppFlag.OnAppealOrder)
    }

    override fun onCatchReceive(action: String?, intent: Intent?) {
        val order = intent?.getParcel<EkiOrder>(AppFlag.DATA_FLAG)
        replaceFragment(FragLevelSet(3).setFrag(OrderArgueFrag().also {
            it.setData(order)
            it.argueSource = ArgueSource.User
            it.onArgueFinish = {
                order?.apply {
                    Argue = false
                }?.sqlSaveOrUpdate()
                onBackPressed()
            }
        }), FragSwitcher.RIGHT_IN)
    }

    override fun setActivityFeature(): IActivityFeatureSet = object : IActivityFeatureSet() {
        override val menuRes: Int
            get() = R.menu.menu_history_activity
    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar
    override fun itemRes(): Int = R.id.filterItem

    override fun initMenuItem(item: MenuItem) {
        filterIcon = FilterIcon(this).also {
            it.eventBack = object : FilterEventBack {
                override fun onStartFilter() {
                    isDescend = !isDescend
                    consumptionFrag.setListSorting(isDescend)
                }
            }
        }

        item.actionView = filterIcon
    }

    private fun getEkiOrderData() {
        object : LoadOrderProcess(this) {
            override fun onProcessOver() {
            }
        }.apply {
            onLocation = { list ->
                orderList = list
                replaceFragment(FragLevelSet(1).setFrag(consumptionFrag.apply {
                    this.setData(orderList.filter { it.isCheckOut() }.toArrayList())
                }), FragSwitcher.NON)
            }
            retryProcess = {
                getEkiOrderData()
            }
        }.run()
    }

    private interface FilterEventBack {
        fun onStartFilter()
    }

    private class FilterIcon(context: Context) : AppCompatImageView(context) {

        var eventBack: FilterEventBack? = null

        init {
            setPadding(0, 0, dpToPx(18f), 0)
            setOnClickListener {
                eventBack?.onStartFilter()
            }
            setImageResource(R.drawable.sort)
        }

        fun setVisible(visible: Boolean) {
            visibility = when (visible) {
                true -> View.VISIBLE
                else -> View.INVISIBLE
            }
        }
    }

    override fun onBackPressed() {
        filterIcon.setVisible(true)
        super.onBackPressed()
    }
}