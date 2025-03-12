package com.eki.parking.Controller.impl

import com.eki.parking.Model.impl.INotifyMsg
import com.google.gson.JsonObject

/**
 * Created by Hill on 2020/09/02
 */
interface IFcmMsg<T:IFcmMsg.Content> {
    val method:String
    val msgFormat:String

    fun content(content:JsonObject):T

    interface Content:INotifyMsg.Content
}