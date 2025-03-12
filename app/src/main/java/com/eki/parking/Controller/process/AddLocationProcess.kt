package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.AddLocationBody
import com.eki.parking.Model.response.AddLocationResponse
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/04/16
 */
abstract class AddLocationProcess(from: Context? = null) : BaseProcess(from),
        BaseProcess.ISetRequestBody<AddLocationBody>{

    var onFail={}
    var onAddSuccess:(ManagerLocation)->Unit={}

    override val request: EkiRequest<AddLocationBody>
        get() = EkiRequest.creatBy(body)

    override fun run() {
        request.sendRequest(from,showProgress = false,
        listener = object :OnResponseListener<AddLocationResponse>{
            override fun onReTry() {
                onFail()
            }

            override fun onFail(errorMsg: String, code: String) {
                onFail()
            }

            override fun onTaskPostExecute(result: AddLocationResponse) {
                val loc=result.info.apply{time= DateTime() }
                loc.sqlSaveOrUpdate()
                onAddSuccess(loc)
            }
        },showErrorDialog = true)
    }
}