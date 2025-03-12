package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.EkiRequestBody

class ChargeBody(val api:EkiApi): EkiRequestBody(){
    override fun requestApi(): EkiApi = api

    var serNum = ArrayList<String>()
}