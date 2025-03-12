package com.eki.parking.Model.response

import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.DTO.ResponseInfo
import com.hill.devlibs.extension.printContent

/**
 * Created by Hill on 31,10,2019
 */
class LoadReservaResponse: ResponseVO(){


    var info=ArrayList<ResponseInfo.LoadReserva>()


//    class InfoData: IConvertToSql<LocationReserva> {
//
//        override fun toSql(): LocationReserva =LocationReserva().also {
//            it.LocId=Id
//            it.LocNum=SerialNumber
//            it.OpenList.addAll(OpenSet)
////            OpenSet.forEach { f-> it.OpenList.add(OpenSet().from(f)) }
//
//            it.ReservaList.addAll(ReservaTime)
////            ReservaTime.forEach { r-> it.ReservaList.add(ReservaSet().from(r)) }
//        }
//
//        var Id=0
//        var SerialNumber=""
//        var OpenSet=ArrayList<OpenSet>()
//        var ReservaTime=ArrayList<ReservaSet>()
//
//    }


    override fun printValue() {
        super.printValue()
        printObj(this)
        info.forEach { printContent(it) }
    }
}