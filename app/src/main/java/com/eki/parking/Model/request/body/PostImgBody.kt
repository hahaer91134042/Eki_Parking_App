package com.eki.parking.Model.request.body

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.impl.IRequestApi
import com.hill.devlibs.EnumClass.PostDataType
import com.hill.devlibs.model.bean.FormDataBody
import java.io.File

/**
 * Created by Hill on 2019/7/1
 */
class PostImgBody :FormDataBody(), IRequestApi {
    override fun requestApi(): EkiApi=EkiApi.PostImg

    fun setIcon(file: File): PostImgBody {
        dataPair["icon"]?.file=file
        return this
    }

    override val setDataPair: LinkedHashMap<String, IFormDataSet> =
            LinkedHashMap<String,IFormDataSet>().apply {
                put("icon",object :IFormDataSet(){
                    override val type: PostDataType
                        get() = PostDataType.JPG
                })
            }

    override fun clear() {

    }

    override fun printValue() {

    }
}