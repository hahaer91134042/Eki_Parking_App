package com.hill.devlibs.time.ext

import com.hill.devlibs.util.TimeUtil
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.toTimeStr
import com.hill.devlibs.impl.IExtractDate
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.TimeSpan
import com.hill.devlibs.time.TimeUnit
import com.hill.devlibs.tools.Log
import java.text.MessageFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun Long.toDateTime():DateTime=DateTime(this)
fun DateUnit.monthEqual(other:DateUnit):Boolean=this.removeDay()==other.removeDay()
fun DateUnit.removeDay():DateUnit= DateUnit(year,month,1)
fun DateTime.shortDate():String=date.formateShort()
fun DateTime.shortTime():String=time.formateShort()
fun TimeUnit.formateShort():String{
    return "{0}:{1}".msgFormat(hour.mod02d(),min.mod02d())
}
fun TimeUnit.formateLong():String{
    return "{0}:{1}:{2}".msgFormat(hour.mod02d(),min.mod02d(),sec.mod02d())
}
fun DateTime.format(format:String?=null):String{
    return toStamp().toTimeStr(format)
}
fun DateTime.formatNoSec():String{
    return "{0}-{1}-{2} {3}:{4}".messageFormat(date.year.toString(),date.month.mod02d(),day.mod02d(),hour.mod02d(),min.mod02d())
}
fun DateUnit.formateShort():String="{0}/{1}".messageFormat(month.mod02d(),day.mod02d())
fun DateUnit.format(format:String?=null):String{
    return toStamp().toTimeStr(format)
}
fun DateTime.toCalendar():Calendar= Calendar.getInstance().apply {
    set(year,month-1,day,hour,min,sec)
    set(Calendar.MILLISECOND,0)
}
fun DateUnit.toCalendar():Calendar= Calendar.getInstance().apply {
    set(year,month-1,day,0,0,0)
    set(Calendar.MILLISECOND,0)
}

fun Calendar.addYear(year:Int): Calendar {
    this.add(Calendar.YEAR,year)
    return this
}
fun Calendar.addMonth(month:Int): Calendar {
    this.add(Calendar.MONTH,month)
    return this
}
fun Calendar.addDay(day:Int): Calendar {
    this.add(Calendar.DAY_OF_MONTH,day)
    return this
}
fun Calendar.addHour(hour:Int): Calendar {
    this.add(Calendar.HOUR_OF_DAY,hour)
    return this
}
fun Calendar.addMin(min:Int): Calendar {
    this.add(Calendar.MINUTE,min)
    return this
}
fun Calendar.addSec(sec:Int): Calendar {
    this.add(Calendar.SECOND,sec)
    return this
}
fun Calendar.getYear():Int{
    return this.get(Calendar.YEAR)
}

fun Calendar.getMonth():Int{
    return this.get(Calendar.MONTH)+1
}
fun Calendar.getMonthMaxDay():Int{
    return getActualMaximum(Calendar.DAY_OF_MONTH)
}
fun Calendar.getWeek():Int{
    return get(Calendar.WEEK_OF_MONTH)
}
fun Calendar.getWeekDay():Int =get(Calendar.DAY_OF_WEEK)-1
fun Calendar.getDay():Int{
    return this.get(Calendar.DAY_OF_MONTH)
}
fun Calendar.getDayOfYear():Int=this.get(Calendar.DAY_OF_YEAR)
fun Calendar.getDayOfWeekInMonth():Int=get(Calendar.DAY_OF_WEEK_IN_MONTH)
fun Calendar.getHour24():Int{
    return this.get(Calendar.HOUR_OF_DAY)
}
fun Calendar.formatString(format:String= TimeUtil.dateTimeFormat):String{
    return SimpleDateFormat(format).format(time)
}
fun Calendar.getHour12():Int{
    return this.get(Calendar.HOUR)
}
fun Calendar.getMin():Int{
    return this.get(Calendar.MINUTE)
}
fun Calendar.getSec():Int{
    return this.get(Calendar.SECOND)
}
inline fun String.msgFormat(vararg objs:Any):String{
    return MessageFormat.format(this, *objs)
}

val Int.secSpan get() = TimeSpan(0,0,0,0,0,this)
val Int.minSpan get() = TimeSpan(0,0,0,0,this,0)
val Int.hourSpan get() = TimeSpan(0,0,0,this,0,0)
val Int.daySpan get() = TimeSpan(0,0,this,0,0,0)
val Int.monthSpan get()=TimeSpan(0,this,0,0,0,0)
val Int.yearSpan get() = TimeSpan(this,0,0,0,0,0)


val IExtractDate.monthMaxDay:Int get() = Calendar.getInstance()
        .apply { set(date().year,date().month-1,date().day) }.getMonthMaxDay()
val IExtractDate.weekDay:Int get() = Calendar.getInstance()
        .apply { set(date().year,date().month-1,date().day) }.getWeekDay()

val IExtractDate.monthStartDate:DateUnit get()=DateUnit(date().year,date().month,1)
val IExtractDate.monthEndDate:DateUnit get() = DateUnit(date().year,date().month,date().monthMaxDay)

val IExtractDate.weekNumInMonth:Int get() {
    var weekNum=0
    var weekDay=date().weekDay
    var start=DateTime().set(DateUnit(date().year, date().month,1))
    for (dayOffset in 0 until start.date.monthMaxDay){
        var day=start+dayOffset.daySpan
        if (day.weekDay()==weekDay)
            weekNum += 1
    }
    return weekNum
}

val IExtractDate.sameWeekDay:List<DateTime> get() {
    var list=ArrayList<DateTime>()
    var weekDay=date().weekDay
    var start=DateUnit(date().year, date().month,1)
//    Log.e("start->$start  month->${Calendar.getInstance()
//            .apply { set(start.year,start.month-1,start.day) }.getMonth()}")
//    Log.d("MaxDay->${start.monthMaxDay}")
    for (dayOffset in 0 until start.monthMaxDay){
        var day=start+dayOffset.daySpan
        Log.i("sameWeekDay start->$start  offset->$dayOffset day->$day")
        if (day.weekDay==weekDay)
            list.add(DateTime(day))
    }
    return list
}
//inline fun DateTime.toJodaTime():org.joda.time.DateTime{
//    return org.joda.time.DateTime(
//        this.date.year,
//        this.date.month,
//        this.date.day,
//        this.time.hour,
//        this.time.min,
//        this.time.sec
//    )
//}