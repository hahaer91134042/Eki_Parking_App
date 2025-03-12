package com.eki.parking.Model

import com.eki.parking.Model.EnumClass.EkiErrorCode
import com.eki.parking.Model.impl.IJsonDeserial
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import com.hill.devlibs.model.bean.ServerResponse
import com.hill.devlibs.tools.AppGson
import com.hill.devlibs.tools.ContentPrinter
import com.hill.devlibs.tools.Log
import org.json.JSONException


/**
 * Created by Hill on 2018/10/31.
 */
abstract class ResponseVO: ServerResponse() {

    @SerializedName("success")
    var isSuccess=false
    @SerializedName("message")
    var message=""
    @SerializedName("errorCode")
    var errorCode: EkiErrorCode = EkiErrorCode.E004
//    set(ratio) {
//        field=ratio
//        msgRes=ErrorParser.parse(ratio).msgRes
//    }
//
//    var msgRes=0

    companion object{
        @Throws(JSONException::class)
        @JvmStatic
        fun < VO:ResponseVO> creat(input:String,clazz:Class<*>?): VO {
            try {
                var obj= JsonParser().parse(input).asJsonObject
                var instance=clazz?.newInstance()
                if (instance is IJsonDeserial<*>)
                    return AppGson.creat(clazz!!,instance.jsonDeserializer())
                            .fromJson<VO>(obj,clazz)
//                var type=object : TypeToken<VO>(){}.type
                return AppGson.creat().fromJson<VO>(obj,clazz)//Gson的bug 注意一定要用class
            }catch (e:Exception){
                Log.e(e.toString())
            }
            throw JSONException("parse Input fail !!")
        }
    }


    override fun clear() {
        isSuccess=false
        message=""
        errorCode=EkiErrorCode.E004
    }

    override fun printValue() {
        printContentValue(ContentPrinter().apply {
            setKeys("success","message","errorCode")
            setValues(isSuccess,message,errorCode)
        })
    }
}