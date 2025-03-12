package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue

/**
 * Created by Hill on 2020/09/09
 */
enum class OrderInvoiceType:IEnumValue{
    None {
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = false
    },
    Donate {
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = true
    },
    Paper {
        override val value: Int
            get() = 2
        override val default: Boolean
            get() = false
    },
    Phone {
        override val value: Int
            get() = 3
        override val default: Boolean
            get() = false
    },
    Nature {
        override val value: Int
            get() = 4
        override val default: Boolean
            get() = false
    },
    EzPay {
        override val value: Int
            get() = 5
        override val default: Boolean
            get() = false
    }
}