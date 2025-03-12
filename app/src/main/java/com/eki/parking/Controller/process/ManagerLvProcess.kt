package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.ManagerLvInfoResponse
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.sqlSave

/**
 * Created by Hill on 2021/01/21
 */
class ManagerLvProcess(from: Context? = null) : BaseProcess(from),
                                                BaseProcess.ISetRequestBody<SendIdBody> {

    override val body: SendIdBody
        get() = SendIdBody(EkiApi.ManagerLvInfo)
    override val request: EkiRequest<SendIdBody>
        get() = EkiRequest<SendIdBody>().also { it.body=body }

    override fun run() {
        request.sendRequest(from, showProgress = false,listener =  object : OnResponseListener<ManagerLvInfoResponse>{
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {
                onOver()
            }

            override fun onTaskPostExecute(result: ManagerLvInfoResponse) {
                sqlSave(result.info)
                onOver()
            }
        },showErrorDialog = false)


    }


}