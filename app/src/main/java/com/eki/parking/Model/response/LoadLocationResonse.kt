package com.eki.parking.Model.response

import com.eki.parking.Controller.impl.ILoadMultiPage
import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.DTO.LoadLocationInfo

class LoadLocationResonse:ResponseVO(), ILoadMultiPage {

    var info: LoadLocationInfo?=null


    override fun total(): Int =info?.Total?:1

    override fun page(): Int =info?.Page?:1


    override fun printValue() {
        super.printValue()
        info?.printValue()
    }
}