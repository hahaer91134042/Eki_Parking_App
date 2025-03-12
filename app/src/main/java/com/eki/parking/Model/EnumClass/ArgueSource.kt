package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue

/**
 * Created by Hill on 2020/05/25
 */
enum class ArgueSource:IEnumValue{
    User {
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    },
    Manager {
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    }
}