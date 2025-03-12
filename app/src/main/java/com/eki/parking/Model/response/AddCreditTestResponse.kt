package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.request.body.AddCreditBody

/**
 * Created by Hill on 2019/7/31
 */
class AddCreditTestResponse : ResponseVO() {

    var Request:CreditTestResponse?=null

    class CreditTestResponse{
        var key=""
        var content=""
        var decode: RequestBody.CreditCard?=null
    }
}