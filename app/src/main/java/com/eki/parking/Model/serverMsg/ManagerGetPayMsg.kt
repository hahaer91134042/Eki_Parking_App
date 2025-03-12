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
 * Created by Hill on 2021/02/03
 */
class ManagerGetPayMsg:IFcmMsg<ManagerGetPayMsg.Content>,
                       ISocketMsg<ManagerGetPayMsg.Content>,
                       INotifyMsg{

    override val method: String
        get() = BroadcastMethod.ManagerGetPay.str
    override val socketMethod: String
        get() = BroadcastMethod.ManagerGetPay.str
    override val title: String
        get() = string(R.string.Car_owner_payment_completed)
    override val msgFormat: String
        get() = string(R.string.Payment_completed)

    override fun content(content: JsonObject): Content
            =AppGson.creat().fromJson<Content>(content,Content::class.java)
    override fun socketContent(content: JsonObject): Content
            =AppGson.creat().fromJson<Content>(content,Content::class.java)

    override fun mapMsg(content: INotifyMsg.Content): String {
        return when(content){
            is Content->{
                val serNum=content.OrderSerNum
                val amt=content.Cost
                msgFormat.messageFormat(serNum,amt)
            }
            else -> string(R.string.Message_error)
        }
    }

    override val needLogin: Boolean
        get() = true


    class Content: IFcmMsg.Content, ISocketMsg.Content{

        var OrderSerNum=""
        var Cost=0.0

        override val socketContentType: SocketMsg.ContentType
            get() = SocketMsg.ContentType.ManagerGetPay

    }
}