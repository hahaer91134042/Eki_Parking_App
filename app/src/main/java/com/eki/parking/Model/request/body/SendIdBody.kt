package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.EkiRequestBody
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit

/**
 * Created by Hill on 31,10,2019
 */
open class SendIdBody(private var api:EkiApi): EkiRequestBody() {

    var id=ArrayList<Int>()
    var serNum=ArrayList<String>()
    var token=""
    var time=""
    var lat=0.0
    var lng=0.0
    var timeSpan:RequestBody.TimeSpan?=null
    var times:List<RequestBody.TimeSpan>?=null
    var invoice:RequestBody.OrderInvoice?=null
    var isVerify=false

    override fun requestApi(): EkiApi =api

}