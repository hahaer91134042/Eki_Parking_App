package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.R

enum class CurrentEnum(var str: Int,
                       val carStr:Int,
                       var sockets: ArrayList<ChargeSocket>) : IEnumValue {

    //現在 地點裡面的 Socket沒有這項 沒有插頭就是一般車位
    NONE(R.string.General_parking_space,
            R.string.General,
            arrayListOf(ChargeSocket.NONE)) {
        override val value: Int
            get() = 0
        override val default: Boolean
            get() = true
    },
    AC(R.string.AC_parking_space,
            R.string.Electric_Car_AC,
            arrayListOf(ChargeSocket.Tesla,
                    ChargeSocket.SAEJ1772,
//                    ChargeSocket.IEC62196_2,
//                    ChargeSocket.GBT_20234,
                    ChargeSocket.eMoving,
                    ChargeSocket.Gogoro_PBGN,
                    ChargeSocket.ionex,
                    ChargeSocket.Household_Socket)) {
        override val value: Int
            get() = 1
        override val default: Boolean
            get() = false
    },
    DC(R.string.DC_parking_space,
            R.string.Electric_Car_DC,
            arrayListOf(ChargeSocket.Tesla,
                    ChargeSocket.CHAdeMO,
                    ChargeSocket.CCS1,
                    ChargeSocket.CCS2
//                    ChargeSocket.GBT_20234
            )) {
        override val value: Int
            get() = 2
        override val default: Boolean
            get() = false
    };

    fun contain(socket:ChargeSocket):Boolean=sockets.contains(socket)

    companion object {
        fun parse(value: Int): CurrentEnum {
            values().forEach {
                when (it.value) {
                    value -> return it
                }
            }
            return NONE
        }
    }
}