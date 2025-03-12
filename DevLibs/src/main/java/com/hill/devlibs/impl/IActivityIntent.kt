package com.hill.devlibs.impl

import android.content.Context
import android.content.Intent
import com.hill.devlibs.extension.isNotEmpty
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.list.DataList
import com.hill.devlibs.model.ValueObjContainer

/**
 * Created by Hill on 2020/02/21
 */
abstract class IActivityIntent(private val from:Context) {
    interface ForResultBack{
        val requestCode:Int
    }

    interface DataPair{
        val flag:String
        val data:ValueObjContainer<*>
    }

    abstract val target:Class<*>
    open val action=""
    open val dataPair: DataPair?=null
    open val dataList=DataList<DataPair>()//多個再用

    val intent: Intent by lazy {
        Intent().also {intent->

            intent.setClass(from, target)
            action.isNotEmpty { intent.action = it }
            dataPair.notNull { intent.putExtra(it.flag,it.data) }
            dataList.forEach { intent.putExtra(it.flag,it.data) }
            onIntent(intent)
        }
    }

    open fun onIntent(intent:Intent){

    }

}