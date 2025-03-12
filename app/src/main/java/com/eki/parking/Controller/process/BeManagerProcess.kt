package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.BeManagerBody
import com.eki.parking.Model.response.BeManagerResponse
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.sqlData
import com.eki.parking.extension.sqlSaveOrUpdate

/**
 * Created by Hill on 2020/04/22
 */
abstract class BeManagerProcess(from: Context? = null) : BaseProcess(from),
                                                BaseProcess.ISetRequestBody<BeManagerBody>{
    var onSuccess:(ResponseInfo.Bank)->Unit={}
    var onFail={}

    override val request: EkiRequest<BeManagerBody>
        get() = EkiRequest.creatBy(body)

    override fun run() {

        request.sendRequest(from, showProgress = false,
                listener = object : OnResponseListener<BeManagerResponse> {
                    override fun onReTry() {
                        onFail()
                    }

                    override fun onFail(errorMsg: String, code: String) {
                        onFail()
                    }

                    override fun onTaskPostExecute(result: BeManagerResponse) {

                        var data=result.info
                        data.toSql().sqlSaveOrUpdate()
                        sqlData<EkiMember>()?.apply {beManager=true}?.sqlSaveOrUpdate()
                        onSuccess(data)
                    }
                }, showErrorDialog = true)
    }


}