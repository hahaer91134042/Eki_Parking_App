package com.hill.devlibs.impl

import com.hill.devlibs.EnumClass.HttpSsl
import java.io.InputStream
import java.security.KeyStore

/**
 * Created by Hill on 2020/06/22
 */
interface IHttpsSet {
    fun hostName():String
    fun keyStore():KeyStore
    fun httpSsl():HttpSsl
    fun sslPwd():String

    //因為ASP.NET的certificate 不是一般的格式
    abstract class IAspNetHttps:IHttpsSet{
        private val keyStoreInstance: String ="PKCS12"
        override fun httpSsl(): HttpSsl =HttpSsl.TLS
        abstract fun keyRaw():InputStream

        override fun keyStore(): KeyStore =
                KeyStore.getInstance(keyStoreInstance).apply {
                    load(keyRaw(), sslPwd().toCharArray())
                }
    }
}