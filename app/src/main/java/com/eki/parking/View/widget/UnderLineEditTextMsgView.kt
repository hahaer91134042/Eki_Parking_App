package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.extension.color
import com.eki.parking.extension.pxToDp

/**
 * Created by Hill on 2019/12/17
 */
class UnderLineEditTextMsgView(context: Context?, attrs: AttributeSet?) : LinearCustomView(context, attrs) {

    private val msgText: TextView = findViewById(R.id.msgText)
    private val underLineEditText: UnderLineEditTextView = findViewById(R.id.underLineEditText)

    var msg:String
        get() = msgText.text.toString()
        set(value) {msgText.text=value}

    var input:String
        set(value) {underLineEditText.input=value}
        get() = underLineEditText.input

    val inputView:UnderLineEditTextView by lazy { underLineEditText }

    fun showError(msg:String){
        this.msg=msg
        underLineEditText.showError()
    }
    fun showNormal(){
        msg=""
        underLineEditText.showNormal()
    }
    fun clearStatus(){
        input=""
        showNormal()
    }

    init {
        inputView.whenInputOnFocuse {
            showNormal()
        }
    }

    override fun setUpInflatView(typedArray: TypedArray) {
        var inputSize = typedArray?.getDimension(R.styleable.UnderLineEditTextMsgView_ule_input_textSize, pxToDp(16))
        var inputType = typedArray?.getInt(R.styleable.UnderLineEditTextMsgView_ule_inputType,UnderLineEditTextView.NONE)
        underLineEditText.setTextSize(inputSize)
        if (inputType!=UnderLineEditTextView.NONE)
            underLineEditText.setInputType(inputType)

        var hintStr=typedArray?.getString(R.styleable.UnderLineEditTextMsgView_ule_input_hint)
        when(hintStr.isNullOrEmpty()){
            true->{
                var res=typedArray?.getResourceId(R.styleable.UnderLineEditTextMsgView_ule_input_hint,0)
                if (res>0)
                    underLineEditText.setHintStr(getString(res))
            }
            else->underLineEditText.setHintStr(hintStr)
        }

        var maxNum=typedArray?.getInteger(R.styleable.UnderLineEditTextMsgView_ule_max_input_num,0)
        if (maxNum>0){
            underLineEditText.setMaxInput(maxNum)
        }

        var headerIconRes = typedArray?.getResourceId(R.styleable.UnderLineEditTextMsgView_ule_headerIcon, 0)
        if (headerIconRes > 0)
            underLineEditText.setHeaderIcon(headerIconRes)

        var lineColor = typedArray?.getColor(R.styleable.UnderLineEditTextMsgView_ule_line_color, 0)
        when (lineColor > 0) {
            true ->underLineEditText.normalColor=lineColor
            else -> {
                var lineColorRes = typedArray?.getResourceId(R.styleable.UnderLineEditTextMsgView_ule_line_color, 0)
                if (lineColorRes > 0)
                    underLineEditText.normalColor= color(lineColorRes)
            }
        }

        var eColor = typedArray?.getColor(R.styleable.UnderLineEditTextMsgView_ule_line_error_color, 0)
        when (eColor > 0) {
            true ->underLineEditText.errorColor=eColor
            else -> {
                var lineColorRes = typedArray?.getResourceId(R.styleable.UnderLineEditTextMsgView_ule_line_error_color, 0)
                if (lineColorRes > 0)
                    underLineEditText.errorColor=color(lineColorRes)
            }
        }

        underLineEditText.showNormal()
    }

    override fun setStyleableRes(): IntArray? =R.styleable.UnderLineEditTextMsgView
    override fun setInflatView(): Int = R.layout.item_edit_text_with_msg_view
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}