package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IRequestApi
import com.hill.devlibs.EnumClass.PostDataType
import com.hill.devlibs.extension.toJsonStr
import com.hill.devlibs.model.bean.FormDataBody
import java.io.File

/**
 * Created by Hill on 2020/12/07
 */
class OrderCancelImgBody: FormDataBody(), IRequestApi {

    fun setImg(img: File){
        dataPair["img"]?.file=img
    }

    fun setInfo(info: RequestBody.OrderCancelImgInfo){
        dataPair["info"]?.text=info.toJsonStr()
    }

    override val setDataPair: LinkedHashMap<String, IFormDataSet> =
    LinkedHashMap<String, IFormDataSet>().apply{
        put("img",object :IFormDataSet(){
            override val type: PostDataType
                get() = PostDataType.JPG
        })
        put("info",object :IFormDataSet(){
            override val type: PostDataType
                get() = PostDataType.TEXT
        })
    }

    override fun requestApi(): EkiApi =EkiApi.OrderCancelImg
}