package com.eki.parking.Model.request.body

import com.eki.parking.Controller.tools.EkiEncoder
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.SecurityBody
import com.hill.devlibs.impl.IEncoder

/**
 * Created by Hill on 2020/04/22
 */
class BeManagerBody(data: RequestBody.BankInfo) :SecurityBody<RequestBody.BankInfo>(data){


    var address=RequestBody.Address()

    override fun encoder(): IEncoder =EkiEncoder.AES
    override fun requestApi(): EkiApi =EkiApi.BeManager
}