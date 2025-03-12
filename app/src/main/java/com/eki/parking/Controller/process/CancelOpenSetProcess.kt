package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.CancelOpenSetBody
import com.eki.parking.Model.response.CancelOpenSetResponse
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.extension.sendRequest
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/03/27
 */
abstract class CancelOpenSetProcess(from: Context? = null) : BaseProcess(from),
    BaseProcess.ISetRequestBody<CancelOpenSetBody>{

    var onEditSuccess:(ManagerLocation,List<ResponseInfo.Mulct>)->Unit={m,list->}
    var onFail={}

    override val request: EkiRequest<CancelOpenSetBody>
        get() = EkiRequest<CancelOpenSetBody>().also { it.body=body }

    override fun run() {
        request.sendRequest(context = from,showProgress = false,
                listener =  object : OnResponseListener<CancelOpenSetResponse> {
            override fun onReTry() {
                onFail()
            }

            override fun onFail(errorMsg: String, code: String) {
                onFail()
            }

            override fun onTaskPostExecute(result: CancelOpenSetResponse) {

                onEditSuccess(result.info.Location.apply{time= DateTime()},result.info.Mulcts)
            }

        },showErrorDialog = true)
    }
}