package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue

/**
 * Created by Hill on 25,09,2019
 */
enum class CurrencyUnit(override val value: Int): IEnumValue {
    TWD(0) {
        override val default: Boolean
            get() = true
    },
    USD(1) {
        override val default: Boolean
            get() = false
    },
    JPY(2) {
        override val default: Boolean
            get() = false
    },
    RMB(3) {
        override val default: Boolean
            get() = false
    };

    companion object{
        fun parse(value: Int): CurrencyUnit {
            values().forEach {
                when (it.value) {
                    value -> return it
                }
            }
            return TWD
        }
    }
}