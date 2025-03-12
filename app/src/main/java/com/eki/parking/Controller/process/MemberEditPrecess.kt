package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.MemberEditBody
import com.eki.parking.Model.response.MemberEditResponse
import com.eki.parking.extension.sendRequest

/**
 * Created by Hill on 2020/06/01
 */
abstract class MemberEditPrecess(from: Context? = null) : BaseProcess(from),
                                                          BaseProcess.ISetRequestBody<MemberEditBody> {

    var onFail={}
    var onSuccess={}

    override val request: EkiRequest<MemberEditBody>
        get() = EkiRequest.creatBy(body)

    override fun run() {
        request.sendRequest(from,showProgress = true,
                listener =object:OnResponseListener<MemberEditResponse>{
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {
                onFail()
            }

            override fun onTaskPostExecute(result: MemberEditResponse) {
                onSuccess()
            }
        },showErrorDialog = true)
    }


}