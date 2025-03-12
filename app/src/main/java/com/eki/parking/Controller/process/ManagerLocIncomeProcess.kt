package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.ManagerLocIncomeResponse
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.extension.sendRequest
import com.hill.devlibs.extension.toArrayList

/**
 * Created by Hill on 2021/01/18
 */
class ManagerLocIncomeProcess(from: Context? = null,
                              private var locList:List<ManagerLocation>,
                              private var times:List<RequestBody.TimeSpan>) : BaseProcess(from),
                                                                  BaseProcess.ISetRequestBody<SendIdBody>{
    var onSuccess:(List<ManagerLocIncomeResponse.IncomeResult>)->Unit={}
    var onFail={}

    override val body: SendIdBody
        get() = SendIdBody(EkiApi.ManagerLocIncome).also { b->
            b.serNum=locList.map { loc->loc.Info?.SerialNumber?:"" }.toArrayList()
            b.times=times
        }
    override val request: EkiRequest<SendIdBody>
        get() = EkiRequest<SendIdBody>().also { it.body=body }

    override fun run() {
        request.sendRequest(context = from,showProgress = false,listener = object :OnResponseListener<ManagerLocIncomeResponse>{
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {
                onFail()
            }

            override fun onTaskPostExecute(result: ManagerLocIncomeResponse) {
                onSuccess(result.info)

            }

        },showErrorDialog = true)

    }
}