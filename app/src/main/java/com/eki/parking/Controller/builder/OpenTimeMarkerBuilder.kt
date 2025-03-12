package com.eki.parking.Controller.builder

import com.eki.parking.AppConfig
import com.eki.parking.Controller.calendar.marker.SimpleMarker
import com.eki.parking.Controller.impl.ICalendarMarker
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.R
import com.eki.parking.extension.color
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.daySpan

/**
 * Created by Hill on 2020/05/14
 */
open class OpenTimeMarkerBuilder(start: DateTime, setList: List<OpenSet>) {

//    val tag="OpenTimeMarkerBuilder"

    var timeSort = LinkedHashMap<DateTime, List<OpenSet>>()

    val calendarMarker: List<ICalendarMarker>
        get() = ArrayList<ICalendarMarker>().also { list ->
            timeSort.forEach {
                var now = it.key
                if (it.value.isNotEmpty())
                    list.add(SimpleMarker(now, color(R.color.Eki_green_3)))
            }
        }


    init {
        var dateStart=DateTime(start.year,start.month,1,0,0,0)

        var end = (start + AppConfig.maxReservaDay.daySpan).let {
            DateTime(it.year,it.month,it.monthMaxDay,0,0,0)
        }
//        Log.w(" end->$end")

        while (end>=dateStart){

//            Log.i("$tag dateStart->$dateStart")
            //這邊把週期性日期還原好了
            timeSort[dateStart] = setList
                    .filter {
//                        Log.w("timeSort filter dateStart date->${dateStart.date}")
//                        Log.i("timeSort filter start date->${it.startDateTime(dateStart).date}")
                        it.startDateTime(dateStart).date == dateStart.date
                    }
                    .let { rawOpenList ->
                        ArrayList<OpenSet>().apply {
                            rawOpenList.forEach { rawOpen ->
                                add(when (rawOpen.weekSet) {
                                    WeekDay.NONE -> rawOpen
                                    else -> OpenSet().also { set ->
                                        set.Date = dateStart.date.toString()
                                        set.StartTime = rawOpen.StartTime
                                        set.EndTime = rawOpen.EndTime
                                    }
                                })
                            }
                        }
                    }

            dateStart += 1.daySpan
        }

//        Log.i("$tag start to end->$dateStart")

//        for (month in start.month..end.month) {
//            var monthStart = DateTime(start.year, month, 1, 0, 0, 0)
//            Log.d("$tag monthStart->$monthStart")
//            var monthMaxDay = monthStart.monthMaxDay
//            Log.d("$tag monthMaxDay->$monthMaxDay")
//
//            for (dayOffset in 0 until monthMaxDay) {
//                var time = monthStart + dayOffset.daySpan
//                Log.i("$tag time->$time")
//                //這邊把週期性日期還原好了
//                timeSort[time] = setList.filter { it.startDateTime(time).date == time.date }
//                        .let { rawOpenList ->
//                            ArrayList<OpenSet>().apply {
//                                rawOpenList.forEach { rawOpen ->
//                                    add(when (rawOpen.weekSet) {
//                                        WeekSet.NONE -> rawOpen
//                                        else -> OpenSet().also { set ->
//                                            set.Date = time.date.toString()
//                                            set.StartTime = rawOpen.StartTime
//                                            set.EndTime = rawOpen.EndTime
//                                        }
//                                    })
//                                }
//                            }
//                        }
//            }
//        }
    }
}