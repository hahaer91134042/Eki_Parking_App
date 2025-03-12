package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.R

/**
 * Created by Hill on 2020/03/19
 */
enum class SitePosition(val strRes:Int):IEnumValue{
    OutSide(R.string.outdoor){
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    },
    InSide(R.string.indoor){
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    }
}