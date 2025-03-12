package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.Controller.listener.OnSpinnerSelectListener
import com.eki.parking.View.abs.ConstrainCustomView
import com.eki.parking.View.spinner.CountryCodeSpinner
import com.hill.devlibs.EnumClass.SpanHelper
import com.hill.devlibs.util.StringUtil


/**
 * Created by Hill on 2019/6/26
 */
class ExpandErrorPhoneText(context: Context?, attrs: AttributeSet?):
        ConstrainCustomView(context, attrs),
        OnSpinnerSelectListener<String>{


    var countrySpinner:CountryCodeSpinner=itemView.findViewById(R.id.countryCodeSpinner)
    var inputTex:EditText=itemView.findViewById(R.id.inputTex)
    var labelTex:TextView=itemView.findViewById(R.id.labelTex)
    var countryCode=""

    private var expanParent: View =itemView.findViewById(R.id.expandParent)
    private var errorMsg:TextView=itemView.findViewById(R.id.errorMsg)


    private var labelName=""
    private var isBeNull=true
    private var msgText=""

    private var labelSize=0.0f
    private var inputSize=0.0f


    init {
//        parseAttrSet(attrs)

        //Log.d("in init labelName->$labelName")

//        initView()
    }

    private fun initView() {

        countrySpinner.listener=this
        labelTex.textSize=labelSize
        var builder=StringUtil.getSpanBuilder()
        builder.setSpanString(labelName,SpanHelper.TYPE_NORMAL)
        if (!isBeNull)
            builder.setSpanString("*",SpanHelper.TYPE_NORMAL
                    .setTextColor(Color.RED)
                    .setRelativeSize(0.8f)
                    .hasSuperscript())

        builder.into(labelTex)

        inputTex.textSize=inputSize


        errorMsg.text=msgText
    }

    fun showMsg(msg:String=""){
        if(msg?.isNotEmpty())
            errorMsg.text=msg
        expanParent.visibility=View.VISIBLE
    }


    fun closeMsg(){
        expanParent.visibility=View.GONE
    }

    override fun onItemSelect(position: Int, item: String) {
        countryCode=item
    }

    override fun setUpInflatView(typedArray: TypedArray?) {
        initView()
    }
    override fun parseTypedArray(typedArray: TypedArray) {
        var nameRes=typedArray.getResourceId(R.styleable.ExpandErrorPhoneText_input_label,0)
        //Log.w("in Parse  nameRes-> $nameRes")
        labelName = when(nameRes!=0){
            true->getString(nameRes)
            false->typedArray.getString(R.styleable.ExpandErrorPhoneText_input_label)?:""
        }
        //Log.i("in Parse  labelName->$labelName")

        isBeNull=typedArray?.getBoolean(R.styleable.ExpandErrorPhoneText_input_is_null,isBeNull)

        labelSize=typedArray?.getDimension(R.styleable.ExpandErrorPhoneText_input_label_textSize,labelSize)
        inputSize=typedArray?.getDimension(R.styleable.ExpandErrorPhoneText_input_textSize,inputSize)

        var msgRes=typedArray.getResourceId(R.styleable.ExpandErrorPhoneText_expan_error_msg,0)
        msgText=when(msgRes!=0){
            true->getString(msgRes)
            false->typedArray.getString(R.styleable.ExpandErrorPhoneText_expan_error_msg)?:""
        }

    }

    override fun setStyleableRes(): IntArray? {
        return R.styleable.ExpandErrorPhoneText
    }

    override fun setInflatView(): Int = R.layout.item_error_expan_phone_text

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}