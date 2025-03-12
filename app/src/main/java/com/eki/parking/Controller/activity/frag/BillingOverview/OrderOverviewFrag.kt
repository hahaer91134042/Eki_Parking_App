package com.eki.parking.Controller.activity.frag.BillingOverview

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.AppResultCode
import com.eki.parking.Controller.activity.frag.BillingOverview.child.OrderOverviewListFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IFilterSet
import com.eki.parking.Controller.process.ManagerLocIncomeProcess
import com.eki.parking.Controller.process.ManagerLocOrderProcess
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.EnumClass.OrderStatus
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.ViewParams
import com.eki.parking.View.impl.ISpinnerContainer
import com.eki.parking.View.impl.ISpinnerItem
import com.eki.parking.View.spinner.SimpleSelectSpinner
import com.eki.parking.databinding.FragOrderOverviewBinding
import com.eki.parking.extension.color
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.drawable
import com.eki.parking.extension.screenWidth
import com.hill.devlibs.extension.isNull
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.onNotNull
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.frag.FragController
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.ext.daySpan
import com.hill.devlibs.time.ext.monthEndDate
import com.hill.devlibs.time.ext.monthStartDate

/**
 * Created by Hill on 2020/05/15
 */
class OrderOverviewFrag : SearchFrag(), ISetData<ArrayList<ManagerLocation>>, IFragViewBinding {

    private var locList: ArrayList<ManagerLocation> = ArrayList()
    private var selectLoc: ManagerLocation? = null
    private var locOrder = ArrayList<ResponseInfo.ManagerLocOrder>()
    private lateinit var filterSet: IFilterSet<EkiOrder>
    private lateinit var binding: FragOrderOverviewBinding

