package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.DTO.OrderReservaTime
import com.eki.parking.Model.request.abs.EkiRequestBody

/**
 * Created by Hill on 06,11,2019
 */
class OrderAddBody: EkiRequestBody() {

    var times=ArrayList<OrderReservaTime>()

    override fun requestApi(): EkiApi =EkiApi.OrderAdd
}