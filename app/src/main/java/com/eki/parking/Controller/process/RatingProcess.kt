package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.RatingBody
import com.eki.parking.Model.response.RatingResponse
import com.eki.parking.extension.sendRequest

/**
 * Created by Hill on 2020/05/21
 */
abstract class RatingProcess(from: Context? = null) : BaseProcess(from),BaseProcess.ISetRequestBody<RatingBody>{

    var onSuccess={}
    var onFail={}

    override val request: EkiRequest<RatingBody>
        get() = EkiRequest.creatBy(body)

    override fun run() {
        request.sendRequest(context = from,showProgress = true,listener = object :OnResponseListener<RatingResponse>{
            override fun onReTry() {
                onFail()
            }

            override fun onFail(errorMsg: String, code: String) {
                onFail()
            }

            override fun onTaskPostExecute(result: RatingResponse) {
                onSuccess()
            }
        },showErrorDialog = true)
    }
}