package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IRequestApi
import com.eki.parking.Model.request.abs.EkiFormDataBody
import com.hill.devlibs.extension.toJsonStr
import java.io.File

/**
 * Created by Hill on 2020/09/26
 */
class SetLocationImgBody: EkiFormDataBody(),
                          EkiFormDataBody.ImgData,
                          EkiFormDataBody.InfoDate<RequestBody.SetLocImg>,
                          IRequestApi {

    override fun setImg(img: File){
//        if (!dataPair.containsKey("img"))
//            dataPair["img"] = object :IFormDataSet(){
//                override val type: PostDataType
//                    get() = PostDataType.JPG
//            }

        dataPair[Flag.img]?.file=img
    }

    override fun setInfo(info:RequestBody.SetLocImg){
        dataPair[Flag.info]?.text=info.toJsonStr()
    }

    override fun requestApi(): EkiApi =EkiApi.ManagerSetLocationImg
}