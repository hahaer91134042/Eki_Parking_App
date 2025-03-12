package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue

/**
 * Created by Hill on 2020/08/26
 */
enum class ActionType :IEnumValue{
    Discount {
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    }
}