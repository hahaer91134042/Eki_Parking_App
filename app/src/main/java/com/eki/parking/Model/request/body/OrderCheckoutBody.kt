package com.eki.parking.Model.request.body

import com.eki.parking.Model.DTO.CheckoutInfo
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IRequestApi
import com.hill.devlibs.EnumClass.PostDataType
import com.hill.devlibs.extension.toJsonStr
import com.hill.devlibs.model.bean.FormDataBody
import java.io.File

/**
 * Created by Hill on 2019/11/26
 */
class OrderCheckoutBody : FormDataBody(), IRequestApi {

    override fun requestApi(): EkiApi = EkiApi.OrderCheckOut

    fun setImg(img: File?) {
        dataPair["img"]?.file = img
    }

    fun setInfo(info: CheckoutInfo) {
        dataPair["info"]?.text = info.toJsonStr()
    }

    override val setDataPair: LinkedHashMap<String, IFormDataSet> =
        LinkedHashMap<String, IFormDataSet>().apply {
            put("info", object : IFormDataSet() {
                override val type: PostDataType
                    get() = PostDataType.TEXT
            })
        }

    override fun clear() {

    }

    override fun printValue() {

    }
}