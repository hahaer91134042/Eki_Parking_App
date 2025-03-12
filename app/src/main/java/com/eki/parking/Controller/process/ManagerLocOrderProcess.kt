package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.LocationOrderResponse
import com.eki.parking.extension.sendRequest

/**
 * Created by Hill on 2020/05/11
 */
abstract class ManagerLocOrderProcess(from: Context? = null) : BaseProcess(from),
        BaseProcess.ISetRequestBody<SendIdBody>{

    var onSuccess:(ArrayList<ResponseInfo.ManagerLocOrder>)->Unit={}
    var onFail={}

    override val request: EkiRequest<SendIdBody>
        get() = EkiRequest<SendIdBody>().also { it.body=body }
    override fun run() {
        request.sendRequest(context = from,showProgress = false,listener =  object:OnResponseListener<LocationOrderResponse>{
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {
                onFail()
            }

            override fun onTaskPostExecute(result: LocationOrderResponse) {
                onSuccess(result.info)
            }
        },showErrorDialog = true)

    }


}