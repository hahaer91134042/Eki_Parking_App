package com.hill.devlibs.time

import androidx.annotation.IntRange
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.IExtractDate
import com.hill.devlibs.time.ext.*
import com.hill.devlibs.tools.Log
import com.kizitonwose.time.milliseconds
import java.io.Serializable

/**
 * Created by Hill on 18,10,2019
 */
data class DateUnit(val year:Int,
                    @IntRange(from = 1) val month:Int,
                    @IntRange(from = 1) val day:Int):IExtractDate,Serializable{

    override fun date(): DateUnit =this

    operator fun plus(date1:DateUnit):TimeSpan{
        var thisTime=this.toStamp().milliseconds.inDays
        var pDate=thisTime.plus(date1.toStamp().milliseconds.inDays)
        return TimeSpan(0,0,pDate.value.toInt(),0,0,0)
    }
    operator fun minus(date1:DateUnit):TimeSpan{
        var thisTime=this.toStamp().milliseconds
        var other=date1.toStamp().milliseconds

        var mDate=thisTime.minus(other).inDays
//        Log.w("DateUnit this->$this other->$date1")
//        Log.e("DateUnit minus this->${thisTime}  minus->$other result->${mDate.value}")
        return TimeSpan(0,0,mDate.value.toInt(),0,0,0)
    }

    operator fun plus(date1:TimeSpan):DateUnit{
//        var thisTime=this.toCalendar()

//        Log.w("this time->$thisTime")
        var pDate=this.toCalendar().apply {
            addYear(date1.year)
            addMonth(date1.month)
            addDay(date1.day)
        }
        return DateUnit(pDate.getYear(),pDate.getMonth(),pDate.getDay())
    }

    operator fun minus(date1: TimeSpan):DateUnit{
//        var thisTime=org.joda.time.DateTime(toStamp())
//        Log.w("this time->$thisTime")
        var pDate=this.toCalendar().apply {
            addYear(-date1.year)
            addMonth(-date1.month)
            addDay(-date1.day)
        }
        return DateUnit(pDate.getYear(),pDate.getMonth(),pDate.getDay())
    }

    override fun equals(other: Any?): Boolean {
        if (other is DateUnit){
            return this.toStamp()==other.toStamp()
        }
        return super.equals(other)
    }

    operator fun compareTo(other:DateUnit):Int{
        var thisTime=this.toStamp()
        var otherTime=other.toStamp()
//        Log.i("Date Compareto this->$this  ${thisTime.millis} other->$time1  ${otherTime.millis}")
        return when {
            thisTime>otherTime -> 1
            thisTime==otherTime -> 0
            else -> -1
        }
    }

    override fun toString(): String {
        return DateTime.dateFormat.msgFormat(year.toString(),month.mod02d(),day.mod02d())
    }
//    fun toStamp():Long= GregorianCalendar().let {
//        it.set(year,month-1,day,0,0,0)
//        it.set(Calendar.MILLISECOND,0)//重要不然比對時間會錯
//        it.timeInMillis
//    }
    fun toStamp():Long= this.toCalendar().timeInMillis
}