package com.eki.parking.Model.serverMsg

import com.eki.parking.Controller.impl.IFcmMsg
import com.eki.parking.Controller.impl.ISocketMsg
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.BroadcastMethod
import com.eki.parking.Model.impl.INotifyMsg
import com.eki.parking.R
import com.eki.parking.extension.string
import com.google.gson.JsonObject
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.tools.AppGson

/**
 * Created by Hill on 2020/09/02
 */
class ManagerOrderCancelMsg:IFcmMsg<ResponseInfo.OrderResult>,INotifyMsg,ISocketMsg<ResponseInfo.OrderResult>{
    override val method: String
        get() = BroadcastMethod.ManagerCancelOrder.str
    override val socketMethod: String
        get() = BroadcastMethod.ManagerCancelOrder.str
    override val title: String
        get() = string(R.string.The_landlord_cancels_the_scheduled_itinerary)
    override val msgFormat: String
        get() = string(R.string.Cancel_the_scheduled_itinerary)

    override fun content(content: JsonObject): ResponseInfo.OrderResult
            =AppGson.creat().fromJson<ResponseInfo.OrderResult>(content.get("Order").asJsonObject,ResponseInfo.OrderResult::class.java)
    override fun socketContent(content: JsonObject): ResponseInfo.OrderResult =
            AppGson.creat().fromJson<ResponseInfo.OrderResult>(content.get("Order").asJsonObject,ResponseInfo.OrderResult::class.java)

    override fun mapMsg(info: INotifyMsg.Content): String {
        return when(info){
            is ResponseInfo.OrderResult->{
                val time="${info.ReservaTime.startDateTime()}~${info.ReservaTime.endDateTime()}"
                val address=info.Address.shortName
                msgFormat.messageFormat(time,address)
            }
            else -> string(R.string.Message_error)
        }
    }

    override val needLogin: Boolean
        get() = true

}