package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO

class CheckChargeResponse: ResponseVO(){
    val info: List<ResponseInfo.CheckCharge>? = null
}

class StartChargeResponse: ResponseVO() {
    val info: List<ResponseInfo.StartCharge>? = null
}