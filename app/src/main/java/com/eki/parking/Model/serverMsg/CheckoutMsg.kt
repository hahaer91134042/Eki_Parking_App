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
import java.io.Serializable

/**
 * Created by Hill on 2020/09/03
 */
class CheckoutMsg:IFcmMsg<CheckoutMsg.Content>,INotifyMsg,ISocketMsg<CheckoutMsg.Content> {
    override val method: String
        get() = BroadcastMethod.GetCheckOut.str
    override val socketMethod: String
        get() = BroadcastMethod.GetCheckOut.str
    override val title: String
        get() = string(R.string.The_owner_successfully_withdraws_the_lease)
    override val msgFormat: String
        get() = string(R.string.Successfully_surrendered)

    override fun content(content: JsonObject): Content =
            AppGson.creat().fromJson<Content>(content, Content::class.java)
    override fun socketContent(content: JsonObject): Content =
            AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun mapMsg(c: INotifyMsg.Content): String {
        return when(c){
            is Content->{
                val serNum=c.OrderSerNum
                val time="${c.StartTime}~${c.EndTime}"
//                var amt=c.Cost
                msgFormat.messageFormat(serNum,time)
            }
            else -> string(R.string.Message_error)
        }
    }

    override val needLogin: Boolean
        get() = true

    class Content:Serializable,IFcmMsg.Content,ISocketMsg.Content{
        var OrderSerNum=""
        var Cost=0.0
        var Unit=0
        var HandlingFee=0.0
        var StartTime=""
        var EndTime=""
        var Img=""
        var CarNum=""
        override val socketContentType: SocketMsg.ContentType
            get() = SocketMsg.ContentType.CheckOut
    }




}