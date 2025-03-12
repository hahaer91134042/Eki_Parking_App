package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.R

/**
 * Created by Hill on 25,09,2019
 */
enum class WeekDay(val strRes:Int) : IEnumValue {
    NONE(R.string.none){
        override val value: Int
            get() = -1
        override val default: Boolean
            get() = true
    },
    Sunday(R.string.Sunday){
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = false
    },
    Monday(R.string.Monday){
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    },
    Tuesday(R.string.Tuesday){
        override val value: Int
            get() = 2
        override val default: Boolean
            get() = false
    },
    Wednesday(R.string.Wednesday){
        override val value: Int
            get() = 3
        override val default: Boolean
            get() = false
    },
    Thursday(R.string.Thursday){
        override val value: Int
            get() = 4
        override val default: Boolean
            get() = false
    },
    Friday(R.string.Friday){
        override val value: Int
            get() = 5
        override val default: Boolean
            get() = false
    },
    Saturday(R.string.Saturday){
        override val value: Int
            get() = 6
        override val default: Boolean
            get() = false
    };

    companion object{
        fun parse(value: Int): WeekDay {
            values().forEach {
                when (it.value) {
                    value -> return it
                }
            }
            return NONE
        }
    }
}