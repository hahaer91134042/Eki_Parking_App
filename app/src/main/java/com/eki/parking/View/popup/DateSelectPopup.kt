package com.eki.parking.View.popup

import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import android.widget.Button
import com.eki.parking.Controller.builder.OpenTimeMarkerBuilder
import com.eki.parking.Controller.calendar.EkiCalendar
import com.eki.parking.Controller.listener.CalendarSelectListener
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.R
import com.eki.parking.View.abs.EkiPopupWindow
import com.eki.parking.View.widget.CalendarMonthBar
import com.eki.parking.extension.toJavaCalendar
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.util.SimpleAnimationUtils

/**
 * Created by Hill on 2020/05/14
 */
class DateSelectPopup(context: Context?,private var openSet:List<OpenSet>) : EkiPopupWindow(context) ,
CalendarSelectListener<Calendar?>{

    private var calendarView=contentView.findViewById<CalendarView>(R.id.calendarView)
    private var monthBar=contentView.findViewById<CalendarMonthBar>(R.id.calendarMonthBar)
    private var selectBtn=contentView.findViewById<Button>(R.id.selectBtn)

    private lateinit var ekiCalendar: EkiCalendar<CalendarView, Calendar?>

    private val now=DateTime()
    var selectTime=now

    var onSelectTime:(DateTime)->Unit={}



    override fun setUpView() {
        ekiCalendar= EkiCalendar.loadSimple(calendarView)
        ekiCalendar.onResume(calendarView)
        monthBar.setCalendar(ekiCalendar)

        var builder=OpenTimeMarkerBuilder(now,openSet)
        ekiCalendar.addCalendarMarker(builder.calendarMarker)
        ekiCalendar.addDateSelectListener(this)

        selectBtn.setOnClickListener {
            onSelectTime(selectTime)
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        monthBar.onPause()
        ekiCalendar.removeDateSelectListener(this)
    }
    override fun layoutRes(): Int = R.layout.popup_reserva_date_selector

    override fun popGravity(): Int =Gravity.BOTTOM
    override fun blurBackground(): Boolean =false
    override fun showAnim(): Animation? =SimpleAnimationUtils.bottomPopUp()
    override fun closeAnim(): Animation? =SimpleAnimationUtils.bottomDown()

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar.notNull { selectTime=DateTime(it.toJavaCalendar()) }
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onYearChange(year: Int) {

    }
}