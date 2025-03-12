package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.request.abs.EkiRequestBody
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.printValue


/**
 * Created by Hill on 2019/6/28
 */
class RegisterBody: EkiRequestBody() {
    override fun requestApi(): EkiApi =EkiApi.Register

    var mail=""
    var phone=""
    var pwd=""
    var mobileType=1
    var pushToken=""
    var lan=""
    var beManager=false
    var address:RequestBody.Address?=null
    var info:RequestBody.MemberInfo=RequestBody.MemberInfo()

    override fun printValue() {
        printObj(this)
        address.notNull { it.printValue() }
        info.printValue()
    }

//    class RegisterAddress:EkiRequestBody(){
//        var country=""
//        var state=""
//        var city=""
//        var detail=""
//        var zip=""
//    }

//    class RegisterMemberInfo:EkiRequestBody(){
//        var firstName=""
//        var lastName=""
//        var nickName=""
//        var countryCode=""
//        var phone=""
//    }
}