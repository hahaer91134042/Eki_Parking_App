package com.hill.devlibs.time

import android.content.Context
import androidx.annotation.IntRange
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toCalendar
import com.hill.devlibs.impl.IExtractDate
import com.hill.devlibs.impl.IExtractTime
import com.hill.devlibs.time.ext.*
import com.hill.devlibs.tools.Log
import net.danlew.android.joda.JodaTimeAndroid
import java.io.Serializable
import java.time.DayOfWeek
import java.util.*

/**
 * Created by Hill on 18,10,2019
 */

class DateTime():IExtractDate,IExtractTime,Serializable{
    companion object{
        @JvmStatic
        fun now():DateTime = DateTime()

        @JvmStatic
        fun initFrom(context:Context?){
            JodaTimeAndroid.init(context)
        }

        @JvmStatic
        fun parse(input:String,format:String?=null):DateTime{
            format.notNull { return DateTime(input.toCalendar(format)) }
            return DateTime(input.toCalendar())
        }

        const val dateFormat="{0}-{1}-{2}"
        const val timeFormat="{0}:{1}:{2}"
        const val dateTimeFormat="{0} {1}"
    }

    var calendar=Calendar.getInstance().also { it.set(Calendar.MILLISECOND,0) }
    val date:DateUnit by lazy {
        DateUnit(calendar.getYear(),calendar.getMonth(),calendar.getDay())
    }
    val time:TimeUnit by lazy {
        TimeUnit(calendar.getHour24(),calendar.getMin(),calendar.getSec())
    }
    val year:Int by lazy { date.year }
    val month:Int by lazy { date.month }
    val day:Int by lazy { date.day }
    val hour:Int by lazy { time.hour }
    val min:Int by lazy { time.min }
    val sec:Int by lazy { time.sec }
    val monthMaxDay:Int by lazy { calendar.getMonthMaxDay() }
    val weekDay:Int=calendar.getWeekDay()


    constructor(year:Int, @IntRange(from = 1) month:Int, @IntRange(from = 1) day:Int, hour:Int, min:Int, sec:Int):this(){
        calendar= Calendar.getInstance().apply {
            set(year,month-1,day,hour,min,sec)
            set(Calendar.MILLISECOND,0)
        }
    }
    constructor(date:DateUnit):this(date.year,date.month,date.day,0,0,0)
    constructor(date: DateUnit,time: TimeUnit):this(date.year,date.month,date.day,time.hour,time.min,time.sec)
    constructor(c: Calendar):this(){
        calendar=c
    }
    constructor(stamp:Long):this(){
        var c=org.joda.time.DateTime(stamp).withMillisOfSecond(0)
        calendar=c.toCalendar(null)
    }


    fun set(date:DateUnit,time:TimeUnit):DateTime{
        calendar.set(date.year,date.month-1,date.day,
            time.hour,time.min,time.sec)
        calendar.set(Calendar.MILLISECOND,0)
        return DateTime(calendar)
    }

    fun set(date:DateUnit):DateTime{
        return set(date, TimeUnit(0,0,0))
    }

    /**
     * @return 一個月內第幾周
     */
    fun week():Int=calendar.getWeek()

    /**
     * @return 日:0 ~ 六:6
     */
    fun weekDay():Int=calendar.getWeekDay()

    operator fun plus(time1:DateTime):TimeSpan{
        var pDate=this.date+time1.date
        var pTime=this.time+time1.time

        return TimeSpan(pDate.year,pDate.month,pDate.day+pTime.day,pTime.hour,pTime.min,pTime.sec)
    }
    operator fun minus(time1:DateTime):TimeSpan{
        var pDate=this.date-time1.date
//        Log.e("minusDateTime this ->${this} other->${time1}")
//        Log.d("minusDateTime pDate->${pDate}")
        var thisTime=this.time+pDate//這是加上日期差距的天數
//        var thisTime=TimeUnit(this.time.hour,this.time.hour,this.time.sec).apply { day=pDate.day}
        var otherTime=TimeUnit(time1.hour,time1.min,time1.sec)
//        Log.w("minusDateTime thisTime->${thisTime.toStringWithDay()} otherTime->${otherTime.toStringWithDay()}")
        var pTime=thisTime-otherTime
//        Log.i("minusDateTime pTime->${pTime}")

//        var result=TimeSpan(pDate.year,pDate.month,pTime.day,pTime.hour,pTime.min,pTime.sec)
//        Log.d("minusDateTime result->${result}")
//        return result
        return TimeSpan(pDate.year,pDate.month,pTime.day,pTime.hour,pTime.min,pTime.sec)
    }

    operator fun plus(time1:TimeSpan):DateTime{

//        Log.d("this-> $this stamp->${toStamp()}")
//        Log.i("plus input-> ${time1}")


        var thisTime=org.joda.time.DateTime(toStamp())
                .withMillisOfSecond(0)
        var jDate=thisTime
            .plusYears(time1.year)
            .plusMonths(time1.month)
            .plusDays(time1.day)
            .plusHours(time1.hour)
            .plusMinutes(time1.min)
            .plusSeconds(time1.sec)

        //        Log.d("joda plus->$jDate stamp->${jDate.millis}")
//        Log.w("plus $this stamp->${toMilliSec()}")
//        Log.w("DateTime plus this->${thisTime} other->${time1} result->${result}")

//        return DateTime(calendar)
        return DateTime(jDate.toCalendar(Locale.getDefault()))
    }
    operator fun minus(time1:TimeSpan):DateTime{
//        Log.d("this-> $this stamp->${toStamp()}")
//        Log.i("minus input-> ${time1} ")

        var thisTime=org.joda.time.DateTime(toStamp())
                .withMillisOfSecond(0)

        var jDate=thisTime
            .minusYears(time1.year)
            .minusMonths(time1.month)
            .minusDays(time1.day)
            .minusHours(time1.hour)
            .minusMinutes(time1.min)
            .minusSeconds(time1.sec)

//        Log.e("joda this->$thisTime stamp->${thisTime.millis} datetime->${
//        DateTime(jDate.toCalendar(Locale.getDefault()))}")
//        Log.d("joda minus->$jDate stamp->${jDate.millis}")

        return DateTime(jDate.toCalendar(Locale.getDefault()))
    }

    override fun equals(other: Any?): Boolean {
//        Log.w("Compare equals other->$other")
        if (other is DateTime){
            return this.toStamp()==other.toStamp()
        }
        return super.equals(other)
    }
    operator fun compareTo(time1:DateTime):Int{
        var thisTime=org.joda.time.DateTime(toStamp()).withMillisOfSecond(0)
        var otherTime=org.joda.time.DateTime(time1.toStamp()).withMillisOfSecond(0)
        return when {
            thisTime.millis>otherTime.millis -> 1
            thisTime.millis==otherTime.millis -> 0
            else -> -1
        }
//        Log.d("Compare this->$thisTime other->$otherTime ")
//        Log.w("Compare stamp->${thisTime.millis} other->${otherTime.millis}")
//        Log.i("Compare result->$result")
//        return result
    }

    override fun date(): DateUnit =date
    override fun time(): TimeUnit =time

    override fun toString(): String {
        return dateTimeFormat.msgFormat(date.toString(),time.toString())
    }

    fun toStamp():Long=calendar.timeInMillis
}