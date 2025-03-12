package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue

/**
 * Created by Hill on 2019/7/31
 */
enum class CreditCategory(override val value: Int): IEnumValue {
    VISA(0) {
        override val default: Boolean
            get() = true
    },
    MASTER(1) {
        override val default: Boolean
            get() = false
    }
}