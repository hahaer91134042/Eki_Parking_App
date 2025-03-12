package com.eki.parking.Controller.activity.frag.Reserva

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.Reserva.adaptor.ReserveTimeAdapter
import com.eki.parking.Controller.calendar.EkiCalendar
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.CalendarSelectListener
import com.eki.parking.Model.DTO.OrderReservaTime
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.Model.sql.LocationReserva
import com.eki.parking.R
import com.eki.parking.databinding.FragSelectDateTimeBinding
import com.eki.parking.extension.sqlDataFromArgs
import com.eki.parking.extension.toDateTime
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 30,10,2019
 */
class SelectDateTimeFrag : SearchFrag(),
    ISetData<EkiLocation>,
    CalendarSelectListener<Calendar?>, IFragViewBinding {

    private lateinit var binding: FragSelectDateTimeBinding

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar.notNull {
            setCalendarAdapter(it)
        }
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onYearChange(year: Int) {

    }

    private lateinit var loc: EkiLocation

    private lateinit var ekiCalendar: EkiCalendar<CalendarView, Calendar?>

    var onSelectReserveTime:
            ((reserveTime: OrderReservaTime, start: DateTime, end: DateTime) -> Unit)? = null

    override fun setData(data: EkiLocation?) {
        data.notNull { loc = it }
    }

    override fun initFragView() {
        ekiCalendar = EkiCalendar.loadSimple(binding.calendarView)
        binding.recycleView.useDrawableDivider(R.drawable.divider_light_gray3)
    }

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.Select_date_time)
        ekiCalendar.onResume(binding.calendarView)
        ekiCalendar.addDateSelectListener(this)
        binding.calendarMonthBar.setCalendar(ekiCalendar)

        startReserveTimeList()
    }

    override fun onPause() {
        super.onPause()
        ekiCalendar.removeDateSelectListener(this)
        binding.calendarMonthBar.onPause()
    }

    private lateinit var reserva: LocationReserva

    private fun startReserveTimeList() {
        reserva = sqlDataFromArgs(LocationReserva().apply { LocId = loc.Id })

        ekiCalendar.selectTime.notNull {
            setCalendarAdapter(it)
        }
    }

    private fun setCalendarAdapter(calendar: Calendar) {
        binding.recycleView.adapter =
            ReserveTimeAdapter(context, reserva, calendar.toDateTime()).also {
                it.onSelectReserveTime = { start, end ->
                    onSelectReserveTime?.invoke(OrderReservaTime().also { t ->
                        t.loc = loc.Id
                        t.start = start.toString()
                        t.end = end.toString()
                    }, start, end)
                }
            }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragSelectDateTimeBinding.inflate(inflater, container, false)
        return binding
    }
}