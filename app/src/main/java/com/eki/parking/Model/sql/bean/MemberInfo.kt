package com.eki.parking.Model.sql.bean

import com.eki.parking.Model.DTO.RequestBody
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.ContentPrinter

/**
 * Created by Hill on 2019/6/20
 */
/**
 * "member": {
"FirstName": "ChiaHung",
"LastName": "Ku",
"CountryCode": "886",
"PhoneNum": "0986108077",
"IconImg": ""
}
 */

class MemberInfo():SqlVO<MemberInfo>() {

    var FirstName=""
    var LastName=""
    var NickName=""
    var CountryCode=""
    var PhoneNum=""
    var IconImg=""

    constructor(info: RequestBody.MemberInfo?):this(){
        FirstName=info?.firstName?:""
        LastName=info?.lastName?:""
        NickName=info?.nickName?:""
        CountryCode=info?.countryCode?:""
        PhoneNum=info?.phone?:""
    }

    override fun clear() {

    }

    override fun printValue() {
        printContentValue(ContentPrinter()
                .setKeys("FirstName","LastName","NickName","CountryCode","PhoneNum","IconImg")
                .setValues(FirstName,LastName,NickName,CountryCode,PhoneNum,IconImg))
    }
}