package com.eki.parking.Controller.activity.frag.Main.child

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppConfig
import com.eki.parking.Controller.activity.frag.Main.adapter.UnpaidAdapter
import com.eki.parking.Controller.activity.frag.SiteDetail.child.SiteBaseInfoFrag
import com.eki.parking.Controller.asynctask.task.server.RequestTask
import com.eki.parking.Controller.broadcastReceiver.EkiReservaAlarm
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.OrderExtendTimeProcess
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.DTO.TimeRange
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.LoadReservaResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ExtendedOrder
import com.eki.parking.Model.sql.LocationReserva
import com.eki.parking.R
import com.eki.parking.View.popup.OrderExtenTimePopup
import com.eki.parking.View.widget.ItemCheckoutOrder
import com.eki.parking.databinding.FragmentUnpaidBinding
import com.eki.parking.extension.checkOutOrder
import com.eki.parking.extension.isBetween
import com.eki.parking.extension.sqlDataListAsync
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format
import com.hill.devlibs.time.ext.minSpan
import com.hill.devlibs.tools.Log


class UnpaidFragment : BaseFragment<SiteBaseInfoFrag>(),ISetData<ArrayList<EkiOrder>>,
    IFragViewBinding {

    private lateinit var binding: FragmentUnpaidBinding

    private lateinit var unpaidAdapter: UnpaidAdapter
    private var orderList: ArrayList<EkiOrder> = arrayListOf<EkiOrder>()

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragmentUnpaidBinding.inflate(inflater, container, false)
        unpaidAdapter = UnpaidAdapter(requireContext())

        return binding
    }

    override fun initFragView() {
        binding.unpaidRecyclerView.adapter = unpaidAdapter

        binding.unpaidRefreshView.setOnRefreshListener {
            sqlDataListAsync<EkiOrder> { list ->
                this.setData(list)
            }
            binding.unpaidRefreshView.isRefreshing = false
        }
    }

    override fun setData(data: ArrayList<EkiOrder>?) {
        data?.let { orderData ->

            orderList = orderData

            val checkOutList = orderList.filter { it.isBeSettle() || it.isBeCheckOut() }
                .sortedBy { it.ReservaTime.startDateTime().toStamp() }

            createItemList(checkOutList)
        }
    }

    private fun createItemList(checkOutList: List<EkiOrder>) {
        val unPayList = checkOutList.filter { it.isBeSettle() }
        val unCheckoutList = checkOutList.filter { it.isBeCheckOut() }

        if (unPayList.isEmpty() && unCheckoutList.isEmpty()) {
            binding.unpaidRecyclerView.visibility = View.GONE
        } else {
            binding.unpaidRecyclerView.visibility = View.VISIBLE
            unpaidAdapter.checkoutEvent = CheckOrderItem()
            unpaidAdapter.submitData(unPayList,unCheckoutList)
        }
    }

    private inner class CheckOrderItem() : ItemCheckoutOrder.CheckoutEvent {

        override fun onPay(order: EkiOrder) {
            //在activity裡面自有判斷 這筆訂單是否要結帳還是付款
            checkOutOrder(order)
        }

        override fun onCheckout(order: EkiOrder) {
            //在activity裡面自有判斷 這筆訂單是否要結帳還是付款
            checkOutOrder(order)
        }

        override fun onExtenTime(order: EkiOrder) {

            //要延長時間之前 先確認目前地點的預約狀態
            RequestTask<LoadReservaResponse>(requireContext(),
                EkiRequest<SendIdBody>().apply {
                    body = SendIdBody(EkiApi.LoadReservaStatus).also {
                        it.serNum.add(order.LocSerial)
                        it.time = DateTime().format(AppConfig.ServerSet.dateTimeFormat)
                    }
                }, true
            ).setExecuteListener(object : OnResponseListener<LoadReservaResponse> {
                override fun onReTry() {

                }

                override fun onTaskPostExecute(result: LoadReservaResponse) {

                    result.info.firstOrNull()?.toSql()?.let { reserve ->
                        showExtendPopup(order, reserve)
                    }
                }

                override fun onFail(errorMsg: String, code: String) {
                }
            }).start()

        }

        private fun showExtendPopup(order: EkiOrder, reserve: LocationReserva) {
            val extRange = ExtendTime(order, reserve).range

            if (extRange == null) {
                showToast(getString(R.string.Insufficient_opening_hours_please_find_other_parking_spaces_thank_you))
                return
            }

            if ((extRange.end - extRange.start).totalMinutes < AppConfig.ReservaTimeOffsetMin) {
                showToast(getString(R.string.Insufficient_opening_hours_please_find_other_parking_spaces_thank_you))
                return
            }

            Log.d("time select start->${extRange.start} end->${extRange.end}")

            OrderExtenTimePopup(context).also { pop ->
                pop.setTimeRange(extRange.start, extRange.end)
                pop.setData(order)
                pop.onSelectExtendEnd = { end ->
                    Log.i("on select extend end->$end")
                    startExtendOrderPrecess(order, end) { success, range ->
                        when (success) {
                            true -> {
                                ExtendedOrder.creatBy(order).sqlSaveOrUpdate()
                                pop.dismiss()
                            }
                            else -> {
                                if (range == null) {
                                    pop.dismiss()
                                } else {
                                    showToast(getString(R.string.Delay_in_this_period_of_time_failed_please_select_again))
                                    pop.refreshRange(range.start, range.end)
                                }
                            }
                        }
                    }
                }
            }.showPopupWindow()
        }
    }

    private class ExtendTime(val order: EkiOrder, val reserve: LocationReserva) {
        var range: TimeRange? = null

        init {
            val now = DateTime.now()

            val openEnd = reserve.OpenList.firstOrNull {
                it.isBetween(now)
            }?.endDateTime()

            //使用者的預約結束時間當延長時間的起點
            val extStart = order.ReservaTime.endDateTime()

            //這邊要找出 這個已預約時間的下個預約時間(假如有的話 )
            val locReserve = reserve.ReservaList.sortedBy { it.startDateTime().toStamp() }
            val orderIndex = locReserve.indexOfFirst {
                it.startDateTime() == order.ReservaTime.startDateTime()
            }
            val nextReserve = locReserve.elementAtOrNull(
                when (orderIndex) {
                    -1 -> orderIndex
                    else -> orderIndex + 1
                }
            )

            if (openEnd != null && extStart != null) {
                val end = when (nextReserve == null) {
                    true -> openEnd
                    else -> nextReserve.startDateTime() - AppConfig.ReservaDelayMin.minSpan
                }
//                Log.w("ExtendTime Range end->${end}")
                range = TimeRange(extStart, end)
            }

        }
    }

    private fun startExtendOrderPrecess(
        oriOrder: EkiOrder,//原來的訂單
        end: DateTime,
        back: (Boolean, TimeRange?) -> Unit
    ) {

        OrderExtendTimeProcess(context, RequestBody.OrderExtend().also {
            it.serNum = oriOrder.SerialNumber
            it.time = end.toString()
        }).apply {
            onSuccess = { newOrder ->
                //延時成功 更新整個訂單  重新顯示(把舊訂單資料重新設定)
                orderList[orderList.indexOfFirst { it.SerialNumber == oriOrder.SerialNumber }] =
                    newOrder
                createItemList(orderList.sortedBy { it.ReservaTime.startDateTime().toStamp() })
                showToast(getString(R.string.Delayed_completion))

                //重新設定通知
                app.timeAlarmManager.removeOrder(oriOrder)
                app.timeAlarmManager.addOrder(newOrder, EkiReservaAlarm.MsgType.ReservaStart)
                back(true, null)
            }
            onExtenFail = { r ->

                val extRange = ExtendTime(oriOrder, r).range

                back(
                    false, when (extRange) {
                        null -> {
                            showToast(getString(
                                R.string.Insufficient_opening_hours_please_find_other_parking_spaces_thank_you))
                            null
                        }
                        else -> when {
                            (extRange.end - extRange.start).min < AppConfig.ReservaTimeOffsetMin -> {
                                showToast(getString(
                                    R.string.Insufficient_opening_hours_please_find_other_parking_spaces_thank_you))
                                null
                            }
                            else -> extRange
                        }
                    }
                )
            }
            onFail = { msg ->
                showToast(msg)
                //正式用
                back(false, null)
            }
        }.run()
    }
}