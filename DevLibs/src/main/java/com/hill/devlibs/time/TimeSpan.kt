package com.hill.devlibs.time

import androidx.annotation.IntRange
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.time.ext.msgFormat
import java.io.Serializable
import kotlin.time.*

@UseExperimental(ExperimentalTime::class)
data class TimeSpan(@IntRange(from = 0)val year:Int,
                    @IntRange(from = 0)val month:Int,
                    @IntRange(from = 0)val day:Int,
                    @IntRange(from = 0)val hour:Int,
                    @IntRange(from = 0)val min:Int,
                    @IntRange(from = 0)val sec:Int):Serializable{

    val totalDays:Double by lazy {  Duration.milliseconds(toMilliSec()).toDouble(DurationUnit.DAYS) }
    val totalHour:Double by lazy { Duration.milliseconds(toMilliSec()).toDouble(DurationUnit.HOURS) }
    val totalMinutes:Double by lazy { Duration.milliseconds(toMilliSec()).toDouble(DurationUnit.MINUTES) }
    val totalSec:Double by lazy { Duration.milliseconds(toMilliSec()).toDouble(DurationUnit.SECONDS) }


    operator fun plus(span:TimeSpan):TimeSpan{

        var thisTime=Duration.milliseconds(this.toMilliSec())
//        Log.d("this time->$thisTime stamp->${toMilliSec()}")
        var pTime=thisTime.plus(Duration.milliseconds(span.toMilliSec()))
        return TimeSpan(
                0,0,0,0,0,
                pTime.inSeconds.toInt()
        )
    }
    operator fun minus(span:TimeSpan):TimeSpan{
        var thisTime=Duration.milliseconds(this.toMilliSec())
        var minusTime=thisTime.minus(Duration.milliseconds(span.toMilliSec()))

        return TimeSpan(
                0,0,0,0,0,
                minusTime.inSeconds.toInt()
        )
    }

    operator fun div(span:TimeSpan):Double{
        return Duration.milliseconds(toMilliSec())/Duration.milliseconds(span.toMilliSec())
    }
    operator fun compareTo(span: TimeSpan): Int = when {
        toMilliSec() > span.toMilliSec() -> 1
        toMilliSec() == span.toMilliSec() -> 0
        else -> -1
    }

    override fun equals(other: Any?): Boolean {
        if (other is TimeSpan){
            return this.toMilliSec()==other.toMilliSec()
        }
        return super.equals(other)
    }


    //拿來做加減
    fun toMilliSec():Long{
//        Log.e("Time->${toString()}")
//        Log.w("hour millisec->${hour.hours.inMilliseconds}")
//        Log.i("min millisec->${min.minutes.inMilliseconds}")
//        Log.d("sec millisec->${sec.seconds.inMilliseconds}")

        return (Duration.days(day).inWholeMilliseconds+
                Duration.hours(hour).inWholeMilliseconds+
                Duration.minutes(min).inWholeMilliseconds+
                Duration.seconds(sec).inWholeMilliseconds)
    }



    override fun toString(): String =
        DateTime.dateTimeFormat.msgFormat(
            DateTime.dateFormat.msgFormat(year.toString(),month.mod02d(),day.mod02d()),
            DateTime.timeFormat.msgFormat(hour.mod02d(),min.mod02d(),sec.mod02d()))
}