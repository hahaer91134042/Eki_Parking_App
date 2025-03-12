package com.eki.parking.Controller.activity.frag.DateSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.calendar.EkiCalendar
import com.eki.parking.Controller.dialog.EkiTimePickerDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.CalendarMultiSelectListener
import com.eki.parking.Controller.listener.CalendarSelectListener
import com.eki.parking.Model.DTO.LocationSearchTime
import com.eki.parking.Model.collection.SearchDateTime
import com.eki.parking.R
import com.eki.parking.databinding.FragDateSearchBinding
import com.eki.parking.extension.toDateTime
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 02,10,2019
 */
class DateSearchFrag : SearchFrag(),
    ISetData<LocationSearchTime>,
    CalendarSelectListener<Calendar?>,
    CalendarMultiSelectListener<Calendar?>, IFragViewBinding {

    private lateinit var binding: FragDateSearchBinding
    private lateinit var ekiCalendar: EkiCalendar<CalendarView, Calendar?>
    private lateinit var monthStrArray: Array<String>
    private lateinit var weekStrArray: Array<String>
    private var selectDateTime = SearchDateTime()
    private lateinit var searchTime: LocationSearchTime
    var onSelectSearchTime: ((LocationSearchTime) -> Unit)? = null


    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        setUpMonthText(calendar)
    }

    private fun setUpMonthText(calendar: Calendar?) {
        Log.i("OnCalendarSelect year->${calendar?.year} month->${calendar?.month} week->${calendar?.week} day->${calendar?.day} lunar->${calendar?.lunar}")

        //這目前沒用
        calendar.notNull { c ->
            binding.startDateText.text = getString(R.string.date_time_format)
                .messageFormat(c.year, c.month, c.day, weekStrArray[c.week])
            binding.endDateText.text = getString(R.string.date_time_format)
                .messageFormat(c.year, c.month, c.day, weekStrArray[c.week])
        }

    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onYearChange(year: Int) {
        Log.w("OnYearChange->$year")
    }

    override fun onMultiSelectOutOfSize(calendar: Calendar?, maxSize: Int) {

    }

    override fun onCalendarMultiSelectOutOfRange(calendar: Calendar?) {

    }

    override fun onCalendarMultiSelect(calendar: Calendar?, curSize: Int, maxSize: Int) {
        var isContain = selectDateTime.contain {
            return@contain it!! == calendar
        }
        var hasDup = selectDateTime.hasDuplicate {
            return@hasDuplicate it!! == calendar
        }
        Log.i("Calendar is Contain->$isContain hasDuplicate->$hasDup size->${selectDateTime.size}")
        calendar.notNull { c -> selectDateTime.addOrRemoveDate(c) }

        Log.w("size2->${selectDateTime.size}")

    }

    override fun initFragView() {
        toolBarTitle = getString(R.string.Select_Search_Time)

        monthStrArray = getStringArr(R.array.monthNumberArray)
        weekStrArray = getStringArr(R.array.chinese_week_string_array)

        ekiCalendar = EkiCalendar.loadMulti(binding.calendarView)
        ekiCalendar.addDateSelectListener(this)
        ekiCalendar.setMultiSelectListener(this)

        binding.calendarMonthBar.setCalendar(ekiCalendar)
        setUpMonthText(ekiCalendar.selectTime)

        setCalendarData()

        binding.startTimeText.setOnClickListener {
            var dialog = EkiTimePickerDialog()
            dialog.onTimeSelect { hourOfDay, minute ->
                Log.w("Pick select hour->$hourOfDay  select minute->$minute")
                selectDateTime.setStartTime(hourOfDay, minute)
                setDateText(binding.startTimeText, hourOfDay, minute)
            }
            dialog.show(childFragmentManager, dialog.TAG)
        }

        binding.endTimeText.setOnClickListener {
            var dialog = EkiTimePickerDialog()
            dialog.onTimeSelect { hourOfDay, minute ->
                Log.w("Pick select hour->$hourOfDay  select minute->$minute")
                selectDateTime.setEndTime(hourOfDay, minute)
                setDateText(binding.endTimeText, hourOfDay, minute)
                if (!selectDateTime.isValidEnd()) {
                    showToast(getString(R.string.End_time_must_be_greater_than_start_time))
                }
            }
            dialog.show(childFragmentManager, dialog.TAG)
        }

        binding.startSearchBtn.setOnClickListener {


            when (selectDateTime.checkTime()) {
                SearchDateTime.Check.DateError -> {
                    showToast(getString(R.string.Please_select_at_least_one_date))
                    return@setOnClickListener
                }
                SearchDateTime.Check.TimeError -> {
                    showToast(getString(R.string.End_time_must_be_greater_than_start_time))
                    return@setOnClickListener
                }
            }

            searchTime.setStart(selectDateTime.startTime)
            searchTime.setEnd(selectDateTime.endTime)
            searchTime.setDate(selectDateTime.map { c -> c.toDateTime().date })
            onSelectSearchTime?.invoke(searchTime)
        }
    }

    fun cleanSeach() {
        searchTime = LocationSearchTime()
        selectDateTime = SearchDateTime()
        ekiCalendar.calendarView?.clearMultiSelect()
        setCalendarData()
    }

    private fun setCalendarData() {

        //設定已經選的日期
        searchTime.dateList().map {
            Calendar().apply {
                year = it.year
                month = it.month
                day = it.day
            }
        }.forEach {
            ekiCalendar.calendarView?.putMultiSelect(it)
        }

        var start = searchTime.startTime()
        var end = searchTime.endTime()

        selectDateTime.setStartTime(start.hour, start.min)
        selectDateTime.setEndTime(end.hour, end.min)

        setDateText(binding.startTimeText, start.hour, start.min)
        setDateText(binding.endTimeText, end.hour, end.min)
    }


    private fun setDateText(timeText: TextView, hour24H: Int, minute: Int) {

        timeText.text = "${hour24H.mod02d()}:${minute.mod02d()}"
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragDateSearchBinding.inflate(inflater, container, false)
        return binding
    }

    override fun setData(data: LocationSearchTime?) {
        data.notNull {
            searchTime = it
            searchTime.dateList().map {
                Calendar().apply {
                    year = it.year
                    month = it.month
                    day = it.day
                }
            }.forEach { c ->
                selectDateTime.addOrRemoveDate(c)
            }
            selectDateTime.startTime = searchTime.startTime()
            selectDateTime.endTime = searchTime.endTime()
        }
    }

}