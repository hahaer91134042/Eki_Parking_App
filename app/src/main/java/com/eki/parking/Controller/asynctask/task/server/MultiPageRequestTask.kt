package com.eki.parking.Controller.asynctask.task.server

import android.content.Context
import com.eki.parking.Controller.asynctask.abs.BaseAsyncTask
import com.eki.parking.Controller.dialog.MsgDialog
import com.eki.parking.Controller.impl.ILoadMultiPage
import com.eki.parking.Controller.impl.IMultiPageConfig
import com.eki.parking.Controller.listener.OnMultiPageResponseBack
import com.eki.parking.Model.DTO.MultiPageResponse
import com.eki.parking.Model.EnumClass.EkiErrorCode
import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.impl.IRequestApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.extension.showMsgDialog
import com.eki.parking.extension.sqlData
import com.hill.devlibs.EnumClass.HttpProtocol
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.annotation.parse.ErrorParser
import com.hill.devlibs.listener.OnMsgDialogBtnListener
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 07,10,2019
 */
class MultiPageRequestTask<RESPONSE:ResponseVO>(
        context: Context,
        var request: EkiRequest<*>,
        isDialog: Boolean) : BaseAsyncTask<ArrayList<MultiPageResponse<RESPONSE>>,
                                           OnMultiPageResponseBack<RESPONSE>?>(context,isDialog) {

    constructor(context: Context,request: EkiRequest<*>):this(context,request,false)

//    private var executeListener: OnMultiPageResponseBack<RESPONSE>?=null

    init {
        if(request.body is IRequestApi){
            request.api=(request.body as IRequestApi).requestApi()
        }

    }


    override fun onPreExecute() {
        if (isDialog)
            showProgress(ProgressMode.PROCESSING_MODE)

    }

    override fun doInBackground(vararg params: Void?): ArrayList<MultiPageResponse<RESPONSE>>? {

        runCatching {
            var apiConfig=request.apiConfig
            if (apiConfig.isAuth)
                request.token= sqlData<EkiMember>()?.token

            var list=ArrayList<MultiPageResponse<RESPONSE>>()

            var page=1
            
            do {
                var response=getResponse(page)

                if (response is ILoadMultiPage){
//                Log.i("Response ${request::class.java.simpleName} is ${ILoadMultiPage::class.java.simpleName}")
                    Log.d("Total->${response.total()}  Page->${response.page()}")

                    list.add(MultiPageResponse(page,response))

                    if (page<response.total())
                        page++
                    else
                        break
                }else
                    throw IllegalArgumentException("${response::class.java.simpleName} Must Implement ${ILoadMultiPage::class.java.simpleName}")

            }while (true)

            Log.d("Response List size->${list.size}")

            return list
        }
        return null
    }

    override fun onPostExecute(result: ArrayList<MultiPageResponse<RESPONSE>>?) {
        closeProgress()
        when(result){
            null->{
                showMsgDialog(context, MsgDialog.MsgDialogSet().apply {
                    msg = getString(R.string.There_was_a_problem_with_the_network_connection)
                    title = getString(R.string.Error_Message)
                    pBtnTex = getString(R.string.Determine)
                    nBtnTex = getString(R.string.cancel)
                },object : OnMsgDialogBtnListener{
                    override fun onPostiveBtn() {
                        listener?.onReTry()
                    }

                    override fun onNegativeBtn() {
                    }
                })
                listener?.onFail(getString(R.string.There_was_a_problem_with_the_network_connection),EkiErrorCode.E004.toString())
            }
            else->{
                var errorList= ArrayList<EkiErrorCode>()
                result?.forEach {
                    if(it.response.errorCode!=EkiErrorCode.E000)
                        errorList.add(it.response.errorCode)
                }


                if (errorList.size>0){
                    var eMsg=StringBuilder()
                    errorList.forEach {
                        eMsg.append("\n"+getString(ErrorParser.parse(it).msgRes))
                    }
                    var errorMsg=eMsg.toString()
                    showMsgDialog(context, MsgDialog.MsgDialogSet().apply {
                        msg = errorMsg
                        title = getString(R.string.Error_Message)
                        pBtnTex = getString(R.string.Determine)
                    },object:OnMsgDialogBtnListener{
                        override fun onPostiveBtn() {
                            listener?.onReTry()
                        }
                        override fun onNegativeBtn() {

                        }
                    })
                    listener?.onFail(errorMsg,EkiErrorCode.E004.toString())
                }else{
                    listener?.onTaskPostExecute(result)
                }
            }
        }
    }

    private fun getResponse(page:Int):RESPONSE{
        var apiConfig=request.apiConfig
        //urlStr=apiConfig?.serverUrl+apiConfig?.pathStr
        request.body.also {
            if (it is IMultiPageConfig){
//                Log.w("Field ${it::class.java.simpleName} is ${IMultiPageConfig::class.java.simpleName}")
                it.page(page)
            }else
                throw IllegalArgumentException("${it::class.java.simpleName} Must Implement ${IMultiPageConfig::class.java.simpleName}")
        }

        Log.w("Request url->${request.url}")
        Log.i("request bodyStr->${request.body.bodyStr}")
        Log.e("Request Api->${request.api}")
//        var body=request.body
//        if (body is IMultiPageConfig){
//            Log.w("Field ${request.body::class.java.simpleName} is IMultiPageConfig")
//            body.page(page)
//        }

        result=when(apiConfig?.protocol){
            HttpProtocol.GET->app.httpManager.sendGetRequest(request)
            HttpProtocol.POST->app.httpManager.sendPostRequest(request)
            else -> ""
        }

        var response=ResponseVO.creat<RESPONSE>(result,apiConfig?.response?:null)
//        response.init(result)
//        Log.w("response->$response  ")
//        response.printValue()
        return response
    }

    override fun setExecuteListener(listener: OnMultiPageResponseBack<RESPONSE>?): MultiPageRequestTask<RESPONSE> {
        super.setExecuteListener(listener)
        return this
    }

//    override fun setExecuteListener(listener: OnPostExecuteListener<ArrayList<MultiPageResponse<RESPONSE>>>?): MultiPageRequestTask<RESPONSE> {
//        executeListener=listener as OnMultiPageResponseBack<RESPONSE>
//        return this
//    }
}