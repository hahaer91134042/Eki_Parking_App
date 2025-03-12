package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO
import com.hill.devlibs.extension.printList

class OrderExtendTimeResponse: ResponseVO() {

    var info= ArrayList<ResponseInfo.OrderExtendTime>()

    override fun printValue() {
        super.printValue()
        info.printList()
    }

}