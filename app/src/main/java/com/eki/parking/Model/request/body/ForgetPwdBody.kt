package com.eki.parking.Model.request.body

import com.eki.parking.Controller.tools.EkiEncoder
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IEkiSecretHeader
import com.eki.parking.Model.request.abs.SecurityBody
import com.hill.devlibs.impl.IEncoder

/**
 * Created by Hill on 2020/10/07
 */
class ForgetPwdBody(data: RequestBody.ForgetPwd) : SecurityBody<RequestBody.ForgetPwd>(data),
                                                   IEkiSecretHeader {
    override fun requestApi(): EkiApi =EkiApi.MemberForgetPwd
    override fun encoder(): IEncoder =EkiEncoder.AES
}