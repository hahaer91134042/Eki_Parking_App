package com.hill.devlibs.tools

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer

/**
 * Created by Hill on 2019/12/25
 */
object AppGson {
    @JvmStatic
    fun creat():Gson =
            GsonBuilder().addSerializationExclusionStrategy(GsonExclusion()).create()
    @JvmStatic
    fun creat(clazz: Class<*>,deserializer: JsonDeserializer<*>):Gson=
            GsonBuilder().registerTypeAdapter(clazz,deserializer)
                    .addSerializationExclusionStrategy(GsonExclusion()).create()
}