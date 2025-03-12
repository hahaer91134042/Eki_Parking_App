package com.eki.parking.Controller.asynctask.task.server

//import com.eki.parking.model.response.EkiResponse
import android.content.Context
import com.eki.parking.Controller.asynctask.abs.ObjResultTask
import com.eki.parking.Controller.dialog.MsgDialog
import com.eki.parking.Controller.listener.OnResponseListener
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
import com.hill.devlibs.extension.isTrue
import com.hill.devlibs.listener.OnMsgDialogBtnListener
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2018/10/26.
 */
open class RequestTask<RESPONSE:ResponseVO> @JvmOverloads constructor(
        context: Context,
        var request: EkiRequest<*>,
        isDialog: Boolean=true,
        private var showErrorDialog:Boolean=true):ObjResultTask<RESPONSE,OnResponseListener<RESPONSE>?>(context,isDialog) {

//    private var executeListener:OnResponseListener<RESPONSE>?=null


//    constructor(context: Context,request: EkiRequest<*>):this(context,request,true)

    //private var parser: ApiParser = ApiParser.parse(request.api)

    init {
        if(request.body is IRequestApi){
            request.api=(request.body as IRequestApi).requestApi()
        }

    }

    override fun onPreExecute() {
        if (isDialog)
            showProgress(ProgressMode.PROCESSING_MODE)
    }

    override fun doInBackground(vararg params: Void?): RESPONSE? {


        runCatching {
            var apiConfig=request.apiConfig

            if (apiConfig.isAuth)
                request.token= sqlData<EkiMember>()?.token

            //urlStr=apiConfig?.serverUrl+apiConfig?.pathStr
            Log.w("Request url->${request.url}")
            Log.i("request bodyStr->${request.body.bodyStr}")
            Log.e("Request Api->${request.api}")

            result=when(apiConfig?.protocol){
                HttpProtocol.GET->app.httpManager.sendGetRequest(request)
                HttpProtocol.POST->app.httpManager.sendPostRequest(request)
                else -> ""
            }

//        var type=object :TypeToken<EkiResponse<RESPONSE>>(){}.type
//        var obj=JsonParser().parse(result) as JsonObject

            //var response=GsonBuilder().create().fromJson<EkiResponse<RESPONSE>>(obj,type)
            var response=ResponseVO.creat<RESPONSE>(result,apiConfig?.response)
//        response.init(result)
            Log.w("response->$response  ")
            response.printValue()

            return response
        }.onFailure {
            Log.e("Task Error->$it")
        }
        return null
    }

    override fun onPostExecute(result: RESPONSE?) {
        closeProgress()
        when (result) {
            null -> {
                showErrorDialog.isTrue {
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
                }
                listener?.onFail(getString(R.string.There_was_a_problem_with_the_network_connection),EkiErrorCode.E004.toString())
            }
            else -> {
                if (!result?.isSuccess!!){
                    runCatching {
                        var errorMsg=getString(ErrorParser.parse(result.errorCode).msgRes)
                        showErrorDialog.isTrue {
                            when(result.errorCode){
                                EkiErrorCode.E010,
                                EkiErrorCode.E011->{
                                    //要重新登入
                                    //這邊以後再做

                                }
                                else->{
                                    showMsgDialog(context, MsgDialog.MsgDialogSet().apply {
                                        msg = errorMsg
                                        title = getString(R.string.Error_Message)
                                        pBtnTex = getString(R.string.Determine)
                                    },object : OnMsgDialogBtnListener{
                                        override fun onPostiveBtn() {
                                            listener?.onReTry()
                                        }

                                        override fun onNegativeBtn() {
                                        }
                                    })
                                }
                            }
                        }
                        listener?.onFail(errorMsg,result.errorCode.toString())
                    }.onFailure {
                        listener?.onFail(getString(R.string.E004),EkiErrorCode.E004.toString())
                    }
                }else{
                    listener?.onTaskPostExecute(result)
                }
            }
        }
    }


//    override fun setExecuteListener(listener: OnResponseListener<RESPONSE>?): LibBaseAsyncTask<RESPONSE, App, OnResponseListener<RESPONSE>?> {
//        return super.setExecuteListener(listener)
//    }

    override fun setExecuteListener(listener: OnResponseListener<RESPONSE>?): RequestTask<RESPONSE> {
        super.setExecuteListener(listener)
        return this
    }

//    inline fun setExecuteListener(crossinline l:(RESPONSE?)->Unit): RequestTask<RESPONSE> {
//        //return super.setExecuteListener(listener)
//        listener=object :OnResponseListener<RESPONSE>{
//            override fun onTaskPostExecute(result: RESPONSE?)=l(result)
//        }
//
//        return this
//    }

}