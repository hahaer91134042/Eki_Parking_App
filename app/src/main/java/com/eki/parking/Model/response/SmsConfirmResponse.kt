package com.eki.parking.Model.response

import com.eki.parking.Model.ResponseVO
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2019/6/25
 */
class SmsConfirmResponse :ResponseVO(){

    var checkCode="0"

    override fun printValue() {
        super.printValue()
        Log.i("checkCode->$checkCode")
    }
}