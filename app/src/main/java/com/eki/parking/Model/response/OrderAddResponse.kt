package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO
import com.hill.devlibs.extension.printContent

/**
 * Created by Hill on 06,11,2019
 */

class OrderAddResponse: ResponseVO() {

    var info=ArrayList<ResponseInfo.OrderAdd>()

//    class InfoData{
//        var Time=OrderReservaTime()
//        var Order: ResponseInfo.OrderResult?=null
//        var LoadData:LoadReservaResponse.InfoData?=null
//        var Success=false
//    }

    override fun printValue() {
        super.printValue()
        info.forEach {
            printContent(it.Time)
            it.Order?.let { it1 -> printContent(it1) }
            it.LoadData?.let { it1 -> it1.toSql().printValue() }
            printContent(it.Success)
        }
    }
}