package com.eki.parking.Model.request

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IEkiSecretHeader
import com.eki.parking.Model.request.abs.EkiRequestBody

/**
 * Created by Hill on 2020/06/16
 */
class NotifyBody:EkiRequestBody(),IEkiSecretHeader{
    override fun requestApi(): EkiApi =EkiApi.LoadNotify


}