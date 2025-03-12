package com.eki.parking.Model.DTO

import com.eki.parking.Controller.impl.IMappingName
import com.hill.devlibs.impl.IConvertData
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.ContentPrinter

/**
 * Created by Hill on 2019/6/20
 */
/**
 * "address": {
"Country": "中華民國",
"State": "台灣",
"City": "新北市",
"Detail": "中和區大勇街25巷38之2號7樓",
"ZipCode": "23579"
}
 *
 */

class AddressInfo() :SqlVO<AddressInfo>(),IMappingName,IConvertData<RequestBody.Address>{

    var Country=""
    var State=""
    var City=""
    var Detail=""
    var ZipCode=""

    constructor(address: RequestBody.Address?):this(){
        Country=address?.country?:""
        State=address?.state?:""
        City=address?.city?:""
        Detail=address?.detail?:""
        ZipCode=address?.zip?:""
    }

    override fun clear() {

    }

    override fun copyFrom(from: AddressInfo): Boolean =
            runCatching {
                Country=from.Country
                State=from.State
                City=from.City
                Detail=from.Detail
                ZipCode=from.ZipCode
            }.isSuccess

    override fun printValue() {
        printContentValue(ContentPrinter()
                .setKeys("Country","State","City","Detail","ZipCode")
                .setValues(Country,State,City,Detail,ZipCode))
    }

    override val fullName: String
        get() = Country+State+City+Detail
    override val shortName: String
        get() = City+Detail

    override fun toData(): RequestBody.Address =RequestBody.Address().apply {
        country=Country
        state=State
        city=City
        detail=Detail
        zip=ZipCode
    }

    override fun toString(): String {
        return "${Country}${State}"
    }
}