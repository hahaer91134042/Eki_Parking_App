package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.EkiErrorCode
import com.eki.parking.Model.EnumClass.NotifyType
import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.impl.IJsonDeserial
import com.eki.parking.extension.toEnum
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.tools.Log
import java.lang.reflect.Type

/**
 * Created by Hill on 2020/06/16
 */
class NotifyResponse:ResponseVO(),IJsonDeserial<NotifyResponse>{

    var info=ArrayList<ResponseInfo.EkiNotify>()

    /**
     * {
    "info":[
        {
            "Type":0,
            "Content":{"Title":"2020/06/25 系統備份","Msg":"目前因為機房調整 所以必須進行維護","Html":""}
        },
        {
            "Type":1,
            "Content":{
            "Name":"上市活動",
            "Page":{
                "Html":"<div><img src=\"http://iparkingnet.eki.com.tw/upload/ppypLogo.png\" /><br/><span style=\"border:2px red solid;font-size:12px;\">測試用活動頁</span></div>\n",
                "Url":" "
                }
            }
        }

    ],"success":true,"message":"success","errorCode":"E000"
    }
     */

    override fun jsonDeserializer(): JsonDeserializer<NotifyResponse> {

        return JsonDeserializer<NotifyResponse> { json, typeOfT, context ->
            var obj=json?.asJsonObject
            var response=NotifyResponse().also {
                it.isSuccess=obj?.get("success")?.asBoolean?:false
                it.message=obj?.get("message")?.asString?:""
                obj?.get("errorCode")?.asString.notNull { e->
                    it.errorCode=EkiErrorCode.valueOf(e)
                }
            }
            var info=obj?.getAsJsonArray("info")
            var gson=Gson()
            info?.forEach {
                var innerObj=it.asJsonObject
                var type=innerObj.get("Type").asInt.toEnum<NotifyType>()
                var content=innerObj.get("Content").asJsonObject
                when(type){
                    NotifyType.Server->{
                        response.info.add(ResponseInfo.EkiNotify().apply {
                            Type=type.value
                            Server=gson.fromJson(content,ResponseInfo.ServerNotify::class.java)
//                            Content=gson.fromJson(content,ResponseInfo.ServerNotify::class.java)
                        })
                    }
                    NotifyType.Action->{
                        response.info.add(ResponseInfo.EkiNotify().apply {
                            Type=type.value
                            Action=gson.fromJson(content,ResponseInfo.ActionNotify::class.java)
//                            Content=gson.fromJson(content,ResponseInfo.ActionNotify::class.java)
                        })
                    }
                }
            }

            response
        }
    }


}