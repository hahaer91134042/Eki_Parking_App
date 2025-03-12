package com.eki.parking.Model.request.body

import com.eki.parking.Controller.tools.EkiEncoder
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.SecurityBody
import com.hill.devlibs.impl.IEncoder

/**
 * Created by Hill on 2020/06/02
 */
class EditPwdBody(data: RequestBody.EditPwd) : SecurityBody<RequestBody.EditPwd>(data) {
    override fun requestApi(): EkiApi =EkiApi.MemberEditPwd
    override fun encoder(): IEncoder =EkiEncoder.AES
}