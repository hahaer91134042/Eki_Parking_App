package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.OrderExtendTimeBody
import com.eki.parking.Model.response.OrderExtendTimeResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.LocationReserva
import com.eki.parking.R
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.sqlSaveOrUpdate
import com.eki.parking.extension.string
import com.hill.devlibs.extension.isNull
import com.hill.devlibs.extension.onNotNull


//目前該api只做成一筆一筆處理 有需要再改成整筆
class OrderExtendTimeProcess(from: Context?, var extend:RequestBody.OrderExtend): BaseProcess(from), BaseProcess.ISetRequestBody<OrderExtendTimeBody>{


    //延時執行成功 要更新舊資料
    var onSuccess:(EkiOrder)->Unit={}
    //延時執行失敗會回傳目前該地點預約狀態 更新顯示畫面
    var onExtenFail:(LocationReserva)->Unit={}
    //執行失敗
    var onFail:(String)->Unit={}
    var retryProcess={}

    override val body: OrderExtendTimeBody
        get() = OrderExtendTimeBody().also { it.data.add(extend) }

    override val request: EkiRequest<OrderExtendTimeBody>
        get() = EkiRequest.creatBy(body)

    override fun run() {
        request.sendRequest(from,showProgress = true,listener = object :OnResponseListener<OrderExtendTimeResponse>{
            override fun onFail(errorMsg: String, code: String) {
                onFail(errorMsg)
            }
            override fun onTaskPostExecute(result: OrderExtendTimeResponse) {
                var r= result.info.firstOrNull { it.Serial==extend.serNum }

                if (r==null){
                    onFail(string(R.string.E003))
                    return
                }

                r.Order.isNull {
                    val reserva = r?.ReservaStatus?.toSql()
                    reserva?.sqlSaveOrUpdate()
                    if (reserva!=null)
                        onExtenFail.invoke(reserva)
                    else
                        onFail(string(R.string.E003))
                }.onNotNull { order->
                    val order = order.toSql()
                    order.sqlSaveOrUpdate()
                    onSuccess.invoke(order)
                }
            }

            override fun onReTry() {
                retryProcess.invoke()
            }

        },showErrorDialog = true)
    }

}