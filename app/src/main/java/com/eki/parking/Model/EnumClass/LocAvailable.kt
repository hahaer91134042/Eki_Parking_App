package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue

/**
 * Created by Hill on 2020/08/14
 */
enum class LocAvailable :IEnumValue{
    UnKnow {
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    },
    Available {
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    },
    UnAvailable {
        override val value: Int
            get() = 2
        override val default: Boolean
            get() = false
    }
}