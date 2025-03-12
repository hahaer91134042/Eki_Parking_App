package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.listener.OnSqlAsyncExecute
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.ManagerLocationResponse
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.extension.*
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/02/20
 */

abstract class ManagerLocationProcess @JvmOverloads constructor(from: Context?, private var now:DateTime, private var isClear:Boolean=false) : BaseProcess(from),
        BaseProcess.ISetRequestBody<SendIdBody>{
    override val body: SendIdBody
        get() = SendIdBody(EkiApi.ManagerGetLocation).apply { time=now.toString() }
    override val request: EkiRequest<SendIdBody>
        get() = EkiRequest<SendIdBody>().also { it.body=body }

    var onLocation:(ArrayList<ManagerLocation>)->Unit={}
    var retryProcess={}
    var onFail={}

    override fun run() {
        Log.w("ManagerLocation time->${body.time}")
        if (!sqlHasData<ManagerLocation>()||isClear){
            loadLocationFromServer()
        }else{
            sqlDataListAsync<ManagerLocation>{l->
                var list=l.filter { it.time.year==now.year&&it.time.month==now.month }
                if (list.isNotEmpty()){
                    onOver()
                    onLocation.invoke(list.toArrayList())
                }else{
                    loadLocationFromServer()
                }
            }
        }
    }


    private fun loadLocationFromServer(){
        request.sendRequest(from,showProgress = false,listener = object :OnResponseListener<ManagerLocationResponse>{
            override fun onReTry() {
//                    onResponse?.onReTry()
                retryProcess.invoke()
            }

            override fun onFail(errorMsg: String, code: String) {
//                    onResponse?.onFail(errorMsg,code)
                onOver()
                onFail()
            }

            override fun onTaskPostExecute(result: ManagerLocationResponse) {
                var list= result.info
                list.forEach { it.time=now }

                if (isClear)
                    sqlClean<ManagerLocation>()

                sqlSaveAsync(list,object :OnSqlAsyncExecute{
                    override fun onFail() {

                    }

                    override fun onSuccess() {
//                        isProcessOver=true
                        onOver()
//                            onResponse?.onTaskPostExecute(result)
                        onLocation.invoke(list)
                    }
                })
            }
        },showErrorDialog = true)
    }
}