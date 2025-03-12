package com.eki.parking.extension

import com.eki.parking.AppConfig
import com.eki.parking.Model.DTO.ReservaSet
import com.eki.parking.Model.DTO.TimeRange
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.impl.ITimeConvert
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.impl.IExtractDate
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.TimeSpan
import com.hill.devlibs.time.TimeUnit
import com.hill.devlibs.time.ext.minSpan
import com.hill.devlibs.time.ext.msgFormat
import com.hill.devlibs.time.ext.secSpan
import com.hill.devlibs.time.ext.weekDay

/**
 * Created by Hill on 2019/11/22
 */
val Int.asWeekStr get() = string(this.toEnum<WeekDay>().strRes)

val ITimeConvert.timeSpan: TimeSpan get() = endDateTime() - startDateTime()

inline fun ITimeConvert.isBetween(time:DateTime):Boolean{
    return startDateTime()<=time && time<endDateTime()
}

inline fun ITimeConvert.isOverLap(other:ITimeConvert):Boolean {
    return this.toRange().isOverLap(other.toRange())
}

inline fun ITimeConvert.toRange():TimeRange {
    return TimeRange(startDateTime(),endDateTime())
}

fun IExtractDate.formatFull(format:String="{0}/{1}/{2}({3})"):String{
    var date=date()
    return format.msgFormat(date.year.toString(),date.month.mod02d(),date.day.mod02d(), string(date.weekDay.toEnum<WeekDay>().strRes))
}
fun DateTime.standarByTimeOffset():DateTime{
    var from=this
    var formatTimeList=ArrayList<DateTime>()

    var start=DateTime().set(from.date, TimeUnit(from.time.hour,0,0))
    var end=DateTime().set(from.date,TimeUnit(from.time.hour,60,0))
    do {
        formatTimeList.add(start)
        start+=AppConfig.ReservaTimeOffsetMin.minSpan
    }while (start<=end)

    for (offset in formatTimeList){
//        Log.i("standarByTimeOffset from->$from offset->$offset")
        if (from<=offset){
            return offset
        }
    }
    return from
}

fun DateTime.standarToCheckOut(reserva: ReservaSet):DateTime{
    var checkoutTime=this
    var rStart=reserva.startDateTime()
    var rEnd=reserva.endDateTime()

    //假如結帳時間小時最小時間(30MIN)結帳時間以30MIN計算
    if ((checkoutTime-rStart).totalMinutes<=AppConfig.ReservaGapMin.toDouble())
        return rStart+AppConfig.ReservaGapMin.minSpan

    //假如結帳時間超出預約結束時間 但是小於10分鐘緩衝時間 以正常結帳時間計算
    if(checkoutTime<(rEnd+AppConfig.FreeCheckoutMinute.minSpan)){
        var freeSpan=(checkoutTime-rStart).totalSec.toInt() % (AppConfig.ReservaTimeOffsetMin*60)

        if (freeSpan<=AppConfig.FreeCheckoutMinute*60)
            checkoutTime -= freeSpan.secSpan
    }



//    Log.w("after freeSpan checkout->$checkoutTime")
//    var formatTimeList=ArrayList<DateTime>()
////    var nowMin=TimeUnit(0,time.min,0)
////    Log.i("unformat now->$this")
//    var start=DateTime().set(checkoutTime.date, TimeUnit(checkoutTime.time.hour,0,0))
//    var end=DateTime().set(checkoutTime.date,TimeUnit(checkoutTime.time.hour,60,0))
//    do {
//        formatTimeList.add(start)
//        start+=AppConfig.ReservaTimeOffsetMin.minSpan
//    }while (start<=end)
////    Log.i("formatCheckOut list->$formatTimeList")
//
//    for (offset in formatTimeList){
//        if (checkoutTime<=offset){
//            return DateTime().set(offset.date,offset.time)
//        }
//    }

//    Log.w("format now->$nowMin")
//    var result=this+

    //    Log.w("standarToCheckOut start->$rStart end->$rEnd checkout->$checkoutTime  result->$result")
    return checkoutTime.standarByTimeOffset()
}