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

class CancelOrderMsg: IFcmMsg<CancelOrderMsg.Content>, INotifyMsg, ISocketMsg<CancelOrderMsg.Content> {

    override val method: String
        get() = BroadcastMethod.CancelOrder.str
    override val socketMethod: String
        get() = BroadcastMethod.CancelOrder.str
    override val title: String
        get() = string(R.string.The_owner_cancels_the_appointment)
    override val msgFormat: String
        get() = string(R.string.Cancels_appointment)

    override fun content(content: JsonObject): Content = AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun socketContent(content: JsonObject): Content = AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun mapMsg(content: INotifyMsg.Content): String {

        return when (content) {
            is Content -> {
                val locName = content.Name
                val time = "${content.Start}~${content.End}"
                val carNum = content.CarNum
                msgFormat.messageFormat(locName, time, carNum)
            }
            else -> string(R.string.Message_error)

        }

    }

    override val needLogin: Boolean
        get() = true

    class Content:IFcmMsg.Content,ISocketMsg.Content{
        var Name=""
        var Start=""
        var End=""
        var CarNum=""
        override val socketContentType: SocketMsg.ContentType
            get() = SocketMsg.ContentType.CancelOrder
    }

}