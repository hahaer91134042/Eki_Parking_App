package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.EkiRequestBody


/**
 * Created by Hill on 2020/06/20
 */
class GetBankBody:EkiRequestBody(){
    override fun requestApi(): EkiApi =EkiApi.ManagerGetBank
}