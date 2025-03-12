package com.eki.parking.Controller.Fcm

import com.eki.parking.Model.impl.INotifyMsg
import com.eki.parking.Model.serverMsg.FcmContent
import com.google.gson.JsonParser

/**
 * Created by Hill on 2020/09/02
 */

object FcmMsg {

    fun parse(data: MutableMap<String, String>):Result?{

        val contentType= FcmContent.msgList.firstOrNull { it.method==data["Method"] }

        if (contentType!=null){
            return Result().apply {
                val content=contentType.content(JsonParser().parse(data["Content"]).asJsonObject)

                if (contentType is INotifyMsg){
                    title=contentType.title
                    msg=contentType.mapMsg(content)
                    needLogin=contentType.needLogin
                }

//                title=contentType.title
//                msg=contentType.content(JsonParser().parse(data["Content"]).asJsonObject)
            }
        }
        return null
    }

    class Result{
        var title=""
        var msg=""
        var needLogin=false
    }

}