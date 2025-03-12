package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IEkiSecretHeader
import com.eki.parking.Model.request.abs.EkiRequestBody
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2019/6/25
 */
class SmsCheckBody : EkiRequestBody(),IEkiSecretHeader{
    override fun requestApi(): EkiApi=EkiApi.SmsConfirm

    var lan=""
    var countryCode=""
    var phone=""
    var isForget=false

    override fun printValue() {
        Log.w("SmsCheckBody->${bodyStr}")
    }

}