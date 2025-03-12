package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.impl.IRequestApi
import com.eki.parking.Model.EnumClass.EkiApi
import com.hill.devlibs.EnumClass.PostDataType
import com.hill.devlibs.extension.toJsonStr
import com.hill.devlibs.model.bean.FormDataBody
import java.io.File

/**
 * Created by Hill on 2019/12/27
 */
class VehicleBody(private var api:EkiApi):FormDataBody(), IRequestApi {
    override fun requestApi(): EkiApi =api

    fun setImg(img:File){
        if (!dataPair.containsKey("img"))
            dataPair["img"] = object :IFormDataSet(){
                override val type: PostDataType
                    get() = PostDataType.JPG
            }
        dataPair["img"]?.file=img

    }

    fun setInfo(info: RequestBody.VehicleInfo){
        dataPair["info"]?.text=info.toJsonStr()
    }

    override val setDataPair: LinkedHashMap<String, IFormDataSet> =
            LinkedHashMap<String, IFormDataSet>().apply{
                put("info",object :IFormDataSet(){
                    override val type: PostDataType
                        get() = PostDataType.TEXT
                })
            }

    override fun clear() {

    }

    override fun printValue() {

    }

}