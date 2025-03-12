package com.hill.devlibs.encryption

import android.util.Base64
import com.hill.devlibs.EnumClass.CipherMode
import com.hill.devlibs.extension.decodeBase64
import com.hill.devlibs.extension.toBase64
import com.hill.devlibs.tools.Log
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Hill on 2019/7/31
 */
class AESCryption{

    var key:ByteArray?=null
    var hashIV:ByteArray?=null
    var mode=CipherMode.CBC

    fun encodeToBase64(data:String):String?{
        return encrypt(data.toByteArray(),key,hashIV)?.toBase64()
    }

    fun decodeToString(cipher:String):String?{
        return decrypt(cipher.decodeBase64(),key,hashIV)?.toString(Charsets.UTF_8)
//        var utf8=cipher.base64ToUTF8()
//        Log.w("64->UTF8  =>$utf8   222->${cipher.decodeBase64()}" )
//        return decrypt(Base64.decode(cipher,Base64.NO_WRAP),key,hashIV)?.toString(Charsets.UTF_8)
    }

    private fun encrypt(data: ByteArray, key: ByteArray?, ivs: ByteArray?): ByteArray? {
        try {
//            val ivSpec = IvParameterSpec(ivs)
//            val newKey = SecretKeySpec(key, "AES")
//            val cipher = Cipher.getInstance("AES/$mode/PKCS5Padding")
//            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
//            return cipher.doFinal(data)

            val cipher = Cipher.getInstance("AES/$mode/PKCS5Padding")
            val secretKeySpec = SecretKeySpec(key, "AES")
            val finalIvs = ByteArray(16)
//            Log.e("IV length->${ivs?.size}")
            val len = if (ivs?.size!! > 16) 16 else ivs?.size
            System.arraycopy(ivs, 0, finalIvs, 0, len!!)
            val ivps = IvParameterSpec(finalIvs)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps)
            return cipher.doFinal(data)
        } catch (e: Exception) {
            throw e
        }
        return null
    }

    private fun decrypt(data: ByteArray, key: ByteArray?, ivs: ByteArray?): ByteArray? {
        try {
            val cipher = Cipher.getInstance("AES/$mode/PKCS5Padding")
            val secretKeySpec = SecretKeySpec(key, "AES")
            val finalIvs = ByteArray(16)
            val len = if (ivs?.size!! > 16) 16 else ivs?.size
//            Log.d("decode len->$len")
            System.arraycopy(ivs, 0, finalIvs, 0, len)
            val ivps = IvParameterSpec(finalIvs)
//            Log.d("decode ivps->$ivps")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivps)
//            Log.d("decode cipher->$cipher")
            return cipher.doFinal(data)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return null
    }
}