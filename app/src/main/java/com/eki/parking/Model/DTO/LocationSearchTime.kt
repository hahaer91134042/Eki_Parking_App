package com.eki.parking.Model.DTO


import com.eki.parking.AppConfig
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.extension.toTime
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.TimeUnit
import com.hill.devlibs.tools.Log
import java.io.Serializable

/**
 * Created by Hill on 16,10,2019
 */
class LocationSearchTime : Serializable {
    var date = ArrayList<String>()
    var start = "00:00:00"
    var end = "00:00:00"

    fun dateList(): List<DateUnit> = date.map { it.toDateTime(AppConfig.ServerSet.dateFormat).date }

    fun startTime(): TimeUnit = when {
        start.isNullOrEmpty() -> TimeUnit(0, 0, 0)
        else -> start.toDateTime(AppConfig.ServerSet.timeFormate).time
    }

    fun endTime(): TimeUnit = when {
        end.isNullOrEmpty() -> TimeUnit(0, 0, 0)
        else -> end.toDateTime(AppConfig.ServerSet.timeFormate).time
    }

    fun setDate(d: List<DateUnit>) {
        date = d.map { it.toString() }.toArrayList()
    }

    fun setStart(s: TimeUnit) {
        start = s.toString()
    }

    fun setEnd(e: TimeUnit) {
        end = e.toString()
    }

    fun isDefault(): Boolean {
//        Log.d("LocationSearchTime isDefault date->$date start->$start end->$end")

        return date.isEmpty() &&
                start.toTime().toStamp()==0L &&
                end.toTime().toStamp()==0L
    }


    override fun equals(other: Any?): Boolean {
        if (other is LocationSearchTime){
            if(date.size!=other.date.size)
                return false
            if (startTime()!=other.startTime()||endTime()!=other.endTime())
                return false
            return startTime()==other.startTime()&&
                    endTime()==other.endTime()&&
                    !dateList().any {n->!other.dateList().any {o->n==o }}
        }
        return super.equals(other)
    }
}