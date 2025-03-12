package com.eki.parking.Controller.activity.frag.BillingOverview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.filter.NoneFilter
import com.eki.parking.Controller.filter.OrderDateFilter
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IFilterChangeBack
import com.eki.parking.Controller.impl.IFilterSet
import com.eki.parking.Controller.sort.OrderAscSort
import com.eki.parking.Controller.sort.OrderDescSort
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragOrderOverviewFilterBinding
import com.eki.parking.extension.color
import com.eki.parking.extension.drawable
import com.eki.parking.extension.setList
import com.hill.devlibs.EnumClass.OrderBy
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.ext.monthEqual

/**
 * Created by Hill on 2020/05/19
 */

class OrderFilterFrag : SearchFrag(), ISetData<List<EkiOrder>>, IFragViewBinding {

    private lateinit var binding: FragOrderOverviewFilterBinding
    private lateinit var sortSelect: SortSelectCtrl
    var filterBack: IFilterChangeBack<EkiOrder>? = null

    private lateinit var orderList: List<EkiOrder>

    private val selectDateList = ArrayList<DateSelectItem>()

    private var datePosition = 0

    var onBack: () -> Unit = {}

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragOrderOverviewFilterBinding.inflate(inflater, container, false)
        return binding
    }

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.Filter)
    }

    override fun initFragView() {

        toolBarTitle = getString(R.string.Filter)

        val strList = createDateList()
        binding.datePicker.setList(strList)
        binding.datePicker.setOnValueChangedListener { _, _, newVal ->
            datePosition = newVal
        }

        sortSelect = SortSelectCtrl(descView = binding.descSortText, ascView = binding.ascSortText)

        binding.determinBtn.setOnClickListener {

            val newFilter = when (datePosition) {
                0 -> NoneFilter()
                else -> OrderDateFilter(selectDateList[datePosition].date)
            }
            filterBack?.onFilterChange(IFilterSet.FilterList(newFilter))
            filterBack?.onSortChange(
                when (sortSelect.selectSortBy()) {
                    OrderBy.DESC -> OrderDescSort()
                    else -> OrderAscSort()
                }
            )

            onBack()

        }

    }

    private fun createDateList(): ArrayList<String> {
        selectDateList.clear()
        val orderDate = ArrayList<DateUnit>()
        orderList.forEach { order ->
            val start = order.ReservaTime.startDateTime()
            if (orderDate.none { it.monthEqual(start.date) }) {
                orderDate.add(DateUnit(start.year, start.month, 1))
            }
        }
        orderDate.sortByDescending { it.toStamp() }

        selectDateList.add(DateSelectItem(getString(R.string.All), DateUnit(0, 1, 1)))
        orderDate.forEach {
            selectDateList.add(
                DateSelectItem(
                    getString(R.string.year_month_format, it.year,it.month),it
//                    dateTemp.messageFormat(
//                        it.year.toString(),
//                        it.month.mod02d()
//                    ), it
                )
            )
        }

        return ArrayList<String>().apply {
            selectDateList.forEach { add(it.str) }
        }
    }

    private data class DateSelectItem(val str: String, val date: DateUnit)
    private inner class SortSelectCtrl(val descView: TextView, val ascView: TextView) {
        var descSortBtn = object : SortBtnCtrl(true) {
            override val view: TextView
                get() = descView
        }
        var ascSortBtn = object : SortBtnCtrl(false) {
            override val view: TextView
                get() = ascView
        }

        init {
            descView.setOnClickListener {
                descSortBtn.isSelect = true
                ascSortBtn.isSelect = false
            }
            ascView.setOnClickListener {
                descSortBtn.isSelect = false
                ascSortBtn.isSelect = true
            }
        }

        fun selectSortBy(): OrderBy = when {
            descSortBtn.isSelect -> OrderBy.DESC
            else -> OrderBy.ASC
        }
    }

    private abstract class SortBtnCtrl(isSelect: Boolean) : IViewControl<TextView>(),
        IViewControl.IClickViewSet<TextView> {

        var isSelect = isSelect
            set(value) {
                field = value
                clickViewSet(view)
            }

        init {
            clickViewSet(view)
        }

        final override fun clickViewSet(clickView: TextView) {
            when (isSelect) {
                true -> selectStyle(clickView)
                else -> unSelectStyle(clickView)
            }
        }

        private fun selectStyle(view: TextView) {
            view.background = drawable(R.drawable.stroke_round_corner_dark_gray1)
            view.setTextColor(color(R.color.dark_gray1))
        }

        private fun unSelectStyle(view: TextView) {
            view.background = drawable(R.drawable.stroke_round_corner_light_gray6)
            view.setTextColor(color(R.color.light_gray6))
        }
    }

    override fun setData(data: List<EkiOrder>?) {
        data.notNull { orderList = it }
    }
}