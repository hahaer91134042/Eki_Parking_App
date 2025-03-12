package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO
import com.hill.devlibs.tools.ContentPrinter

/**
 * Created by Hill on 2019/6/20
 */
class RegisterResponse :ResponseVO(){
    var info:ResponseInfo.RegisterToken?=null


    override fun printValue() {
        super.printValue()
        printContentValue(ContentPrinter()
                .setKeys("token")
                .setValues(info?.token))
    }
}