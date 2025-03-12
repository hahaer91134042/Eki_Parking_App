package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.R

/**
 * Created by Hill on 2020/03/19
 */
enum class SiteType(val strRes:Int):IEnumValue{
    Flat(R.string.Flat_parking){
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    },
    Mechanical(R.string.Mechanical_parking){
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    }
}