package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.ActionType
import com.eki.parking.Model.EnumClass.EkiErrorCode
import com.eki.parking.Model.EnumClass.NotifyType
import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.impl.IJsonDeserial
import com.eki.parking.extension.toEnum
import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.tools.Log
import java.lang.Exception

/**
 * Created by Hill on 2020/08/26
 */
class ActionResponse:ResponseVO(), IJsonDeserial<ActionResponse> {

    var info=ArrayList<ResponseInfo.Action>()


    override fun jsonDeserializer(): JsonDeserializer<ActionResponse> =
            JsonDeserializer { json, typeOfT, context ->
                var obj=json?.asJsonObject
                var response=ActionResponse().also {
                    it.isSuccess=obj?.get("success")?.asBoolean?:false
                    it.message=obj?.get("message")?.asString?:""
                    obj?.get("errorCode")?.asString.notNull { e->
                        it.errorCode= EkiErrorCode.valueOf(e)
                    }
                }
                var info=obj?.getAsJsonArray("info")
                var gson= Gson()

                info?.forEach {
                    try {
                        var innerObj=it.asJsonObject
                        var serial=innerObj.get("Serial").asString
//                    Log.w("Action isJsonNull->${innerObj.get("Action").isJsonNull}")
                        //去掉沒有辦法使用的錯誤活動碼
                        if (!innerObj.get("Action").isJsonNull){
                            innerObj.getAsJsonObject("Action").notNull { actionObj->
                                var type=actionObj.get("Type").asInt.toEnum<ActionType>()
                                var detail=actionObj.get("Detail").asJsonObject

                                when(type){
                                    ActionType.Discount->{
                                        response.info.add(ResponseInfo.Action().apply {
                                            Serial=serial
                                            Type=type.value
                                            discount=gson.fromJson(detail,ResponseInfo.ActionDiscount::class.java)
                                        })
                                    }
                                }
                            }
                        }
                    }catch (e:Exception){
                        printException(e)
                    }
                }
                response
            }


}