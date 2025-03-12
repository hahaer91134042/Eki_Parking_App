package com.eki.parking.Model.DTO

import com.eki.parking.AppConfig
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.impl.ITimeConvert
import com.eki.parking.extension.parseEnum
import com.hill.devlibs.extension.toCalendar
import com.hill.devlibs.impl.IConvertData
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.daySpan

/**
 * Created by Hill on 25,09,2019
 */
class OpenSet : SqlVO<OpenSet>(),
                ITimeConvert,
                IConvertData<RequestBody.Open>{

    var Week:Int= -1
    var Date:String= ""
    var StartTime:String= ""
    var EndTime:String= ""

    var weekSet
        get() = parseEnum<WeekDay>(Week)
        set(value) {
            Week = value.value
        }

    override fun startDateTime(from: DateTime): DateTime = try {
        var d = when (weekSet == WeekDay.NONE) {
            true -> Date
            else -> {
//                Log.i("OpenSet startDateTime from->$from - ${(from.weekDay() - Week).daySpan} from week->${from.weekDay()} Week->$Week")
                (from - (from.weekDay() - Week).daySpan).date.toString()
            }
        }
//        Log.w("OpenSet startDateTime d->$d")
        DateTime("$d $StartTime".toCalendar(AppConfig.ServerSet.dateTimeFormat))
    } catch (e: Exception) {
        from
    }

    override fun endDateTime(from: DateTime): DateTime = try {
        var d = when (weekSet == WeekDay.NONE) {
            true -> Date
            else -> (from - (from.weekDay() - Week).daySpan).date.toString()
        }
        DateTime("$d $EndTime".toCalendar(AppConfig.ServerSet.dateTimeFormat))
    } catch (e: Exception) {
        from
    }


    override fun equals(other: Any?): Boolean {
        return if (other is OpenSet){
            Week==other.Week&&Date==other.Date&&
                    StartTime==other.StartTime&&EndTime==other.EndTime
        }else
            super.equals(other)
    }

    override fun clear() {

    }

    override fun printValue() {
        printObj(this)
    }

    override fun toData(): RequestBody.Open =
            RequestBody.Open().also {
                it.week=Week
                it.date=Date
                it.start=StartTime
                it.end=EndTime
            }



//    fun from(it: OpenInfo): OpenSet {
//        Week=it.Week
////        it.Date.notNull {d-> Date=d }
//        Date=it.Date
//        Start=it.StartTime
//        End=it.EndTime
//        return this
//    }
}