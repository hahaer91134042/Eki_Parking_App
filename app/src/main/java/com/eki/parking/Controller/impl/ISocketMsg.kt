package com.eki.parking.Controller.impl

import com.eki.parking.Controller.socket.SocketMsg
import com.google.gson.JsonObject

/**
 * Created by Hill on 2020/11/16
 */
interface ISocketMsg<T:ISocketMsg.Content> {
    val socketMethod:String

    fun socketContent(content: JsonObject):T

    interface Content{
        val socketContentType:SocketMsg.ContentType
    }

}