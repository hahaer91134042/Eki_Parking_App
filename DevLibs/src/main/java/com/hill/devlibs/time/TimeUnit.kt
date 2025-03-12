package com.hill.devlibs.time

import androidx.annotation.IntRange
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.impl.IExtractTime
import com.hill.devlibs.time.ext.msgFormat
import java.io.Serializable
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * Created by Hill on 18,10,2019
 */
@UseExperimental(ExperimentalTime::class)
data class TimeUnit(@IntRange(from = 0,to = 24) val hour:Int,
                    @IntRange(from = 0,to = 60) val min:Int,
                    @IntRange(from = 0,to = 60) val sec:Int):IExtractTime,Serializable {


    fun refresh():TimeUnit{
        Duration.milliseconds(this.toStamp()).toComponents { days, hours, minutes, seconds, nanoseconds ->
            return TimeUnit(
                    hours,
                    minutes,
                    seconds).also { it.day=days.toInt() }
        }
    }

    override fun time(): TimeUnit =this

    //這只能用來單獨計算時間用
    @IntRange(from = 0)var day:Int=0

    operator fun plus(time1:TimeUnit):TimeSpan{

        var d1=Duration.milliseconds(toStamp())
        var d2=Duration.milliseconds(time1.toStamp())
//        Log.w("d1=${d1}  d2=$d2")
        var totalDay:Duration=d1+d2
//        Log.i("totalDay=${totalDay.inDays}")
        totalDay.toComponents { days, hours, minutes, seconds, nanoseconds ->
            return TimeSpan(0,0,days.toInt(), hours, minutes, seconds)
        }
    }
    operator fun minus(time1:TimeUnit):TimeSpan{
//        Log.d("time minus this->${this.toStringWithDay()} other->${time1.toStringWithDay()}")
        var d1=Duration.milliseconds(toStamp())
        var d2=Duration.milliseconds(time1.toStamp())
//        Log.w("d1=${d1}  d2=$d2")
        var totalDay:Duration=d1-d2
//        Log.i("totalDay=${totalDay.inDays}")
        totalDay.toComponents { days, hours, minutes, seconds, nanoseconds ->
            return TimeSpan(0,0,days.toInt(), hours, minutes, seconds)
        }
    }

    operator fun plus(time1:TimeSpan):TimeUnit{

        var d1=Duration.milliseconds(toStamp())
        var d2=Duration.milliseconds(time1.toMilliSec())
//        Log.w("d1=${d1}  d2=$d2")

        var totalDay:Duration=d1+d2
//        Log.i("totalDay=${totalDay.inDays}")
        totalDay.toComponents { days, hours, minutes, seconds, nanoseconds ->
            return TimeUnit(
                hours,
                minutes,
                seconds).also { it.day=days.toInt() }
        }
    }
    operator fun minus(time1:TimeSpan):TimeUnit{

        var d1=Duration.milliseconds(toStamp())
        var d2=Duration.milliseconds(time1.toMilliSec())
//        Log.w("d1=${d1}  d2=$d2")
        var totalDay:Duration=d1-d2
//        Log.i("totalDay=${totalDay}")
        totalDay.toComponents { days, hours, minutes, seconds, nanoseconds ->
            return TimeUnit(
                hours,
                minutes,
                seconds).also { it.day=days.toInt() }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is TimeUnit){
            return this.toStamp()==other.toStamp()
        }
        return super.equals(other)
    }
    operator fun compareTo(other: TimeUnit): Int {
        var thisTime = toStamp()
        var otherTime = other.toStamp()
        return when {
            thisTime > otherTime -> 1
            thisTime == otherTime -> 0
            else -> -1
        }
    }

    override fun toString(): String {
        return DateTime.timeFormat.msgFormat(hour.mod02d(),min.mod02d(),sec.mod02d())
    }
    fun toStringWithDay():String{
        return "day:$day ${toString()}"
    }

    fun toStamp():Long{
//        Log.e("Time->${toString()}")
//        Log.w("hour millisec->${hour.hours.inMilliseconds}")
//        Log.i("min millisec->${min.minutes.inMilliseconds}")
//        Log.d("sec millisec->${sec.seconds.inMilliseconds}")

        return (Duration.days(day).inWholeMilliseconds+
                Duration.hours(hour).inWholeMilliseconds+
                Duration.minutes(min).inWholeMilliseconds+
                Duration.seconds(sec).inWholeMilliseconds)
    }
}