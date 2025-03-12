package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IRequestApi
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.request.abs.EkiRequestBody

/**
 * Created by Hill on 2019/11/26
 */

class EditLocationBody : EkiRequestBody(), IRequestApi {

    override fun requestApi(): EkiApi =EkiApi.ManagerEditLocation

    var id=0
    var info= RequestBody.LocationInfo()
    var config= RequestBody.LocationConfig()
    var socket= ArrayList<RequestBody.LocationSocket>()

    override fun clear() {

    }

    override fun printValue() {

    }

}

//    fun setImg(img:File){
//        if (!dataPair.containsKey("img"))
//            dataPair["img"] = object :IFormDataSet(){
//                override val type: PostDataType
//                    get() = PostDataType.JPG
//            }
//        dataPair["img"]?.file=img
//    }

//    fun setInfo(info:RequestBody.EditLocation){
//        dataPair["info"]?.text=info.toJsonStr()
//    }

//    override val setDataPair: LinkedHashMap<String, IFormDataSet> =
//            LinkedHashMap<String, IFormDataSet>().apply{
//                put("info",object :IFormDataSet(){
//                    override val type: PostDataType
//                        get() = PostDataType.TEXT
//                })
//            }
