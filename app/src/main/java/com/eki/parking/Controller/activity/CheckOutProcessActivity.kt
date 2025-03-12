package com.eki.parking.Controller.activity

import android.content.Intent
import com.eki.parking.AppFlag
import com.eki.parking.AppResultCode
import com.eki.parking.Controller.activity.abs.BaseActivity
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.CheckOutProcess.CheckOutInvoiceFrag
import com.eki.parking.Controller.activity.frag.CheckOutProcess.CheckOutProcessFragment
import com.eki.parking.Controller.activity.frag.CheckOutProcess.PayFinalFrag
import com.eki.parking.Controller.activity.frag.CheckOutProcess.PayPageFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.OrderStatus
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.OrderCheckoutBody
import com.eki.parking.Model.response.OrderCheckoutResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.sqlSaveOrUpdate
import com.eki.parking.utils.Logger
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toJsonStr
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 19,11,2019
 */
class CheckOutProcessActivity : TitleBarActivity(),
    BaseActivity.ActivityBackResult {

    private var resultCode = AppResultCode.OrderStateNotChange
    private lateinit var checkoutOrder: EkiOrder
    private var checkoutBody = OrderCheckoutBody()

    private lateinit var processResult: CheckOutProcessFragment.ProcessResult

    private val checkOutProcessFrag = CheckOutProcessFragment().apply {
        onInitData = { result ->
            processResult = result
            processResult.info.invoice = RequestBody.OrderInvoice().apply {
                address = ""
                carrierNum = ""
                loveCode = ""
                mail = ""
                name = ""
                type = 5
                ubn = ""
            }
        }
        onReceiveBack = {
            replaceFragment(FragLevelSet(2)
                .setFrag(CheckOutInvoiceFrag().also {
                    it.onStartCheckout = { invoice ->
                        processResult.info.invoice = invoice
                        Logger.e("invoice = ${invoice.toJsonStr()}")
                        this@CheckOutProcessActivity.onBackPressed()
                        this.setUiData(invoice)
                    }
                    //it.onStartCheckout = startCheckout
                }), FragSwitcher.RIGHT_IN
            )
        }
        onCheckoutBack = { flag,result ->
            processResult = result
            startCheckout(flag)
        }
    }

    private fun startCheckout(flag:Int) {
        if (flag == 0) {
            processResult.info.let { i ->
                EkiRequest<OrderCheckoutBody>().also {
                    it.body = checkoutBody.apply {
                        processResult.pic.notNull { p ->
                            setImg(p)
                        }
                        setInfo(i)
                    }
                }.sendRequest(this, true, object : OnResponseListener<OrderCheckoutResponse> {
                    override fun onReTry() {
                    }

                    override fun onFail(errorMsg: String, code: String) {

                    }

                    override fun onTaskPostExecute(result: OrderCheckoutResponse) {
                        if (result.isSuccess) {
                            resultCode = AppResultCode.OrderStateChange
                            app.timeAlarmManager.removeOrder(checkoutOrder)
                        }

                        if (result.info.Url.isNotEmpty()) {
                            checkoutOrder.orderStatus = OrderStatus.BeSettle
                            checkoutOrder.Cost = processResult.calResult.finalCost
                            checkoutOrder.ReservaTime.EndTime =
                                processResult.calResult.standarCheckOut.toString()
                            checkoutOrder.CheckOutUrl = result.info.Url
                            checkoutOrder.sqlSaveOrUpdate()
                            startPayPage(checkoutOrder)
                        } else {
                            checkoutOrder.orderStatus = OrderStatus.CheckOut
                            checkoutOrder.Cost = processResult.calResult.finalCost
                            checkoutOrder.ReservaTime.EndTime =
                                processResult.calResult.standarCheckOut.toString()
                            checkoutOrder.CheckOutUrl = result.info.Url
                            checkoutOrder.sqlSaveOrUpdate()
                            showToast("感謝您的消費!!")
                            toMain()
                        }
                    }
                })
            }
        } else if (flag == 1) {
            //Line Pay
        }
    }

    private fun startPayPage(order: EkiOrder) {
        replaceFragment(FragLevelSet(1)
            .setFrag(PayPageFrag().apply {
                setData(order)
                onFinal = { final ->
                    resultCode = AppResultCode.OrderStateChange
                    replaceFragment(FragLevelSet(1)
                        .setFrag(PayFinalFrag().also { f ->
                            f.setData(final)
                        }), FragSwitcher.RIGHT_IN
                    )
                }
            }), FragSwitcher.RIGHT_IN
        )
    }

    override fun initActivityView() {
        checkoutOrder = intent.getParcel(AppFlag.DATA_FLAG)

        when {
            checkoutOrder.isBeCheckOut() -> {
                //進入結清畫面
                replaceFragment(FragLevelSet(1)
                    .setFrag(checkOutProcessFrag.also { frag ->
                        //frag.isBackToMain=isBackToMain
                        frag.setData(checkoutOrder)
                    }), FragSwitcher.NON
                )
            }
            checkoutOrder.isBeSettle() -> {
                //直接開付款畫面
                startPayPage(checkoutOrder)
            }
        }

    }

    override fun setUpResumeComponent() {}

    override fun setActivityFeature(): IActivityFeatureSet = object : IActivityFeatureSet() {
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun toolBarRes(): Int = R.id.toolbar
    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun activityBackCode(): Int = resultCode
    override fun backData(): Intent? = null
}