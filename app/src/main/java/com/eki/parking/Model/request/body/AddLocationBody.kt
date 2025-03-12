package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IRequestApi
import com.eki.parking.Model.request.abs.EkiRequestBody

/**
 * Created by Hill on 2020/04/14
 */
class AddLocationBody:EkiRequestBody(),IRequestApi{
    override fun requestApi(): EkiApi =EkiApi.ManagerAddLocation

    var lat:Double=0.0
    var lng:Double=0.0

    var address= RequestBody.Address()
    var info= RequestBody.LocationInfo()
    var config= RequestBody.LocationConfig()
    var socket= ArrayList<RequestBody.LocationSocket>()
//    fun setImg(img: File){
//        if (!dataPair.containsKey("img"))
//            dataPair["img"] = object :IFormDataSet(){
//                override val type: PostDataType
//                    get() = PostDataType.JPG
//            }
//        dataPair["img"]?.file=img
//    }
//    fun setInfo(info: RequestBody.AddLocation){
//        dataPair["info"]?.text=info.toJsonStr()
//    }

//    override val setDataPair: LinkedHashMap<String, IFormDataSet> =
//            LinkedHashMap<String, IFormDataSet>().apply{
//                put("info",object :IFormDataSet(){
//                    override val type: PostDataType
//                        get() = PostDataType.TEXT
//
//                })
//            }
}