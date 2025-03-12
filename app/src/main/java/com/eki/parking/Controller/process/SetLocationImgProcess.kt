package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.LocationImg
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SetLocationImgBody
import com.eki.parking.Model.response.SetLocationImgResponse
import com.eki.parking.extension.sendRequest

/**
 * Created by Hill on 2020/09/26
 */
open class SetLocationImgProcess(private var imgBody: SetLocationImgBody, from: Context? = null): BaseProcess(from) {

    var onSuccess:((List<LocationImg>)->Unit)?=null
    var onFail:(()->Unit)?=null

    override fun run() {
        EkiRequest<SetLocationImgBody>().apply {
            body= imgBody
        }.sendRequest(from,false,object : OnResponseListener<SetLocationImgResponse> {
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {
                onOver()
                onFail?.invoke()
            }

            override fun onTaskPostExecute(result: SetLocationImgResponse) {
                onOver()
                onSuccess?.invoke(result.info)
            }
        })
    }
}