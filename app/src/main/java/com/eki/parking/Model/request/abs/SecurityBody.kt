package com.eki.parking.Model.request.abs

import com.hill.devlibs.impl.IEncodeSet

/**
 * Created by Hill on 2019/7/31
 */
abstract class SecurityBody<T>(data:T) : EkiRequestBody(),IEncodeSet {
    var key:String
    var content:String

    init {
        val crypto=encoder().encode(data)
        key=crypto.publicKey
        content=crypto.cipher
    }


//    protected fun encodeByAes(source:String):String{
//        var hash="$key${AppConfig.privateKey}".toSHA1()
//        var aesCryption=AESCryption().apply {
//            key=hash.substring(0,16).toASCII().md5Digest()
//            hashIV=hash.substring(8,16).toASCII().md5Digest()
//            mode=CipherMode.CBC
//        }
//        Log.i(("hash->$hash"))
//        Log.w("hashKey->${hash.substring(0,16)} hashIV->${hash.substring(8,16)}")
//        Log.d("key md5->${hash.substring(0,16).toASCII().md5Digest()?.toBase64()}")
//        return aesCryption.encodeToBase64(source)?:""
//    }
}