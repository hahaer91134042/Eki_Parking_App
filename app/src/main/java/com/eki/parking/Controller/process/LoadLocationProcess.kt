package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.asynctask.task.server.MultiPageRequestTask
import com.eki.parking.Controller.listener.OnMultiPageResponseBack
import com.eki.parking.Controller.listener.OnSqlAsyncExecute
import com.eki.parking.Model.DTO.MultiPageResponse
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.LoadLocationBody
import com.eki.parking.Model.response.LoadLocationResonse
import com.eki.parking.Model.sql.EkiLocation
import com.hill.devlibs.extension.notNull

/**
 * Created by Hill on 2019/12/09
 */

abstract class LoadLocationProcess @JvmOverloads constructor(from: Context?,private val isDialog:Boolean=false) : BaseProcess(from),
        BaseProcess.ISetRequestBody<LoadLocationBody>,
        BaseProcess.ISetResponseListener<OnMultiPageResponseBack<LoadLocationResonse>>{

    override val request: EkiRequest<LoadLocationBody>
        get() = EkiRequest<LoadLocationBody>().also { it.body = body }

    override fun run() {
        from.notNull { context ->
            MultiPageRequestTask<LoadLocationResonse>(context,request,isDialog)
                    .setExecuteListener(object :OnMultiPageResponseBack<LoadLocationResonse>{
                        override fun onReTry() {
                            onResponse?.onReTry()
                        }

                        override fun onFail(errorMsg: String, code: String) {
                            onResponse?.onFail(errorMsg,code)
                        }

                        override fun onTaskPostExecute(result: ArrayList<MultiPageResponse<LoadLocationResonse>>) {
                            //                            startActivitySwitchAnim(MainActivity.class);
                            var list = ArrayList<EkiLocation>()
                            result.forEach {
                                list.addAll(it.response.info!!.List)
                            }
//                            list = addLocationV2(list) // 新增 Socket

                            sqlManager.saveAsync(list, EkiLocation::class.java, object : OnSqlAsyncExecute {
                                override fun onFail() {}
                                override fun onSuccess() {
//                                    i("On Save Async Success")
//                                    isProcessOver = true
                                    onOver()
                                    onResponse?.onTaskPostExecute(result)
                                }
                            })
                        }
                    }).start()
        }
    }
}