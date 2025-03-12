package com.eki.parking.Controller.listener

/**
 * Created by Hill on 03,10,2019
 */
interface CalendarSelectListener<CALENDAR>{
    fun onCalendarSelect(calendar: CALENDAR, isClick: Boolean)
    fun onCalendarOutOfRange(calendar: CALENDAR)
    fun onYearChange(year: Int)
}