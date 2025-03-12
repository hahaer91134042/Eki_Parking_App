package com.eki.parking.Controller.socket

import com.eki.parking.Controller.impl.ISocketMsg
import com.eki.parking.Model.impl.INotifyMsg
import com.eki.parking.Model.serverMsg.*
import com.google.gson.JsonParser
import com.hill.devlibs.model.ValueObject
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/11/13
 */

object SocketMsg {

    @Deprecated("目前這功能用起來沒有比web api快多少 所以暫時不要用 使用上還比較麻煩")
    abstract class Send<REQUEST>:ValueObject(){

        //這是指 能發送給伺服器解析的指令
        enum class SocketMethod{
            SendAll,
            SendTo,
            LoadLocation
        }

        var time:String=setTime().toString()
        var method=setMethod().toString()
//        var target=setTarget()
        var request:REQUEST?=null

        abstract fun setTime():DateTime
        abstract fun setMethod():SocketMethod
//        abstract fun setTarget():String
//        abstract fun setRequest():REQUEST

        override fun clear() {

        }

        override fun printValue() {
            printObj(this)
        }
    }

    private val list: ArrayList<ISocketMsg<*>> = arrayListOf(
        SocketEvent(),
        GetOrderMsg(),
        ManagerOrderCancelMsg(),
        CheckoutMsg(),
        CancelOrderMsg(),
        ManagerGetPayMsg(),
        OrderNoCheckOut(),
        ManagerBankTransfer(),
        PaySumReport(),
        OrderExtend()
    )


    fun parse(msg:String):ISocketMsg.Content?{
        val obj=JsonParser().parse(msg).asJsonObject
        val socketMsg= list.firstOrNull { it.socketMethod==obj.get("Method").asString }

        return socketMsg?.socketContent(obj.getAsJsonObject("Content"))

    }

    enum class ContentType(val notifyMsg:INotifyMsg?){
        SocketEvent(null),
        GetOrder(GetOrderMsg()),
        CheckOut(CheckoutMsg()),
        ManagerOrderCancel(ManagerOrderCancelMsg()),
        CancelOrder(CancelOrderMsg()),
        ManagerGetPay(ManagerGetPayMsg()),
        OrderNoCheckOut(OrderNoCheckOut()),
        ManagerBankTransfer(ManagerBankTransfer()),
        PaySumReport(PaySumReport()),
        OrderExtend(OrderExtend())
    }

}