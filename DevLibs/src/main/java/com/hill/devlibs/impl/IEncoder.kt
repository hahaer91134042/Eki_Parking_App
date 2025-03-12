package com.hill.devlibs.impl

/**
 * Created by Hill on 2020/04/22
 */
interface IEncoder {
    fun <T> decode(clazz: Class<T>,cipher:String,key:String=""):T?
    fun <T> encode(data:T):CryptoContent

    class CryptoContent{
        var publicKey=""
        var cipher=""
    }
}