package com.eki.parking.Model.impl

import com.google.gson.JsonDeserializer

/**
 * Created by Hill on 2020/06/16
 */
interface IJsonDeserial<T>{
    fun jsonDeserializer():JsonDeserializer<T>
}