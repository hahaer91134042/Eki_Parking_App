package com.eki.parking.Controller.activity.frag.BillingOverview.bean

data class ActualIncomeBean(
    var serial_number: String,
    var title: String,
    var picture: String,
    var number_parking_space_income: Int,
    var number_default_fee: Int,
    var number_actual_income: Int
)