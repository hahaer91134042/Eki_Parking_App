package com.hill.devlibs.tools

import android.util.SparseArray


import java.util.HashMap

/**
 * Created by Hill on 2017/3/7.
 */

object Log {
    private const val tag = "Hill->"
    private const val isOpenLog = true

    var isOpenTest = false

    @JvmStatic fun d(msg: String) {
        if (isOpenLog) android.util.Log.d(tag, msg)
    }

    @JvmStatic fun <K, V> d(paramsMap: Map<out K, V>) {
        if (isOpenLog) {
            Log.d("total Key->" + paramsMap.keys)
            for (key in paramsMap.keys) {
                Log.d("Key->" + key.toString() + " Value->" + paramsMap.get(key)).toString()
            }
        }
    }

    @JvmStatic fun d(list: List<*>) {
        if (isOpenLog) {
            Log.d("total name->" + list.size)
            for (value in list) {
                Log.d("name->" + value.toString())
            }
        }
    }

    @JvmStatic fun d(list: SparseArray<*>) {
        if (isOpenLog) {
            Log.d("total name->" + list.size())
            for (i in 0 until list.size()) {
                Log.d("name->" + list.get(i).toString())
            }
        }
    }

    @JvmStatic fun d(flag: String, msg: String) {
        if (isOpenLog) android.util.Log.d(tag, "$flag->$msg")
    }

    @JvmStatic fun e(msg: String) {
        if (isOpenLog) android.util.Log.e(tag, msg)
    }

    @JvmStatic fun e(flag: String, msg: String) {
        if (isOpenLog) android.util.Log.e(tag, "$flag->$msg")
    }

    @JvmStatic fun v(msg: String) {
        if (isOpenLog) android.util.Log.v(tag, msg)
    }

    @JvmStatic fun v(flag: String, msg: String) {
        if (isOpenLog) android.util.Log.v(tag, "$flag->$msg")
    }

    @JvmStatic fun i(msg: String) {
        if (isOpenLog) android.util.Log.i(tag, msg)
    }

    @JvmStatic fun i(flag: String, msg: String) {
        if (isOpenLog) android.util.Log.i(tag, "$flag->$msg")
    }

    @JvmStatic fun w(msg: String) {
        if (isOpenLog) android.util.Log.w(tag, msg)
    }

    @JvmStatic fun w(flag: String, msg: String) {
        if (isOpenLog) android.util.Log.w(tag, "$flag->$msg")
    }


    @JvmStatic fun printConnectUrlStr(urlStr: String, paramsMap: HashMap<String, String>) {
        if (isOpenLog) {
            val connectUrl = StringBuffer("$urlStr?")
            val iterator = paramsMap.keys.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                connectUrl.append(key + "=" + paramsMap[key] + "&")
            }
            connectUrl.replace(connectUrl.length - 1, connectUrl.length, "")
            Log.d("ConnectURL->" + connectUrl.toString())
        }
    }

    @JvmStatic fun printConnectUrlStr(tag: String, urlStr: String, paramsMap: HashMap<String, String>) {
        Log.e("-----$tag-----")
        printConnectUrlStr(urlStr, paramsMap)
    }

    @JvmStatic fun largeE(tag: String, content: String) {
        if (isOpenLog){
            var content = content
            val p = 2048

            while (content.length > p) {
                val logContent = content.substring(0, p)
                content = content.replace(logContent, "")
                Log.e(tag, logContent)
            }
            Log.e(tag, content)
        }
    }

    @JvmStatic fun largeW(tag: String, content: String) {
        if (isOpenLog){
            var content = content
            val p = 2048

            while (content.length > p) {
                val logContent = content.substring(0, p)
                content = content.replace(logContent, "")
                Log.w(tag, logContent)
            }
            Log.w(tag, content)
        }
    }
}
