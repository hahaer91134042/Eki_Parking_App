package com.eki.parking.View.widget

import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.eki.parking.AppConfig
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ExtendedOrder
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.View.libs.StateButton
import com.eki.parking.extension.*
import com.hill.devlibs.extension.getColor
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.formateShort
import com.hill.devlibs.widget.AutoSizeTextView

/**
 * Created by Hill on 2021/11/17
 */
class ItemCheckoutOrder(context: Context?) : LinearCustomView(context) {

    private lateinit var payBtn:ItemBtn
    private lateinit var noCheckoutFrame:NoCheckoutSelectBtn
    private var counterHandler=Handler()

    private val btnFrame: FrameLayout = findViewById(R.id.btnFrame)
    private val startDateText: AutoSizeTextView = findViewById(R.id.startDateText)
    private val startTimeText: AutoSizeTextView = findViewById(R.id.startTimeText)
    private val endDateText: AutoSizeTextView = findViewById(R.id.endDateText)
    private val endTimeText: AutoSizeTextView = findViewById(R.id.endTimeText)
    private val alarm_msg: AutoSizeTextView = findViewById(R.id.alarm_msg)

    interface CheckoutEvent{
        fun onPay(order:EkiOrder)//點擊付款
        fun onCheckout(order:EkiOrder)//點擊退租
        fun onExtenTime(order:EkiOrder)//延時
    }

    var checkoutEvent:CheckoutEvent?=null
    private lateinit var order:EkiOrder

    override fun initInFlaterView() {

        payBtn= ItemBtn(context)
        payBtn.text= string(R.string.Go_pay)
        payBtn.setOnClickListener {
            checkoutEvent?.onPay(order)
        }
        noCheckoutFrame=NoCheckoutSelectBtn(context)
        noCheckoutFrame.checkoutBtn.setOnClickListener {
            checkoutEvent?.onCheckout(order)
        }

        noCheckoutFrame.extenBtn.setOnClickListener {
            checkoutEvent?.onExtenTime(order)
        }
    }

    fun setOrder(order:EkiOrder){
        this.order=order
        btnFrame.removeAllViews()
        when{
            order.isBeSettle()->{
                btnFrame.addView(payBtn)
            }
            order.isBeCheckOut()->{
                btnFrame.addView(noCheckoutFrame)
            }
            else->{
                throw IllegalArgumentException("Order invalid status!! serNum->${order.SerialNumber}")
            }
        }
        showData(order)
    }

    //private var c=0
    private fun showData(order:EkiOrder){
        var start=order.startDateTime()
        var end=order.endDateTime()
        startDateText.text="${start.month.mod02d()}/${start.day.mod02d()} ${string(start.weekDay.toEnum<WeekDay>().strRes)}"
        startTimeText.text="${start.time.formateShort()}"

        endDateText.text="${end.month.mod02d()}/${end.day.mod02d()} ${string(end.weekDay.toEnum<WeekDay>().strRes)}"
        endTimeText.text="${end.time.formateShort()}"

        //測試用
//        counterHandler.post(object :Runnable{
//            override fun run() {
//                //c++
//                var now=DateTime.now()
//                var min=(end-now).totalMinutes
//                //Log.w("now-> ${now}")
//                alarm_msg.text=getString(R.string.Minutes_remaining).messageFormat(min)
//                counterHandler.postDelayed(this,1000)
////                if(c>5){
////                    noCheckoutFrame.extenBtn.visibility= INVISIBLE
////                }
//            }
//        })

        //正式

        if(order.isBeCheckOut()){//假如未退租
            when{
                //已經延時過
                ExtendedOrder.creatBy(order).hasData()->{
                    alarm_msg.text=""
                    noCheckoutFrame.extenBtn.visibility= INVISIBLE
                }
                else->startExtendCounter(end)//未延時過
            }

        }else{//已經進到未付款
            alarm_msg.text=""
        }
    }

    private fun startExtendCounter(end:DateTime){
        counterHandler.post(object :Runnable{
            override fun run() {
                var now=DateTime.now()
                var min=(end-now).totalMinutes
                when{
                    AppConfig.openOrderExtenMin>min&&min>0.0->{
                        alarm_msg.text=getString(R.string.Minutes_remaining).messageFormat(min.toInt())
                        counterHandler.postDelayed(this,1000)
                    }
                    else->{
                        alarm_msg.text=""
                        noCheckoutFrame.extenBtn.visibility= INVISIBLE
                        counterHandler.removeCallbacks(this)
                    }
                }
            }
        })
    }

    override fun setInflatView(): Int = R.layout.item_checkout_order_new

    override fun initNewLayoutParams(): ViewGroup.LayoutParams?
    =ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,200f.toPx)

    private class ItemBtn(context: Context?) : StateButton(context) {
        init {
            layoutParams= LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
            //setRound(true)
            setNormalBackgroundColor(getColor(R.color.Eki_orange_4)!!)
            setPressedBackgroundColor(getColor(R.color.Eki_orange_1))
            setNormalTextColor(getColor(R.color.color_white))
            setPressedTextColor(getColor(R.color.color_white))
            setRadius(dimen(R.dimen.btn_radius_5))
            textSize= dimen(R.dimen.text_size_8)
            //去掉陰影
            stateListAnimator=null
        }

    }

    private class NoCheckoutSelectBtn(context: Context?) : LinearCustomView(context) {

        var extenBtn:StateButton
        var checkoutBtn:StateButton


        init {
            orientation=LinearLayout.HORIZONTAL
            gravity=Gravity.CENTER_VERTICAL
            removeAllViews()
            extenBtn=ItemBtn(context).also {
                it.layoutParams=LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT).also {param->
                    param.weight=1f
                    param.marginEnd=10f.toPx
                }
                it.text=string(R.string.Go_extension)
            }

            checkoutBtn=ItemBtn(context).also {
                it.layoutParams=LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT).also {param->
                    param.weight=1f
                }
                it.text=string(R.string.Go_checkout)
            }

            addView(extenBtn)
            addView(checkoutBtn)
        }

        override fun setInflatView(): Int =0
        override fun initNewLayoutParams(): ViewGroup.LayoutParams?
                =ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)

    }
}