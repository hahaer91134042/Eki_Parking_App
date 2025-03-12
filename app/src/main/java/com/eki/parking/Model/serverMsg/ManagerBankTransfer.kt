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

class ManagerBankTransfer:IFcmMsg<ManagerBankTransfer.Content>,INotifyMsg,ISocketMsg<ManagerBankTransfer.Content> {
    override val method: String
        get() = BroadcastMethod.ManagerBankTransfer.str
    override val socketMethod: String
        get() = BroadcastMethod.ManagerBankTransfer.str
    override val title: String
        get() = string(R.string.Landlord_receipt_notice)
    override val msgFormat: String
        get() = string(R.string.Manager_bank_transfer)

    override fun content(content: JsonObject): Content = AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun socketContent(content: JsonObject): Content = AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun mapMsg(content: INotifyMsg.Content): String {

        return when (content) {
            is Content -> {
                val date=content.Date
                val amt=content.Amt.toInt().toString().trim()
                msgFormat.messageFormat(date,amt)
            }
            else -> string(R.string.Message_error)
        }
    }

    override val needLogin: Boolean
        get() = true


    class Content:IFcmMsg.Content,ISocketMsg.Content{
        var Date=""
        var Amt=0.0

        override val socketContentType: SocketMsg.ContentType
            get() = SocketMsg.ContentType.ManagerBankTransfer
    }

}