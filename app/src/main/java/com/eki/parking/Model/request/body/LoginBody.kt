package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.abs.EkiRequestBody

/**
 * Created by Hill on 2019/6/17
 */
class LoginBody: EkiRequestBody() {
    override fun requestApi(): EkiApi =EkiApi.Login

    var acc=""
    var pwd=""
    var lan=""
    var pushToken=""
    var mobileType=1

}