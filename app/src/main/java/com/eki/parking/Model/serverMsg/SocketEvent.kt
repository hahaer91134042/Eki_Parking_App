package com.eki.parking.Model.serverMsg

import com.eki.parking.Controller.impl.ISocketMsg
import com.eki.parking.Controller.socket.SocketMsg
import com.google.gson.JsonObject
import com.hill.devlibs.tools.AppGson

/**
 * Created by Hill on 2020/11/16
 */
class SocketEvent:ISocketMsg<SocketEvent.Content>{
    companion object{
        const val open=1
        const val close=0
    }


    override val socketMethod: String
        get() = "SocketEvent"



    override fun socketContent(data: JsonObject): Content {

        return AppGson.creat()
                .fromJson<Content>(data, Content::class.java)
    }

    class Content:ISocketMsg.Content{
        var Event= close
        var Msg=""
        override val socketContentType: SocketMsg.ContentType
            get() = SocketMsg.ContentType.SocketEvent
    }
}