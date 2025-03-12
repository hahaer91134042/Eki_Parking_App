package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.AddOpenSetBody
import com.eki.parking.Model.response.EditOpenSetResponse
import com.eki.parking.extension.sendRequest

/**
 * Created by Hill on 2020/03/27
 */
abstract class AddOpenSetProcess(from: Context? = null) : BaseProcess(from),
    BaseProcess.ISetRequestBody<AddOpenSetBody>{

    //現在api(v2)不再回傳資料了
    var onEditSuccess:()->Unit={}
    var onFail={}


    override val request: EkiRequest<AddOpenSetBody>
        get() = EkiRequest.creatBy(body)

    override fun run() {
        request.sendRequest(
                context = from,
                showProgress = false,
                listener =  object : OnResponseListener<EditOpenSetResponse> {
            override fun onReTry() {
                onFail()
            }

            override fun onFail(errorMsg: String, code: String) {
                onFail()
                onOver()
            }

            override fun onTaskPostExecute(result: EditOpenSetResponse) {

                onEditSuccess()
                onOver()
            }

        },showErrorDialog = true)
    }
}