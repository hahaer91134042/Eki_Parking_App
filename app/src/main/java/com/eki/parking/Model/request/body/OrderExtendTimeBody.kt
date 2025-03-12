package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IRequestApi
import com.eki.parking.Model.request.abs.EkiRequestBody

class OrderExtendTimeBody:EkiRequestBody(),IRequestApi{
    override fun requestApi(): EkiApi =EkiApi.OrderExtendTime

    var data=ArrayList<RequestBody.OrderExtend>()

}