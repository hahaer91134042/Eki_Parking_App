package com.eki.parking.Model.DTO

import com.eki.parking.AppConfig
import com.eki.parking.Model.impl.ITimeConvert
import com.hill.devlibs.extension.toCalendar
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 25,09,2019
 */
class ReservaSet : SqlVO<ReservaSet>(),ITimeConvert {

//    var Week = -1
//    var Date = ""
    var StartTime = ""
    var EndTime = ""
    var Remark=""
    var IsUser=false


    override fun startDateTime(from:DateTime): DateTime {
//        var d=Date
//        Date.isNullOrEmpty {
//            d=(from-(from.weekDay()-Week).daySpan).date.toString()
//        }
        return DateTime(StartTime.toCalendar(AppConfig.ServerSet.dateTimeFormat))
    }

    override fun endDateTime(from:DateTime): DateTime {
//        var d=Date
//        Date.isNullOrEmpty {
//            d=(from-(from.weekDay()-Week).daySpan).date.toString()
//        }
        return DateTime(EndTime.toCalendar(AppConfig.ServerSet.dateTimeFormat))
    }

    override fun clear() {

    }

    override fun printValue() {
        printObj(this)
    }

//    fun from(it: Reserva): ReservaSet {
////        Week=it.Week
////        Date=it.Date
//        StartTime=it.StartTime
//        EndTime=it.EndTime
//        Remark=it.Remark
//        IsUser=it.IsUser
//        return this
//    }
}