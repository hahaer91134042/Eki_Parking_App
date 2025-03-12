package com.eki.parking.Controller.activity.frag.Main.child

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.Main.adapter.ReserveCalendarAdapter
import com.eki.parking.Controller.activity.frag.SiteDetail.child.SiteBaseInfoFrag
import com.eki.parking.Controller.calendar.EkiCalendar
import com.eki.parking.Controller.calendar.marker.SimpleMarker
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Controller.listener.CalendarSelectListener
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragParkingCalendarBinding
import com.eki.parking.extension.toDateTime
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit


class ReserveCalendarFragment : BaseFragment<SiteBaseInfoFrag>(), ISetData<ArrayList<EkiOrder>>,
    IFragViewBinding, CalendarSelectListener<Calendar?> {

    private lateinit var binding: FragParkingCalendarBinding

    private lateinit var ekiCalendar: EkiCalendar<CalendarView, Calendar?>
    private var orderList = ArrayList<EkiOrder>()
    private var calendarDataList = ArrayList<CalendarOrderData>()
    private var selectDate = DateTime()
    private lateinit var adapter:ReserveCalendarAdapter

    var toReserveDetail: ((EkiOrder) -> Unit)? = null

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragParkingCalendarBinding.inflate(inflater, container, false)
        adapter = ReserveCalendarAdapter(requireContext())
        return binding
    }

    override fun initFragView() {
        ekiCalendar = EkiCalendar.loadSimple(binding.calendarView)
    }

    private fun creatCalendarMarker() {
        ekiCalendar.addCalendarMarker(ArrayList<SimpleMarker>().apply {
            calendarDataList.forEach {
                add(SimpleMarker(DateTime().set(it.date), getColor(R.color.Eki_orange_1)))
            }
        })
    }

    override fun onResumeFragView() {
        ekiCalendar.onResume(binding.calendarView)
        ekiCalendar.addDateSelectListener(this)
        binding.calendarMonthBar.setCalendar(ekiCalendar)

        createCalendarOrderList()
    }

    override fun onPause() {
        super.onPause()
        ekiCalendar.removeDateSelectListener(this)
        binding.calendarMonthBar.onPause()
    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar.notNull { c ->
            selectDate = c.toDateTime()
            createCalendarOrderList()
        }
    }

    private fun createCalendarOrderList() {
        val list = orderList.filter {
            (it.ReservaTime.startDateTime().date == selectDate.date ||
                    it.ReservaTime.endDateTime().date == selectDate.date) &&
                    it.isReserved()
        }.sortedBy {
            it.ReservaTime.startDateTime().toStamp()
        }.toArrayList()

        adapter.apply {
            seeMoreCallback = { order ->
                toReserveDetail?.invoke(order)
            }
        }
        binding.reserveCalendarRecycleView.adapter = adapter

        if (list.isEmpty()) {
            binding.reserveCalendarRecycleView.visibility = View.GONE
        } else {
            binding.reserveCalendarRecycleView.visibility = View.VISIBLE
            adapter.submitData(list)
        }
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {
    }

    override fun onYearChange(year: Int) {
    }

    override fun setData(data: ArrayList<EkiOrder>?) {
        data.notNull {
            orderList = it
            createCalendarData()
            createCalendarOrderList()
        }
    }

    private fun createCalendarData() {
        val now = DateTime()
        calendarDataList.clear()
        orderList.forEach { order ->
            val start = order.ReservaTime.startDateTime()
            val end = order.ReservaTime.endDateTime()
            if (start.date >= now.date && order.isReserved()) {
                if (calendarDataList.any { it.date == start.date }) {
                    calendarDataList.first { it.date == start.date }.orders.add(order)
                } else if (calendarDataList.any { it.date == end.date }) {
                    //跨日資料加入
                    calendarDataList.first { it.date == end.date }.orders.add(order)
                } else {
                    calendarDataList.add(CalendarOrderData(start.date).apply { orders.add(order) })
                    //若有跨日就再加一次,讓marker出現在跨日天數上
                    if (start.date != end.date) {
                        calendarDataList.add(CalendarOrderData(end.date).apply { orders.add(order) })
                    }
                }
            }
        }
        creatCalendarMarker()
    }

    private data class CalendarOrderData(var date: DateUnit) {
        var orders = ArrayList<EkiOrder>()
    }
}