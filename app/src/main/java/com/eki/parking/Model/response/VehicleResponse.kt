package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.VehicleInfo
import com.eki.parking.Model.ResponseVO

/**
 * Created by Hill on 2019/12/27
 */
class VehicleResponse:ResponseVO(){

    var info:VehicleInfo=VehicleInfo()

    override fun printValue() {
        super.printValue()
        printObj(this)
    }
}