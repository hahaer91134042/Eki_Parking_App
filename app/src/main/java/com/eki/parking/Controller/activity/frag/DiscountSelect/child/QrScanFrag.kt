package com.eki.parking.Controller.activity.frag.DiscountSelect.child

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.frag.ChildFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.ActionScan
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.ActionResponse
import com.eki.parking.databinding.FragQrScanBinding
import com.eki.parking.extension.sendRequest
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.extension.toObj
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.tools.Log
import com.king.zxing.CaptureHelper
import com.king.zxing.OnCaptureCallback

/**
 * Created by Hill on 2020/08/25
 */
class QrScanFrag : ChildFrag(), OnCaptureCallback, IFragViewBinding {

    private lateinit var binding: FragQrScanBinding
    private lateinit var scanHelper: CaptureHelper

    override fun initFragView() {
        scanHelper = CaptureHelper(activity, binding.surfaceView, binding.viewfinderView).also {
            it.setOnCaptureCallback(this)
            it.vibrate(true)
                .fullScreenScan(true)//這樣掃描起來比較不會出錯
//                    .supportLuminanceInvert(true)
                .continuousScan(false)
        }

        scanHelper.onCreate()
    }

    override fun onResume() {
        super.onResume()
        scanHelper.onResume()
    }

    override fun onPause() {
        super.onPause()
        scanHelper.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        scanHelper.onDestroy()
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragQrScanBinding.inflate(inflater, container, false)
        return binding
    }

    override fun onResultCallback(result: String?): Boolean {
        try {
            var action = result?.toObj<ActionScan>()!!
//            sendBroadcast(Intent(AppFlag.ActionSelect).apply { putExtra(AppFlag.DATA_FLAG,action) })

            checkActionCode(action)

        } catch (e: Exception) {
            showToast("活動碼錯誤!")
            toMainActivity()
        }
        Log.w("QR code->$result")


        return true
    }

    private fun checkActionCode(action: ActionScan) {
        if (action.Limit) {
            var now = DateTime.now()
            var start = action.Start.toDateTime()
            var end = action.End.toDateTime()
            if (now < start || now > end) {
                showToast("活動時間錯誤!")
                toMainActivity()
                return
            }
        }

        EkiRequest<SendIdBody>().also {
            it.body = SendIdBody(EkiApi.LoadAction).apply {
                serNum.add(action.Code)
            }
        }.sendRequest(
            context,
            showProgress = true,
            listener = object : OnResponseListener<ActionResponse> {
                override fun onReTry() {

                }

                override fun onFail(errorMsg: String, code: String) {

                    toMainActivity()
                }

                override fun onTaskPostExecute(result: ActionResponse) {

                    if (result.info.any { it.Serial == action.Code }) {
                        var rAction = result.info.first { it.Serial == action.Code }
                        sendBroadcast(Intent(AppFlag.ActionSelect).apply {
                            putExtra(
                                AppFlag.DATA_FLAG,
                                rAction
                            )
                        })
                    } else {
                        showToast("活動碼錯誤!")
                    }

                    toMainActivity()
                }
            })
    }
}