package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.eki.parking.R
import com.eki.parking.Controller.calendar.EkiCalendar
import com.eki.parking.extension.color
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.stringArray
import com.eki.parking.Controller.listener.CalendarSelectListener
import com.eki.parking.View.abs.RelativeCustomView
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hill.devlibs.extension.boldText
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.widget.AutoSizeTextView

/**
 * Created by Hill on 31,10,2019
 */
class CalendarMonthBar(
    context: Context?,
    attrs: AttributeSet?
) : RelativeCustomView(context, attrs),
    CalendarSelectListener<Calendar?> {

    init {

    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar.notNull { setUpMonthText(it) }

    }

    private fun setUpMonthText(calendar: Calendar) {
//        Log.i("OnCalendarSelect year->${calendar?.year} month->${calendar?.month} week->${calendar?.week} day->${calendar?.day} lunar->${calendar?.lunar}")

        val positionPrevious = if (calendar.month - 2 < 0) {
            11
        } else {
            calendar.month - 2
        }
        val preMonthText = monthStrArray[positionPrevious]
        prevMonth.text = preMonthText

        val nowPosition = if (calendar.month - 1 < 0) {
            0
        } else {
            calendar.month - 1
        }
        val nowMonthText = "${calendar.year} ${monthStrArray[nowPosition]}"
        nowMonth.text = nowMonthText

        val positionNext = if (calendar.month > 11) {
            0
        } else {
            calendar.month
        }
        val nextMonthText = monthStrArray[positionNext]
        nextMonth.text = nextMonthText
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onYearChange(year: Int) {

    }

    private val monthStrArray = stringArray(R.array.monthNumberArray)

    var prevMonth = AutoSizeTextView(context).also {
        it.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, dpToPx(30f)).apply {
            addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            addRule(RelativeLayout.CENTER_VERTICAL)
            leftMargin = dpToPx(16f)
        }
        it.setTextColor(color(R.color.light_gray))
//        it.textSize= dimen(R.dimen.text_size_24px)
        it.gravity = Gravity.CENTER_VERTICAL
        it.boldText()
        addView(it)
    }
    var nowMonth = AutoSizeTextView(context).also {
        it.layoutParams = LayoutParams(dpToPx(100f), LayoutParams.MATCH_PARENT).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }
        it.setTextColor(color(R.color.text_color_1))
//        it.textSize= dimen(R.dimen.text_size_36px)
        it.gravity = Gravity.CENTER
        it.boldText()
        addView(it)
    }
    var nextMonth = AutoSizeTextView(context).also {
        it.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, dpToPx(30f)).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            addRule(RelativeLayout.CENTER_VERTICAL)
            rightMargin = dpToPx(16f)
        }
        it.setTextColor(color(R.color.light_gray))
//        it.textSize= dimen(R.dimen.text_size_24px)
        it.gravity = Gravity.CENTER_VERTICAL
        it.boldText()
        addView(it)
    }

    private var calendarView: EkiCalendar<CalendarView, Calendar?>? = null

    fun setCalendar(calendar: EkiCalendar<CalendarView, Calendar?>) {
        calendarView = calendar
        calendarView?.selectTime.notNull {
            setUpMonthText(it)
        }

        calendar.addDateSelectListener(this)
        prevMonth.setOnClickListener {
            calendarView?.scrollToPre()
        }
        nowMonth.setOnClickListener {
            calendarView?.scrollToNow()
        }
        nextMonth.setOnClickListener {
            calendarView?.scrollToNext()
        }
    }

    fun onPause() {
        calendarView?.removeDateSelectListener(this)
    }


    override fun setStyleableRes(): IntArray? {
        return super.setStyleableRes()
    }

    override fun parseTypedArray(typedArray: TypedArray) {
        super.parseTypedArray(typedArray)
    }

    override fun setInflatView(): Int = 0

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null
}