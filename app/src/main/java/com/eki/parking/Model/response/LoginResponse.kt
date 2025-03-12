package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO

/**
 * Created by Hill on 2019/6/20
 */

class LoginResponse : ResponseVO() {
    var info: ResponseInfo.LoginInfo?=null

    override fun printValue() {
        super.printValue()
        info?.printValue()
    }
}