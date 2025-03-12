package com.eki.parking.Controller.tools

import com.eki.parking.AppConfig
import com.hill.devlibs.EnumClass.CipherMode
import com.hill.devlibs.encryption.AESCryption
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.IEncoder

/**
 * Created by Hill on 2020/04/22
 */
object EkiEncoder{
    val AES:IEncoder=AESencoder()

    private class AESencoder:IEncoder{

        override fun <T> decode(clazz: Class<T>, cipher: String, key: String): T? {
            var hash="$key${AppConfig.privateKey}".toSHA1()
            var aesCryption= AESCryption().also {
                it.key=hash.substring(0,16).toASCII().md5Digest()
                it.hashIV=hash.substring(8,16).toASCII().md5Digest()
                it.mode= CipherMode.CBC
            }
            return aesCryption.decodeToString(cipher)?.toObj(clazz)
        }

        override fun <T> encode(data: T): IEncoder.CryptoContent {
            var key= randomStr(7)
            var hash="$key${AppConfig.privateKey}".toSHA1()
            var aesCryption= AESCryption().also {
                it.key=hash.substring(0,16).toASCII().md5Digest()
                it.hashIV=hash.substring(8,16).toASCII().md5Digest()
                it.mode= CipherMode.CBC
            }
//            Log.i(("hash->$hash"))
//            Log.w("hashKey->${hash.substring(0,16)} hashIV->${hash.substring(8,16)}")
//            Log.d("key md5->${hash.substring(0,16).toASCII().md5Digest()?.toBase64()}")

            return IEncoder.CryptoContent().also {
                it.publicKey=key
                it.cipher=aesCryption.encodeToBase64(data.toJsonStr())?:""
            }
        }
    }
}