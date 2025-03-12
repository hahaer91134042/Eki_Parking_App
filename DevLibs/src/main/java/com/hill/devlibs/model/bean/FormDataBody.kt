package com.hill.devlibs.model.bean

import com.hill.devlibs.EnumClass.PostDataType
import java.io.File

/**
 * Created by Hill on 2019/7/1
 */
abstract class FormDataBody :ServerRequestBody(){
    val dataPair:LinkedHashMap<String,IFormDataSet> by lazy { setDataPair }

    abstract val setDataPair: LinkedHashMap<String,IFormDataSet>

    abstract class IFormDataSet{
        abstract val type:PostDataType
        var file:File?=null
        var text=""
    }

    override fun clear() {

    }

    override fun printValue() =printObj(this)
}