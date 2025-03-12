package com.eki.parking.Model.DTO

/**
 * Created by Hill on 2019/11/26
 */
data class CheckoutInfo(val number:String,val date:String) {
    var lat:Double=0.0
    var lng:Double=0.0
    var action=""
    var discount=""
    var invoice:RequestBody.OrderInvoice?=null
}