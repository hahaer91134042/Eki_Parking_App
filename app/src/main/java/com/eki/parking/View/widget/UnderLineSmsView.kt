package com.eki.parking.View.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.View.abs.UnderLineLayout
import com.eki.parking.View.libs.StateButton
import com.eki.parking.extension.color
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.util.StringUtil

/**
 * Created by Hill on 2019/12/10
 */
class UnderLineSmsView(context: Context?, attrs: AttributeSet?) : UnderLineLayout(context, attrs) {

    private var normalLineColor= color(R.color.light_gray5)
    private var errorLineColor= Color.RED

    private val sendTex = findViewById<StateButton>(R.id.sendTex)
    private val countryTex = findViewById<TextView>(R.id.countryTex)
    private val inputTex = findViewById<EditText>(R.id.inputTex)
    private val headerIcon = findViewById<ImageView>(R.id.headerIcon)

    val sendTextView=sendTex

    var contryCode:String=""
        set(value) {
            countryTex.text=getString(R.string.country_code_format).messageFormat(value)
            field=value
        }

    var input:String
        set(value) {inputTex.setText(value)}
        get() = StringUtil.getCleanPhoneNum(inputTex.text.toString())

    var onSendClick:((String)->Unit)?=null

    var onSelectCountry:(()->Unit)?=null


    init {
        headerIcon.setImageResource(R.drawable.icon_phone)
        setLineColor(normalLineColor)


        countryTex.setOnClickListener {
            onSelectCountry?.invoke()
        }

        sendTex.setOnClickListener {
            onSendClick?.invoke(input)
        }
    }

    fun showNormal(){
        setLineColor(normalLineColor)
    }
    fun showError(){
        setLineColor(errorLineColor)
    }

    fun lockSendBtn(){
        sendTex.isEnabled=false
    }
    fun unLockSendBtn(){
        sendTex.isEnabled=true
    }

    fun unEditMode(){
        sendTex.visibility= View.GONE
        inputTex.isFocusable=false
    }
    fun inputEnable(b:Boolean){
        inputTex.isEnabled=b
    }

    override fun setInflatView(): Int = R.layout.item_sms_input_view
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}