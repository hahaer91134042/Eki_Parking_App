package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.EditLocationBody
import com.eki.parking.Model.response.EditLocationResponse
import com.eki.parking.extension.sendRequest

/**
 * Created by Hill on 2020/02/26
 */
abstract class EditLocationProcess(from: Context?) : BaseProcess(from),
        BaseProcess.ISetRequestBody<EditLocationBody>,
        BaseProcess.ISetResponseListener<OnResponseListener<EditLocationResponse>>{

    override val request: EkiRequest<EditLocationBody>
        get() = EkiRequest<EditLocationBody>().also {
            it.body=body
        }

    override fun run() {
        request.sendRequest(from,showProgress = false,listener =  object :OnResponseListener<EditLocationResponse>{
            override fun onReTry() {
                onResponse?.onReTry()
            }

            override fun onFail(errorMsg: String, code: String) {
                onResponse?.onFail(errorMsg,code)
            }

            override fun onTaskPostExecute(result: EditLocationResponse) {
                onOver()
                onResponse?.onTaskPostExecute(result)
            }
        })
    }
}