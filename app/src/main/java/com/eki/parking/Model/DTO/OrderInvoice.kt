package com.eki.parking.Model.DTO

import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 2020/10/26
 */
class OrderInvoice:SqlVO<OrderInvoice>(){

    var Number=""
    var Card4No=""


    override fun clear() {

    }
}