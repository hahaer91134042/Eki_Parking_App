package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.CheckoutInfo
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IRequestApi
import com.eki.parking.Model.request.abs.EkiRequestBody
import com.hill.devlibs.EnumClass.PostDataType
import com.hill.devlibs.extension.toJsonStr
import com.hill.devlibs.model.bean.FormDataBody
import java.io.File


class OrderLinePayBody : EkiRequestBody() {

    override fun requestApi(): EkiApi = EkiApi.OrderLinePay

    var serial = ""

    override fun clear() {

    }

    override fun printValue() {

    }
}