package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO

/**
 * Created by Hill on 2019/11/26
 */
class OrderCheckoutResponse:ResponseVO(){

    var info=ResponseInfo.CheckOutUrl()


    override fun printValue() {
        super.printValue()
        printObj(info)
    }
}