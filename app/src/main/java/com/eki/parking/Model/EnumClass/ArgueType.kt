package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.R

/**
 * Created by Hill on 2020/05/22
 */
enum class ArgueType(val str:Int):IEnumValue{
    Other(R.string.Other) {
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    },
    Report(R.string.Report){
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    },
    IllegalParking(R.string.Illegal_parking){
        override val value: Int
            get() = 2
        override val default: Boolean
            get() = false
    },
    BadAttitude(R.string.Bad_attitude){
        override val value: Int
            get() = 3
        override val default: Boolean
            get() = false
    },
    TrashOrDamaged(R.string.Leftover_trash_damaged_items){
        override val value: Int
            get() = 4
        override val default: Boolean
            get() = false
    },
    DirtyEnvironment(R.string.Dirty_environment){
        override val value: Int
            get() = 5
        override val default: Boolean
            get() = false
    },
    FakePhoto(R.string.Fake_Photo){
        override val value: Int
            get() = 6
        override val default: Boolean
            get() = false
    }
}