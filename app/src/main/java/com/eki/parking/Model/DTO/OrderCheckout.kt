package com.eki.parking.Model.DTO

import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 2020/05/20
 */
class OrderCheckout:SqlVO<OrderCheckout>(){

    var Date=""
    var CostFix:Double=0.0
    var Claimant:Double=0.0
    var Img=""

    override fun clear() {

    }
}