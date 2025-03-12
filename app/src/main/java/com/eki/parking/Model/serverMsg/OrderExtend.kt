package com.eki.parking.Model.serverMsg

import com.eki.parking.Controller.impl.IFcmMsg
import com.eki.parking.Controller.impl.ISocketMsg
import com.eki.parking.Controller.socket.SocketMsg
import com.eki.parking.Model.EnumClass.BroadcastMethod
import com.eki.parking.Model.impl.INotifyMsg
import com.eki.parking.R
import com.eki.parking.extension.string
import com.google.gson.JsonObject
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.tools.AppGson

/**
 * Created by Hill on 2020/09/03
 */

class OrderExtend:IFcmMsg<OrderExtend.Content>,INotifyMsg,ISocketMsg<OrderExtend.Content> {
    override val method: String
        get() = BroadcastMethod.OrderExtend.str
    override val socketMethod: String
        get() = BroadcastMethod.OrderExtend.str
    override val title: String
        get() = string(R.string.The_owner_extends_order)
    override val msgFormat: String
        get() = string(R.string.The_owner_extends_order_msg)

    override fun content(content: JsonObject): Content = AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun socketContent(content: JsonObject): Content = AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun mapMsg(content: INotifyMsg.Content): String {
        return when (content) {
            is Content -> {
                val serNum = content.Serial
                val endTime = if (content.End.length >= 14) {
                    content.End.removeRange(content.End.length-3,content.End.length)
                } else {
                    content.End
                }
                msgFormat.messageFormat(serNum, endTime)
            }
            else -> string(R.string.Message_error)
        }
    }

    override val needLogin: Boolean
        get() = true

    class Content:IFcmMsg.Content,ISocketMsg.Content{
        //接收變數命名須和API文件回傳參數名稱一致
        var Serial = ""
        var End = ""

        override val socketContentType: SocketMsg.ContentType
            get() = SocketMsg.ContentType.OrderExtend
    }

}