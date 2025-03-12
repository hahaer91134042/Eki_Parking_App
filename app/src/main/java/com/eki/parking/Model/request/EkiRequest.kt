package com.eki.parking.Model.request

import com.eki.parking.AppConfig
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.impl.IEkiSecretHeader
import com.hill.devlibs.annotation.parse.ApiParser
import com.hill.devlibs.model.bean.ServerRequest
import com.hill.devlibs.model.bean.ServerRequestBody
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.SecurityUtil
import com.hill.devlibs.util.StringUtil

/**
 * Created by Hill on 2019/4/24
 */
open class EkiRequest<BODY:ServerRequestBody> :ServerRequest<BODY>(){
    companion object {
        fun <T:ServerRequestBody> creatBy(body:T):EkiRequest<T> =EkiRequest<T>().also { it.body=body }
    }

    var api:EkiApi?=null
    set(value) {
        apiConfig=ApiParser.parse(value)
        url=apiConfig?.serverUrl+apiConfig?.pathStr
        field=value
    }

    init {
        scheme= AppConfig.EkiScheme
    }

    fun secretHeader(){

        var secret=StringUtil.randomString(8)
        headerPair["secret"] = secret

        var encodeStr=scheme+secret+AppConfig.privateKey

        var hashStr=SecurityUtil.sha256Encrypt(encodeStr).replace("-","").toUpperCase()

        headerPair["token"]=hashStr

        //Log.i("secret->$secret encodeStr->$encodeStr hashStr->$hashStr")
    }

    override fun printValue() {
        super.printValue()
    }

    override fun clear() {
    }
}