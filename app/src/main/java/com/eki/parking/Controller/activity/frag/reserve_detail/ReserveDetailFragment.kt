package com.eki.parking.Controller.activity.frag.reserve_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppConfig
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.dialog.EkiProgressDialog
import com.eki.parking.Controller.dialog.SimulatePageDialog
import com.eki.parking.Controller.dialog.child.ApplyCompensationDialog
import com.eki.parking.Controller.dialog.child.OrderCancelSelectDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.BaseProcess
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.EnumClass.EkiErrorCode
import com.eki.parking.Model.model.EkiOrder2
import com.eki.parking.Model.model.toEkiOrder
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.OrderCancelImgBody
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.OrderCancelImgResponse
import com.eki.parking.Model.response.OrderCancelResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragmentReserveDetailBinding
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.show
import com.eki.parking.extension.showProgress
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format
import java.io.File

//By Linda
class ReserveDetailFragment : SearchFrag(), ISetData<EkiOrder2>, IFragViewBinding {

    private lateinit var binding: FragmentReserveDetailBinding

    private lateinit var orderData: EkiOrder2
    private val now = DateTime.now()

    var toMain: (() -> Unit)? = null

    override fun initFragView() {
        toolBarTitle = getString(R.string.Appointment_Details)

        binding.detailAddressText.text = orderData.Address.Detail

        val startDate = orderData.ReservaTime.startDateTime().date
        val startWeek = getStringArr(
            R.array.chinese_week_string_array
        )[orderData.ReservaTime.startDateTime().weekDay]
        val startTime = orderData.ReservaTime.startDateTime().time.toString()
        val endDate = orderData.ReservaTime.endDateTime().date
        val endWeek = getStringArr(
            R.array.chinese_week_string_array
        )[orderData.ReservaTime.endDateTime().weekDay]
        val endTime = orderData.ReservaTime.endDateTime().time.toString()

        binding.detailStartDateText.text = "${startDate} $startWeek ${startTime.removeRange(startTime.length-3,startTime.length)}"
        binding.detailEndDateText.text = "${endDate} $endWeek ${endTime.removeRange(endTime.length-3,endTime.length)}"
        binding.priceText.text = getString(R.string.price_half_hour, orderData.LocPrice)
        binding.estimatedCostText.text = getString(R.string.price,orderData.Cost.toInt())

        binding.carPlaceText.text = orderData.LocSerial
        binding.orderNumberText.text = orderData.SerialNumber

        binding.reserveDetailDeleteButton.setOnClickListener {
            cancelReserve(orderData)
        }

        if (now > orderData.ReservaTime.startDateTime()) {
            binding.reserveDetailDeleteButton.visibility = View.INVISIBLE
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragmentReserveDetailBinding.inflate(inflater, container, false)
        return binding
    }

    override fun setData(data: EkiOrder2?) {
        data?.let {
            orderData = data
        }
    }

    private fun cancelReserve(order: EkiOrder2) {
        //                Log.d("delete order serNum->${order.SerialNumber}")
        val now = DateTime.now()
        val start = order.ReservaTime.startDateTime()
        val end = order.ReservaTime.endDateTime()
        val span = start - now

        if (span.totalMinutes > AppConfig.ReservaCancelFreeMin) {
            //30分鐘以上取消
            EkiMsgDialog().also {
                it.msg = getString(R.string.Confirm_to_delete_appointment_information)
                    .messageFormat(
                        "\n${start.format("MM/dd HH:mm")}-${end.format("MM/dd HH:mm")}\n${order.Address.City}${order.Address.Detail}"
                    )
                it.determinClick = { CancelOrderProcess(order.toEkiOrder(), now).run() }
            }.show(childFragmentManager)

        } else {
            //30分以內取消讓使用者選擇要不要送審核
            OrderCancelSelectDialog().also {
                it.onCancelByPerson = {
                    CancelOrderProcess(order.toEkiOrder(), now).run()
                }
                it.onCancelNonPerson = {
                    SimulatePageDialog
                        .initWithFrag(ApplyCompensationDialog().also { inner ->
                            inner.onApplyPhoto = { pic: File, now: DateTime ->
                                CancelOrderProcess(order.toEkiOrder(), now, true, pic).run()
                            }
                        }, "申請補償")
                        .show(childFragmentManager, "ApplyCompensationDialog")
                }
            }.show(childFragmentManager)
        }
    }

    private inner class CancelOrderProcess(
        private var order: EkiOrder,
        private var now: DateTime,
        private var verify: Boolean = false,
        private var pic: File? = null,
        private var invo: RequestBody.OrderInvoice? = null
    ) : BaseProcess(context) {
        private var progress: EkiProgressDialog? = null
        private var remainNum = 0

        override fun run() {
            progress = context?.showProgress(ProgressMode.PROCESSING_MODE)
            EkiRequest<SendIdBody>().apply {
                body = SendIdBody(EkiApi.OrderCancel).apply {
                    serNum.add(order.SerialNumber)
                    time = now.toString()
                    lat = app.gps.latitude
                    lng = app.gps.longitude
                    invoice = invo
                    isVerify = verify
                }
            }.sendRequest(
                context, showProgress = false,
                listener = object : OnResponseListener<OrderCancelResponse> {
                    override fun onReTry() {

                    }

                    override fun onFail(errorMsg: String, code: String) {
                        progress?.dismiss()
                        when (EkiErrorCode.valueOf(code)) {
                            EkiErrorCode.E002 -> {
                                showErrorMsgDialog(getString(R.string.E002))
                            }
                            EkiErrorCode.E019 -> {
                                showErrorMsgDialog(getString(R.string.Delete_number_of_times_was_used_up_in_today))
                            }
                        }
                    }

                    override fun onTaskPostExecute(result: OrderCancelResponse) {
                        val cancelResult = result.info.Result.first()
                        remainNum = result.info.Remain

                        if (cancelResult.Success) {
                            val resultOrder = cancelResult.Order.toSql()

                            order.copyFrom(resultOrder)
                            order.sqlSaveOrUpdate()

                            //取消提醒
                            app.timeAlarmManager.removeOrder(order)

                            //這邊要上傳取消的審核圖片
                            if (verify) {
                                sendCancelImg()
                            } else {
                                finishProcess()
                            }
                        } else {
                            progress?.dismiss()
                            showErrorMsgDialog(getString(R.string.This_order_cannot_be_deleted))
                        }
                    }
                }, showErrorDialog = false
            )
        }

        private fun finishProcess() {
            progress?.dismiss()
            showToast(
                getString(R.string.Deleted_successfully_Today_delete_the_remaining_times)
                    .messageFormat(remainNum)
            )
            toMain?.invoke()
        }

        private fun sendCancelImg() {
            EkiRequest<OrderCancelImgBody>().also {
                it.body = OrderCancelImgBody().apply {
                    setImg(pic!!)
                    setInfo(RequestBody.OrderCancelImgInfo().apply { serNum = order.SerialNumber })
                }
            }.sendRequest(
                context, showProgress = false,
                listener = object : OnResponseListener<OrderCancelImgResponse> {
                    override fun onReTry() {

                    }

                    override fun onFail(errorMsg: String, code: String) {
                        progress?.dismiss()
                        showErrorMsgDialog(getString(R.string.This_order_cannot_be_deleted))
                    }

                    override fun onTaskPostExecute(result: OrderCancelImgResponse) {
                        finishProcess()
                    }
                }, showErrorDialog = false
            )
        }
    }
}