package com.hill.devlibs.model

import android.os.Bundle
import com.hill.devlibs.annotation.GsonSkip
import com.hill.devlibs.extension.getValue
import com.hill.devlibs.tools.ContentPrinter
import com.hill.devlibs.tools.Log


import org.json.JSONArray
import org.json.JSONObject

import java.io.Serializable

/**
 * Created by Hill on 2017/9/14.
 */

abstract class ValueObject : Serializable {
    //<VO : ValueObject<VO>>
    @GsonSkip
    @JvmField
    protected var TAG: String = javaClass.simpleName


//    var bundle: Bundle?
//        get() = null
//        set(bundle) {}

    open fun init(arr: JSONArray): Boolean {
        return false
    }

    open fun init(obj: JSONObject): Boolean {
        return false
    }

    open fun init(input: String): Boolean {
        return false
    }

    open fun getBundle(): Bundle? {
        return null
    }
    open fun setBundle(bundle: Bundle){
    }

//    open fun getContainer(): ValueObjContainer<VO>? {
//        return null
//    }
    open fun getJSONObj(): JSONObject? {
        return null
    }
    open fun isEmpty():Boolean{
        return true
    }

    abstract fun clear()
    abstract fun printValue()

    fun <vo:ValueObject> printListValue(vararg lists: List<vo>) {
        for (list in lists) {
            for (vo in list) {
                vo.printValue()
            }
        }
    }

    fun  printListData(vararg lists: List<*>) {
        for (list in lists){
            for (data in list){
                Log.i("body->$data")
                if (data is ValueObject)
                    data.printValue()
            }
        }
    }

    fun <K:ValueObject>printMapValue(list: Map<*,K>) {
        try {
            if (list.isNotEmpty()) {
                Log.w("----$TAG----")
                for ((key, value) in list) {
                    Log.i("Map Key->" + key.toString() + " name->" + value.printValue())
                }
                Log.w("----Print Value End----")
            } else {
                Log.e("Map List has not name")
            }
        } catch (e: Exception) {
            printException(e)
        }

    }

    protected fun printContentValue(contents: ContentPrinter) {
        val keys = contents.keys
        val values = contents.values

        if (keys.size == values.size) {
            Log.w("----$TAG----")
            for (i in keys.indices) {
                if (values[i]!=null)
                    Log.i(keys[i], values[i].toString())
                else
                    Log.i(keys[i], null.toString())
            }
            Log.w("----Print Value End----")
        } else {
            Log.e("Key Value none paire!")
        }
    }

    protected fun printException(which: String, e: Exception) {
        Log.e("-----$which-----")
        printException(e)
    }

    protected fun printException(e: Exception) {
        Log.e(TAG, e.toString())
    }

    protected fun printObj(obj:Any){
        var clazz=obj.javaClass
        var fields=clazz.declaredFields
        var content= ContentPrinter()

        fields.forEach {
            try {
                content.addKey(it.name)
                        .addValue(it.getValue(obj))
            }catch (e:Exception){
                Log.e("Field name->${it.name} cant print")
                content.addValue("null")
            }
        }
        printContentValue(content)
    }

}
