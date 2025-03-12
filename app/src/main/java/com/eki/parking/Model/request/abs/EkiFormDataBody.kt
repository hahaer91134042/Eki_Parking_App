package com.eki.parking.Model.request.abs

import com.eki.parking.Model.impl.IRequestApi
import com.hill.devlibs.EnumClass.PostDataType
import com.hill.devlibs.model.bean.FormDataBody
import java.io.File

/**
 * Created by Hill on 2020/09/26
 */
abstract class EkiFormDataBody:FormDataBody() {
    companion object Flag{
        const val img="img"
        const val info="info"
        const val icon="icon"
    }

    interface ImgData{
        fun setImg(pic:File)
    }
    interface IconDate{
        fun setIcon(pic:File)
    }
    interface InfoDate<T>{
        fun setInfo(info:T)
    }

//    abstract fun setFormDataPair():PairList

//    data class FormSetPair(var flag:String,var type:PostDataType)
//    class PairList(vararg pairs:FormSetPair):ArrayList<FormSetPair>(){
//        init {
//            addAll(pairs)
//        }
//    }

    override val setDataPair: LinkedHashMap<String, IFormDataSet>
        get() = LinkedHashMap<String,IFormDataSet>().also {
            if (this is ImgData)
                it[img] = object :IFormDataSet(){
                    override val type: PostDataType
                        get() = PostDataType.JPG
                }
            if (this is IconDate)
                it[icon] = object :IFormDataSet(){
                    override val type: PostDataType
                        get() = PostDataType.JPG
                }

            if (this is InfoDate<*>)
                it[info] = object :IFormDataSet(){
                    override val type: PostDataType
                        get() = PostDataType.TEXT
                }
        }

}