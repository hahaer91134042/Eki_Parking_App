package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.R
import com.eki.parking.extension.string
import com.hill.devlibs.extension.messageFormat

/**
 * Created by Hill on 25,09,2019
 */
enum class BillingMethod(override val value: Int,val format:Int): IEnumValue {
    Per30Mins(0, R.string.billing_method_30min) {
        override val default: Boolean
            get() = true
    };

    fun formatePrice(price:Double):String{
        return string(format).messageFormat(price)
    }
    companion object{
        fun parse(value: Int): BillingMethod {
            values().forEach {
                when (it.value) {
                    value -> return it
                }
            }
            return Per30Mins
        }
    }
}