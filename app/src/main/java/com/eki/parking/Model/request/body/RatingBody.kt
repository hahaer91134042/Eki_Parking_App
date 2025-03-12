package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.EkiRequestBody
import com.eki.parking.Model.sql.EkiOrder

/**
 * Created by Hill on 2020/05/21
 */
class RatingBody(var api:EkiApi):EkiRequestBody(){


    var serial=""
    var star=0.0
    var text=""

    override fun requestApi(): EkiApi =api
}