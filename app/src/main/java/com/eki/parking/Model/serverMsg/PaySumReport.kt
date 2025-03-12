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

class PaySumReport:IFcmMsg<PaySumReport.Content>,INotifyMsg,ISocketMsg<PaySumReport.Content> {
    override val method: String
        get() = BroadcastMethod.PaySumReport.str
    override val socketMethod: String
        get() = BroadcastMethod.PaySumReport.str
    override val title: String
        get() = string(R.string.Monthly_report_delivery)
    override val msgFormat: String
        get() = string(R.string.PaySumReport)

    override fun content(content: JsonObject): Content = AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun socketContent(content: JsonObject): Content = AppGson.creat().fromJson<Content>(content, Content::class.java)

    override fun mapMsg(content: INotifyMsg.Content): String {

        return when (content) {
            is Content -> {
                val lastSettlementDate = content.Start
                val totalRevenue = content.Sum.toInt().toString().trim()
                val cumulativeFines = content.Mulct.toInt().toString().trim()
                msgFormat.messageFormat(lastSettlementDate, totalRevenue, cumulativeFines)
            }
            else -> string(R.string.Message_error)
        }

    }

    override val needLogin: Boolean
        get() = true

    class Content:IFcmMsg.Content,ISocketMsg.Content{

        var Start = ""
        var Mulct = 0.0
        var Sum = 0.0

        override val socketContentType: SocketMsg.ContentType
            get() = SocketMsg.ContentType.PaySumReport
    }

}