package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.EkiRequestBody

/**
 * Created by Hill on 2020/03/27
 */
class AddOpenSetBody: EkiRequestBody() {

    override fun requestApi(): EkiApi =EkiApi.ManagerAddOpenSet

    var id=0
    var serNum=""
    var openSet=ArrayList<RequestBody.Open>()
    var time=""

}