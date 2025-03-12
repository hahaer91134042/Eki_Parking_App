package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.R

/**
 * Created by Hill on 2020/03/19
 */
enum class SiteSize(val strRes:Int):IEnumValue{
    Standar(R.string.Standard_parking_spaces){
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    },
    Small(R.string.Small_parking_spaces){
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    },
    Motor(R.string.Motorcycle_parking_space){
        override val value: Int
            get() = 2
        override val default: Boolean
            get() = false
    };
}