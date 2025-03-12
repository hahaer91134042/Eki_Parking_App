package com.eki.parking.Model.collection

import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.DTO.SocketSelect
import com.hill.devlibs.tools.Log
import java.io.Serializable

/**
 * Created by Hill on 2019/12/06
 */
class SocketSelectList(): ArrayList<SocketSelect>(), Serializable {

    companion object{
        //切記 要建立新實體的話 要自己重新new 不然會導致錯誤
        val motor=SocketSelectList(
            SocketSelect.eMoving,
            SocketSelect.PBGN,
            SocketSelect.ionex,
            SocketSelect.Home,
            SocketSelect.None
        )
        val car=SocketSelectList(
            SocketSelect.Tesla_ac,
            SocketSelect.J1772,
            SocketSelect.Tesla_dc,
            SocketSelect.CCS1,
            SocketSelect.CCS2,
            SocketSelect.CHAdeMO,
            SocketSelect.None
        )
        val all=SocketSelectList().apply {
            for (soket in CurrentEnum.NONE.sockets)
                add(SocketSelect(CurrentEnum.NONE, soket))

            for (socket in CurrentEnum.AC.sockets)
                add(SocketSelect(CurrentEnum.AC, socket))

            for (socket in CurrentEnum.DC.sockets)
                add(SocketSelect(CurrentEnum.DC, socket))
        }
    }

    init {

    }

    constructor(vararg args:SocketSelect) : this() {
        addAll(args)
    }
    constructor(list:List<SocketSelect>):this(){
        addAll(list)
    }

    //空的表示全選 =預設值
    fun currentContain(current: CurrentEnum): Boolean =
        when {
            size > 0 -> this.any { it.current == current }
            else -> false
        }

    //空的表示全選 =預設值
    fun socketContain(socket: List<ChargeSocket>): Boolean =
        when {
            size > 0 -> this.map { it.socket }.any { socket.contains(it) }
            else -> false
        }


    override fun equals(other: Any?): Boolean {
//        Log.w("SocketSelectList equal other")
        if (other is SocketSelectList){
            if(size!=other.size)
                return false
            return !any { ori->!other.any { ori==it } }
        }
        return super<java.util.ArrayList>.equals(other)
    }
}