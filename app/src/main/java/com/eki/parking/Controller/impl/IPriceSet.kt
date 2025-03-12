package com.eki.parking.Controller.impl

import com.eki.parking.Model.EnumClass.BillingMethod

/**
 * Created by Hill on 2020/09/01
 */
interface IPriceSet<T> {
    fun price():T
    fun billingMethod():BillingMethod
}