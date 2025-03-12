package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.VehicleInfo
import com.eki.parking.Model.ResponseVO

/**
 * Created by Hill on 2020/01/10
 */
class VehicleListResponse:ResponseVO(){

    var info=ArrayList<VehicleInfo>()

    override fun printValue() {
        super.printValue()
        printObj(this)
    }

//    data class Info(val Result:DeleteResult,val VehicleInfo:VehicleInfo)
//
//    data class DeleteResult(val Id:Int, val Success:Boolean)
}