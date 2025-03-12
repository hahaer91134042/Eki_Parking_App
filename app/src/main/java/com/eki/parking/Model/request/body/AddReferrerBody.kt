package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.EkiRequestBody

/**
 * Created by Hill on 2020/06/23
 */
class AddReferrerBody:EkiRequestBody(){

    var code=""

    override fun requestApi(): EkiApi =EkiApi.ManagerAddReferrer
}