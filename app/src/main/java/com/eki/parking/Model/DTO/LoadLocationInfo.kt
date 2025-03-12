package com.eki.parking.Model.DTO

import com.eki.parking.Model.ListInVO
import com.eki.parking.Model.sql.EkiLocation

class LoadLocationInfo:ListInVO<EkiLocation>() {

    var Page=0
    var Total=0
    var Lat:Double=0.0
    var Lng:Double=0.0

    override fun printValue() {
        printObj(this)
        super.printValue()
    }
}