    var onFilter: (List<EkiOrder>) -> Unit = {}
    var onSelectLoc: (ManagerLocation?) -> Unit = {}

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragOrderOverviewBinding.inflate(inflater, container, false)
        return binding
    }

    override fun initFragView() {
        toolBarTitle = getString(R.string.Billing_overview_string)

        selectLoc = locList.firstOrNull()

        val locSpinner = LocNameSpinner()
        binding.orderOverviewSpinnerContainer.initContainer(locSpinner)
        locSpinner.setSelectListener { position, _ ->
            selectLoc = if (locList.isNotEmpty()) {
                locList[position]
            } else {
                null
            }
//            onSelectLoc(selectLoc)
            setSelectLocOrderFrag()
        }

        if (locOrder.isEmpty()) {
            loadOrder()
        } else {
            setSelectLocOrderFrag()
        }

        binding.orderOverviewFilter.setOnClickListener {
            if (locOrder.isNotEmpty()) {
                val list = ArrayList<EkiOrder>()
                locOrder.forEach {
                    list.addAll(it.Order.map { o -> o.toSql() })
                }
                onFilter(list)

            } else
                showToast(getString(R.string.error_no_order))
        }
        binding.orderOverviewRefreshView.setOnRefreshListener {
            loadOrder()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            AppResultCode.OrderStateChange -> loadOrder()
        }
    }

    private fun loadOrder() {
        if (locList.isNotEmpty()) {
            binding.orderOverviewRefreshView.setColorSchemeResources(R.color.Eki_green_2)
            binding.orderOverviewRefreshView.isRefreshing = false
            binding.orderOverviewProgress.relativeLayout.visibility = View.VISIBLE
            LocOrderProcess().apply {
                onSuccess = { result ->
                    binding.orderOverviewProgress.relativeLayout.visibility = View.GONE
                    locOrder = result
                    setSelectLocOrderFrag()
                }
                onFail = {
                    binding.orderOverviewProgress.relativeLayout.visibility = View.GONE
                }
            }.run()
        }
    }

    private var locSelectOrders: List<EkiOrder>? = null

    //當選擇income之後 要去下載該地點的 income
    private var onSelectIncomeDate: (DateUnit) -> Unit = { time ->
        val start = time.monthStartDate
        val end = time.monthEndDate
        //Log.d("select date->$time start->$start end->$end")
        selectLoc.notNull { loc ->
            binding.orderOverviewProgress.relativeLayout.visibility = View.VISIBLE
            ManagerLocIncomeProcess(
                context,
                listOf(loc),
                listOf(RequestBody.TimeSpan(start, end))
            ).apply {
                onFail = {
                    binding.orderOverviewProgress.relativeLayout.visibility = View.GONE
                }
                onSuccess = { list ->
                    binding.orderOverviewProgress.relativeLayout.visibility = View.GONE
                    list.firstOrNull { i ->
                        i.SerNum == loc.Info?.SerialNumber
                    }.isNull {
                        showToast(getString(R.string.error_no_income))
                    }.onNotNull { result ->
                        val intent = Intent(AppFlag.LocIncome)
                        intent.putExtra(AppFlag.DATA_FLAG, result)
                        requireContext().sendBroadcast(intent)
                    }
                }
            }.run()
        }
    }

    private fun setSelectLocOrderFrag() {
        if (locList.isNotEmpty()) {

            locSelectOrders = locOrder.firstOrNull { it.Id == selectLoc?.Id }
                ?.Order?.map { o -> o.toSql() }
                ?.filter { o -> o.orderStatus == OrderStatus.CheckOut }

            if (locSelectOrders != null) {
                val orders = filterSet.filter(locSelectOrders!!).toArrayList()

                if (orders.isNotEmpty()) {
                    replaceFragWithCacheChildFrag(
                        ChildCacheLevel(1)
                            .setFrag(OrderOverviewListFrag().also {
                                it.clickIncomeDate = onSelectIncomeDate
                                it.setData(orders)
                            }), FragController.FragSwitcher.SWITCH_FADE
                    )
                    setNoOrder(false)
                } else {
                    setNoOrder(true)
                }
            } else {
                setNoOrder(true)
            }
        } else {
            setNoOrder(true)
        }
    }

    private fun setNoOrder(isNoOrder: Boolean) {
        if (isNoOrder) {
            binding.orderOverviewNoOrder.visibility = View.VISIBLE
            binding.orderOverviewRefreshView.visibility = View.GONE
        } else {
            binding.orderOverviewNoOrder.visibility = View.GONE
            binding.orderOverviewRefreshView.visibility = View.VISIBLE
        }
    }

    override fun setData(data: ArrayList<ManagerLocation>?) {
        data.notNull { locList = it.sortedBy { loc -> loc.Id }.toArrayList() }
    }

    fun setFilter(set: IFilterSet<EkiOrder>) {
        set.notNull { filterSet = it }
    }

    private inner class LocOrderProcess : ManagerLocOrderProcess(context) {
        override val body: SendIdBody
            get() = SendIdBody(EkiApi.ManagerLocOrder).also {
                it.timeSpan = RequestBody.TimeSpan().also { span ->
                    val end = DateTime()
                    val start = end - 180.daySpan
                    span.start = start.toString()
                    span.end = end.toString()
                }
                it.id = locList.map { loc -> loc.Id }.toArrayList()
            }
    }

    private inner class LocNameSpinner : SimpleSelectSpinner(
        context,
        if (locList.size > 0) {
            locList.map { it.Info?.Content ?: "" }.toArrayList()
        } else {
            arrayListOf(getString(R.string.no_location))
        }
    ), ISpinnerItem, ISpinnerContainer {
        override val itemRes: Int
            get() = R.layout.item_autosize_textview

        override fun setUpItem(view: View, position: Int) {
            val item = view.findViewById<TextView>(R.id.textView)
            item.layoutParams = LayoutParams(screenWidth() * 3 / 5, dpToPx(30f))
            item.text = optionList[position]
            item.setPadding(15, 5, 5, 5)
            item.setTextColor(color(R.color.text_color_1))
            item.gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }

        override val spinner: Spinner
            get() {
                this.layoutParams =
                    LinearLayout.LayoutParams(ViewParams.WRAP_CONTENT, ViewParams.WRAP_CONTENT)
                        .also {
                            it.gravity = Gravity.CENTER_VERTICAL
                            it.setMargins(5, 5, 10, 5)
                        }
                this.background = drawable(android.R.color.transparent)

                return this
            }
        override val icon: ImageView
            get() {
                val img = ImageView(context)
                img.layoutParams = LinearLayout.LayoutParams(dpToPx(18f), dpToPx(7f)).also {
                    it.setMargins(10, 5, 20, 5)
                }
                img.setImageResource(R.drawable.icon_button_drop)

                return img
            }

        override fun containerSet(parent: LinearLayout) {
            parent.background = drawable(R.drawable.stroke_spinner_gray6)
        }
    }
}