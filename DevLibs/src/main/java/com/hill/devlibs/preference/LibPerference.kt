package com.hill.devlibs.preference

import android.content.Context
import android.content.SharedPreferences
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2019/6/20
 */
abstract class LibPerference<P:LibPerference<P>>(var context: Context?) {

    companion object {
        const val MODE_PRIVATE=Context.MODE_PRIVATE
    }

    var setting: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    fun creat(name: String): P? {
        setting = context?.getSharedPreferences(name, MODE_PRIVATE)
        Log.d("Perference->$name  `is`->$setting")
        editor = setting?.edit()
        return this as P
    }
}