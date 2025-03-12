package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.R

enum class ChargeSocket constructor(override val value: Int, var socketName: Int): IEnumValue {

    NONE(0,R.string.socket_none) {
        override val default: Boolean
            get() = true
    },
    All(1,R.string.socket_all){
        override val default: Boolean
            get() = false
    },
    CCS2(2,R.string.socket_CCS2) {//CCS2-IEC62196
        override val default: Boolean
            get() = false
    },
    CHAdeMO(3,R.string.socket_CHAdeMO) {
        override val default: Boolean
            get() = false
    },
    Tesla(4,R.string.socket_Tesla) {
        override val default: Boolean
            get() = false
    },
    CCS1(5,R.string.socket_Type1CCS) {//CCS1-J1772
        override val default: Boolean
            get() = false
    },
    GBT_20234(6,R.string.socket_GBT20234) {
        override val default: Boolean
            get() = false
    },
    SAEJ1772(7,R.string.socket_SAEJ1772) {
        override val default: Boolean
            get() = false
    },
    CNS15511(8,R.string.socket_CNS15511) {
        override val default: Boolean
            get() = false
    },
    IEC62196(9,R.string.socket_IEC621962) {
        override val default: Boolean
            get() = false
    },
    eMoving(10,R.string.eMoving){
        override val default: Boolean
        get() = false
    },
    Gogoro_PBGN(11,R.string.PBGN) {
        override val default: Boolean
        get() = false
    },
    ionex(12,R.string.ionex) {
        override val default: Boolean
        get() = false
    },
    Household_Socket(13,R.string.Household_socket) {
        override val default: Boolean
        get() = false
    };

    companion object{
        fun parse(value: Int): ChargeSocket {
            values().forEach {
                when (it.value) {
                    value -> return it
                }
            }
            return NONE
        }
    }
}