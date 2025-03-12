package com.eki.parking.Controller.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.IToolBarItemSet
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.BillingOverview.*
import com.eki.parking.Controller.filter.NoneFilter
import com.eki.parking.Controller.impl.IFilterChangeBack
import com.eki.parking.Controller.impl.IFilterSet
import com.eki.parking.Controller.sort.OrderDescSort
import com.eki.parking.Model.response.ManagerLocIncomeResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.extension.color
import com.eki.parking.extension.dimen
import com.eki.parking.extension.sqlDataListAsync
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.printValue
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/05/15
 * 目前 基本上跟ConsumptionFilterFrag 差不多
 */
class BillingOverviewActivity : TitleBarActivity(), TitleBarActivity.SetToolbar, IToolBarItemSet {

    private lateinit var loc: ArrayList<ManagerLocation>
    private var restoreText: RestoreText? = null
    private var restoreItem: MenuItem? = null

    private var overviewFrag = OrderOverviewFrag()
    private var filterFrag = OrderFilterFrag()
    private var menuTitle: String? = null

    private var incomeStartTime = ""
    private var incomeEndTime = ""

    companion object {
        const val flagIn = 0
        const val flagOut = 1
    }

    //預設的過濾條件
    private var filterSet = object : IFilterSet<EkiOrder>() {
        override fun sortBy(): SortBySet<EkiOrder> = OrderDescSort()
        override fun filters(): FilterList<EkiOrder> = FilterList(NoneFilter())
    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar
    override fun setActivityFeature(): IActivityFeatureSet = object : IActivityFeatureSet() {
        override val menuRes: Int
            get() = R.menu.menu_order_overview_activity
    }
    override fun setUpToolbar(toolbar: Toolbar) {
        toolbar.setBackgroundColor(color(R.color.Eki_green_2))
    }

    override fun initActivityView() {
        openToolBarActionView()
        initOrderOverview()

        sqlDataListAsync<ManagerLocation> {
            loc = it
            replaceFragment(FragLevelSet(1)
                .setFrag(overviewFrag.also { frag ->
                    frag.setFilter(filterSet)
                    frag.setData(loc)
                    frag.onFilter = onFilterClick
                }), FragSwitcher.LEFT_IN)
        }
    }

    override fun itemRes(): Int = R.id.restore
    override fun initMenuItem(item: MenuItem) {
        restoreItem = item
        restoreText = RestoreText(this, menuTitle)
        item.actionView = restoreText
    }
    private fun initOrderOverview() {
        menuTitle = getString(R.string.Enquiry_for_all_parking_spaces)
        restoreItem?.let { initMenuItem(it) }

        openToolBarActionView()
    }

    private fun replaceOrderOverview() {
        initOrderOverview()

        replaceFragment(FragLevelSet(1)
            .setFrag(overviewFrag.also { frag ->
                frag.setFilter(filterSet)
                frag.setData(loc)
                frag.onFilter = onFilterClick
            }), FragSwitcher.LEFT_IN)
    }

    private fun replaceActualIncome(flag:Int) {
        /*使用ActualIncomeFrag() 而不是 actualIncomeFrag 是因為用 actualIncomeFrag 的話生成畫面會資料會有錯 */
        if (flag == 0) { //in
            replaceFragment(
                FragLevelSet(2)
                .setFrag(ActualIncomeFrag().also { it.onStoreIncomeDate = restoreIncomeDate })
                , FragSwitcher.RIGHT_IN)
        } else { //out
            replaceFragment(
                FragLevelSet(2)
                    .setFrag(ActualIncomeFrag().also {
                        it.startDate = incomeStartTime
                        it.endDate = incomeEndTime
                        it.onStoreIncomeDate = restoreIncomeDate
                    })
                , FragSwitcher.LEFT_IN)
        }
    }
    private val restoreIncomeDate:(String,String) -> Unit = {sD,eD ->
        incomeStartTime = sD
        incomeEndTime = eD
    }

    private fun replaceDefaultDetails(claimant:String,serialNumber:String) {
        val defaultDetailsFrag = DefaultDetailsFrag()
        replaceFragment(FragLevelSet(3)
            .setFrag(defaultDetailsFrag.also {
                it.getData(claimant,serialNumber)
                it.startTime = incomeStartTime
                it.endTime = incomeEndTime
            }), FragSwitcher.RIGHT_IN)
    }

    private var onFilterClick: (List<EkiOrder>) -> Unit = { list ->
        replaceFragment(FragLevelSet(2)
            .setFrag(filterFrag.also { frag ->
                frag.setData(list)
                menuTitle = getString(R.string.Restore_default)
                restoreItem?.let { initMenuItem(it) }
                frag.onBack = { replaceOrderOverview() }
                frag.filterBack = onFilterChange
            }), FragSwitcher.RIGHT_IN)
    }

    private var onFilterChange = object : IFilterChangeBack<EkiOrder> {
        override fun onFilterChange(filters: IFilterSet.FilterList<EkiOrder>) {
            filterSet.filters = filters
        }

        override fun onSortChange(sortby: IFilterSet.SortBySet<EkiOrder>) {
            filterSet.sortBy = sortby
        }
    }

    override fun setUpResumeComponent() {
        registerReceiver(AppFlag.DefaultDetails,AppFlag.LocIncome)
    }

    override fun onCatchReceive(action: String?, intent: Intent?) {
        when(action){
            AppFlag.DefaultDetails->{
                val claimant = intent?.getStringExtra("claimant") ?: ""
                val serialNumber = intent?.getStringExtra("serialNumber") ?: ""
                closeToolBarActionView()
                replaceDefaultDetails(claimant,serialNumber)
            }
            AppFlag.LocIncome -> {
                replaceFragment(FragLevelSet(2)
                    .setFrag(LocIncomeFrag().also {
                        val result = intent?.getSerializableExtra(AppFlag.DATA_FLAG).notNull { data->
                            data.printValue()
                            data as ManagerLocIncomeResponse.IncomeResult
                        }
                        result?.let { resultNotNull -> it.receiveData = resultNotNull }
                    }), FragSwitcher.NON)
            }
        }
    }

    override fun onBackPressed() {
        val sf = supportFragmentManager.findFragmentById(R.id.fragViewLoader)
        val fragment = sf?.fragmentManager?.fragments?.firstOrNull()

        when(fragment) {
            is OrderOverviewFrag -> {toMain()}
            is OrderFilterFrag -> {replaceOrderOverview()}
            is ActualIncomeFrag -> {replaceOrderOverview()}
            is LocIncomeFrag -> {replaceOrderOverview()}
            is DefaultDetailsFrag -> {replaceActualIncome(flagOut)}
            else -> super.onBackPressed()
        }
    }

    private inner class RestoreText(context: Context, title: String?) : AppCompatTextView(context) {
        init {
            text = title
            setTextColor(Color.WHITE)
            textSize = dimen(R.dimen.text_size_5)
            setPadding(0, 0, 30, 0)

            setOnClickListener {
                if (title == getString(R.string.Restore_default)) {
                    filterSet.filters = IFilterSet.FilterList(NoneFilter())
                    filterSet.sortBy = OrderDescSort()
                    replaceOrderOverview()
                }else if(title == getString(R.string.Enquiry_for_all_parking_spaces)){
                    if(loc != null && loc?.size!! > 0){
                        isEnabled = true
                        replaceActualIncome(flagIn)
                        closeToolBarActionView()
                    }else {
                        isEnabled = false
                        showToast(getString(R.string.No_orders_have_been_checked_out))
                    }
                }
            }
        }
    }
}