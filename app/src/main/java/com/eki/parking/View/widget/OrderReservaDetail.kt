package com.eki.parking.View.widget

import android.content.Context
import android.view.ViewGroup
import com.eki.parking.Controller.activity.intent.CheckOutProcessIntent
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.View.libs.StateButton
import com.eki.parking.extension.startActivityAnim
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.time.ext.shortTime
import com.hill.devlibs.widget.AutoSizeTextView


/**
 * Created by Hill on 15,11,2019
 */
class OrderReservaDetail(context: Context?) : LinearCustomView(context) {

    private val addressText: AutoSizeTextView = findViewById(R.id.addressText)
    private val parkingTimeText: AutoSizeTextView = findViewById(R.id.parkingTimeText)
    private val parkingFeeText: AutoSizeTextView = findViewById(R.id.parkingFeeText)
    private val orderNumText: AutoSizeTextView = findViewById(R.id.orderNumText)
    private val parkingNumText: AutoSizeTextView = findViewById(R.id.parkingNumText)
    private val checkoutBtn: StateButton = findViewById(R.id.checkoutBtn)

    fun setOrder(order: EkiOrder){

        addressText.text="${order.Address.City}${order.Address.Detail}"

        parkingTimeText.text="${order.ReservaTime.startDateTime().shortTime()}-${order.ReservaTime.endDateTime().shortTime()}"

        parkingFeeText.setTextColor(getColor(R.color.Eki_red_1))
        parkingFeeText.text=getString(R.string.Price_format).messageFormat(order.Cost.toString())

        orderNumText.text=order.SerialNumber
        parkingNumText.text=order.LocSerial

        if (!order.isBeCheckOut()){
            checkoutBtn.visibility= GONE
        }

        checkoutBtn.setOnClickListener {
            startActivityAnim(CheckOutProcessIntent(context,order))
        }

    }

    override fun setInflatView(): Int = R.layout.item_order_reserva_detail
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}