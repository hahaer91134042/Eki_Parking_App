package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import java.io.Serializable

/**
 * Created by Hill on 2020/06/15
 */
enum class NotifyType:IEnumValue{
    Server {
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    },
    Action {
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    }
}