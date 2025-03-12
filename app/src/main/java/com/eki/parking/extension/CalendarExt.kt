package com.eki.parking.extension

import com.eki.parking.AppConfig
import com.haibin.calendarview.Calendar
import com.hill.devlibs.extension.toTimeStr
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.util.StringUtil
import java.util.*

/**
 * Created by Hill on 03,10,2019
 */

fun Calendar.toJavaCalendar():java.util.Calendar{
    //java的calendar從0開始
    var c=java.util.Calendar.getInstance()
    c.set(this.year,this.month-1,this.day)
    return c
}
fun Calendar.toDateStr():String{
    return this.timeInMillis.toTimeStr(AppConfig.ServerSet.dateFormat)
}
fun Calendar.toDateTime():DateTime{
    return DateTime(this.toJavaCalendar())
}
