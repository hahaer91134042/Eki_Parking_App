package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.View.abs.FrameCustomView
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.StringUtil
import java.util.*

/**
 * Created by Hill on 2018/11/20.
 */
class TimerBtn(context: Context,attributeSet: AttributeSet):FrameCustomView(context,attributeSet){

    private var btnText:String?=""
    private var totalMinute=0f
    private var totalSec=0
    private var periodSec=0
    private var startSec=0
    private var textSize=0.0f
    private var textColor=Color.BLACK
    private var timer=Timer()

    private var clickBtn=Button(context).apply { layoutParams=getChildLayoutParams() }
    private var progress=ProgressBar(context).apply { layoutParams=getChildLayoutParams() }
    private var msgText=TextView(context).apply { layoutParams=getChildLayoutParams() }

    private var countTemp="剩餘{0}秒"
    var listener:TimerListener?=null

    interface TimerListener{
        fun onStartEvent():Boolean
        fun onCountOver()
    }

//    constructor(context: Context):super(context)
//    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
//        parse(context.obtainStyledAttributes(attributeSet,R.styleable.TimerBtnAttrs))
//        PrintLogKt.d("1 btnText->$btnText totalMin->$totalMinute periodSec->$periodSec startSec->$startSec")
////        initView()
//    }

    init {
        parse(context.obtainStyledAttributes(attributeSet,R.styleable.TimerBtn))
        Log.d("btnText->$btnText totalMin->$totalMinute periodSec->$periodSec startSec->$startSec")

        initView()
    }

    private fun initView() {
        removeAllViews()//不知道為啥會已經有ChildView在裡面
        clickBtn.text = btnText
        clickBtn.textSize = textSize
        clickBtn.setPadding(0,0,0,0)
        clickBtn.background=null
        clickBtn.setTextColor(textColor)
        clickBtn.gravity=Gravity.CENTER
        clickBtn.setOnClickListener(clickBtnListener)
        addView(clickBtn)


        msgText.textSize=textSize
        msgText.setTextColor(textColor)
        msgText.gravity=Gravity.CENTER
    }
    fun start() {
        if (listener?.onStartEvent()!!){
            removeAllViews()
            addView(progress)
        }
    }
    private var clickBtnListener= OnClickListener {
//        PrintLogKt.d("cliclBtn->click")
        start()
    }
    fun back() {
        removeAllViews()
        addView(clickBtn)
    }

    fun onProcessOver(){
        removeAllViews()
        startCount()
        addView(msgText)
    }

    private fun startCount() {
        timer.schedule(CountTask(),(startSec*1000).toLong(),(periodSec*1000).toLong())

    }

    private fun getChildLayoutParams():LayoutParams=LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)

//    override fun parseTypedArray(typedArray: TypedArray) {
//        totalMinute= typedArray?.getFloat(R.styleable.TimerBtnAttrs_total_count_minute,totalMinute)!!
//        totalSec=(totalMinute*60).toInt()
//        periodSec=typedArray?.getInteger(R.styleable.TimerBtnAttrs_period_sec,periodSec)!!
//        startSec=typedArray?.getInteger(R.styleable.TimerBtnAttrs_timer_start_sec,startSec)!!
//        textSize=typedArray?.getDimension(R.styleable.TimerBtnAttrs_timer_btn_textSize,textSize)!!
//
//        var colorRes=typedArray?.getResourceId(R.styleable.TimerBtnAttrs_timer_btn_textColor,0)!!
//        textColor=typedArray?.getColor(R.styleable.TimerBtnAttrs_timer_btn_textColor,textColor)!!
//        if (colorRes!=0)
//            textColor=getColor(colorRes)
//
//        var textRes=typedArray?.getResourceId(R.styleable.TimerBtnAttrs_timer_btn_text,0)
//        btnText=typedArray?.getString(R.styleable.TimerBtnAttrs_timer_btn_text)
////        PrintLogKt.w("1 btnText->$btnText textRes->$textRes")
//        if (textRes!=0)
//            btnText=getString(textRes!!)
//        if (btnText==null)
//            btnText=""
//
//        var tempRes=typedArray.getResourceId(R.styleable.TimerBtnAttrs_timer_msg_countTemp,0)
//        countTemp=typedArray.getString(R.styleable.TimerBtnAttrs_timer_msg_countTemp)
//        if (tempRes!=0)
//            countTemp=getString(tempRes)
//        if (countTemp==null)
//            countTemp=""
//
////        PrintLogKt.w("2 btnText->$btnText textRes->$textRes")
//        typedArray.recycle()
//    }

     private fun parse(typedArray: TypedArray?) {
        totalMinute= typedArray?.getFloat(R.styleable.TimerBtn_total_count_minute,totalMinute)!!
        totalSec=(totalMinute*60).toInt()
        periodSec=typedArray?.getInteger(R.styleable.TimerBtn_period_sec,periodSec)!!
        startSec=typedArray?.getInteger(R.styleable.TimerBtn_timer_start_sec,startSec)!!
        textSize=typedArray?.getDimension(R.styleable.TimerBtn_timer_btn_textSize,textSize)!!

        var colorRes=typedArray?.getResourceId(R.styleable.TimerBtn_timer_btn_textColor,0)!!
        textColor=typedArray?.getColor(R.styleable.TimerBtn_timer_btn_textColor,textColor)!!
        if (colorRes!=0)
            textColor=getColor(colorRes)

        var textRes=typedArray?.getResourceId(R.styleable.TimerBtn_timer_btn_text,0)
        btnText=typedArray?.getString(R.styleable.TimerBtn_timer_btn_text)
//        PrintLogKt.w("1 btnText->$btnText textRes->$textRes")
        if (textRes!=0)
            btnText=getString(textRes!!)
        if (btnText==null)
            btnText=""
         var tempRes=typedArray.getResourceId(R.styleable.TimerBtn_timer_msg_countTemp,0)
         countTemp=typedArray.getString(R.styleable.TimerBtn_timer_msg_countTemp)?:""
         if (tempRes!=0)
             countTemp=getString(tempRes)
         if (countTemp==null)
             countTemp=""
//        PrintLogKt.w("2 btnText->$btnText textRes->$textRes")
        typedArray.recycle()
    }

//    override fun setStyleableRes(): IntArray? {
//        return R.styleable.TimerBtnAttrs
//    }


    override fun setInflatView(): Int {
        return 0
    }

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? {
        return null
    }




    private inner class CountTask:TimerTask(){
        override fun run() {
            app.topStackActivity.runOnUiThread {
                if (totalSec<1){
                    totalSec=(totalMinute*60).toInt()
                    listener?.onCountOver()
                    back()
                    cancel()
                    timer.purge()
                    return@runOnUiThread
                }else{
                    var msg= StringUtil.getFormateMessage(countTemp,totalSec)
                    msgText.text=msg
                    totalSec--
                }
            }
        }

    }

}