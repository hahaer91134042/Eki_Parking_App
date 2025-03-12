package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.listener.OnSqlAsyncExecute
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.LoadOrderResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.extension.*

/**
 * Created by Hill on 2019/12/09
 */

open class LoadOrderProcess(from: Context?): BaseProcess(from),
                                             BaseProcess.ISetRequestBody<SendIdBody>,
                                             BaseProcess.ISetProcessBack{

    var onLocation:(ArrayList<EkiOrder>)->Unit={}
    var retryProcess={}
    var onFail={}

    override val body: SendIdBody
        get() = SendIdBody(EkiApi.LoadOrder)
    override val request: EkiRequest<SendIdBody>
        get() = EkiRequest<SendIdBody>().also { it.body=body }

    override fun onProcessOver() {}

    override fun run() {
        request.sendRequest(from,false, listener = object :OnResponseListener<LoadOrderResponse>{
            override fun onFail(errorMsg: String, code: String) {
                onOver()
                onFail()
            }
            override fun onTaskPostExecute(result: LoadOrderResponse) {
                val list = result.info.toSqlList()
                //println("取得Api Order1 " + list.size)
                sqlSaveAsync(list, object : OnSqlAsyncExecute {
                    override fun onFail() {
                        onOver()
                        this@LoadOrderProcess.onFail()
                    }

                    override fun onSuccess() {
                        onOver()
                        onLocation.invoke(list)
                    }
                })
            }

            override fun onReTry() {
                retryProcess.invoke()
            }

        },showErrorDialog = true)

    }
}