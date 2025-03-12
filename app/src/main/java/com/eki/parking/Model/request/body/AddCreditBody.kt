package com.eki.parking.Model.request.body

import com.eki.parking.Controller.tools.EkiEncoder
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.SecurityBody

/**
 * Created by Hill on 2019/7/31
 */
class AddCreditBody(data:RequestBody.CreditCard) : SecurityBody<RequestBody.CreditCard>(data) {
    override fun requestApi(): EkiApi =EkiApi.AddCreditTest

    override fun encoder() =EkiEncoder.AES

}