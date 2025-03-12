package com.eki.parking.Model.DTO

import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.EnumClass.CurrentEnum
import java.io.Serializable

/**
 * Created by Hill on 2019/12/06
 */
data class SocketSelect(var current:CurrentEnum,var socket:ChargeSocket):Serializable{

    companion object {
        val None=SocketSelect(CurrentEnum.NONE,ChargeSocket.NONE)

        val eMoving=SocketSelect(CurrentEnum.AC,ChargeSocket.eMoving)
        val PBGN=SocketSelect(CurrentEnum.AC,ChargeSocket.Gogoro_PBGN)
        val ionex=SocketSelect(CurrentEnum.AC,ChargeSocket.ionex)
        val Home=SocketSelect(CurrentEnum.AC,ChargeSocket.Household_Socket)
        val Tesla_ac=SocketSelect(CurrentEnum.AC,ChargeSocket.Tesla)
        val J1772=SocketSelect(CurrentEnum.AC,ChargeSocket.SAEJ1772)

        val Tesla_dc=SocketSelect(CurrentEnum.DC,ChargeSocket.Tesla)
        val CCS1=SocketSelect(CurrentEnum.DC,ChargeSocket.CCS1)
        val CCS2=SocketSelect(CurrentEnum.DC,ChargeSocket.CCS2)
        val CHAdeMO=SocketSelect(CurrentEnum.DC,ChargeSocket.CHAdeMO)
    }

    override fun equals(other: Any?): Boolean {
        if (other is SocketSelect){
            return current==other.current&&socket==other.socket
        }
        return super.equals(other)
    }
}