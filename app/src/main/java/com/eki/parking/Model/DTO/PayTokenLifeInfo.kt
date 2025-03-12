package com.eki.parking.Model.DTO

import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.ContentPrinter

class PayTokenLifeInfo() : SqlVO<PayTokenLifeInfo>() {

    var neweb = ""

    constructor(info: RequestBody.PayTokenInfo?) : this() {
        neweb = info?.neweb ?: ""
    }

    override fun clear() {

    }

    override fun printValue() {
        printContentValue(
            ContentPrinter()
                .setKeys("neweb")
                .setValues(neweb)
        )
    }
}