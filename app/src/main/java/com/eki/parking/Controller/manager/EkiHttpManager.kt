package com.eki.parking.Controller.manager

import android.content.Context

import com.eki.parking.R
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.abs.EkiRequestBody
import com.hill.devlibs.manager.LibHttpManager
import com.hill.devlibs.model.bean.ServerRequest

import java.io.IOException

import androidx.collection.ArrayMap
import com.eki.parking.AppConfig
import com.hill.devlibs.impl.IHttpsSet


class EkiHttpManager(context: Context) : LibHttpManager<EkiRequest<*>, EkiRequestBody>(context) {

    private val errorMsg: Array<String> = getStringArr(R.array.responseMsg)

//    companion object{
//        @JvmStatic
//        val httpStateCode: Int
//            get() =HTTP_STATUS_CODE
//    }


    override fun setConnectSetting(): HttpConnectSetting {
        return HttpConnectSetting(
                30,
                30,
                30
        )
    }

    @Throws(IOException::class)
    override fun sendGetRequest(serverRequest: EkiRequest<*>): String {
        return super.sendGetRequest(serverRequest)
    }


    @Throws(IOException::class)
    override fun sendGetRequest(url: String, pairs: ArrayMap<String, String>): String {
        return super.sendGetRequest(url, pairs)
    }

    @Throws(IOException::class)
    override fun sendPostRequest(url: String, pairs: ArrayMap<String, String>): String {
        return super.sendPostRequest(url, pairs)
    }

    @Throws(IOException::class)
    override fun sendPostRequest(urlStr: String, requestBody: EkiRequestBody): String {
        return super.sendPostRequest(urlStr, requestBody)
    }

    @Throws(IOException::class)
    override fun sendPostRequest(request: ServerRequest<*>): String {
        return super.sendPostRequest(request)
    }

//    override fun setKeyStoreRes(): Int {
//        return R.raw.ppyp_cer
//    }



    override fun httpsSet(): IHttpsSet =AppConfig.HttpSet.ppyp

}
