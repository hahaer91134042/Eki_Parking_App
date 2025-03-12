package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.EkiRequestBody

/**
 * Created by Hill on 2020/06/01
 */
class MemberEditBody: EkiRequestBody() {

    var mail=""
    var phone=""
    var pwd=""
    var address: RequestBody.Address?=null
    var info: RequestBody.MemberInfo?=null

    override fun requestApi(): EkiApi =EkiApi.MemberEdit
}