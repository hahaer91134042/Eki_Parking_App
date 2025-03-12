package com.eki.parking.Controller.listener

/**
 * Created by Hill on 03,10,2019
 */
interface CalendarMultiSelectListener<DATA> {
    fun onMultiSelectOutOfSize(calendar: DATA, maxSize: Int)
    fun onCalendarMultiSelectOutOfRange(calendar: DATA)
    fun onCalendarMultiSelect(calendar: DATA, curSize: Int, maxSize: Int)
}