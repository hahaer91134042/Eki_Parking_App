package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.View.abs.ConstrainCustomView
import com.hill.devlibs.EnumClass.SpanHelper
import com.hill.devlibs.util.StringUtil


/**
 * Created by Hill on 2019/6/26
 */
class ExpandErrorTextView(context: Context?, attrs: AttributeSet?): ConstrainCustomView(context, attrs){

    companion object{
        private const val TEXT=0
        private const val PWD=1
        private const val NUMBER=2
    }

    var inputType= TEXT

    var inputTex:EditText=itemView.findViewById(R.id.inputTex)
    var labelTex:TextView=itemView.findViewById(R.id.labelTex)

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

//        Log.w("1 inputType->$inputType")
//
        //TODO("In edittext password input need text input together")
        inputTex.inputType=when(inputType){
            TEXT->InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            PWD->InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            NUMBER->InputType.TYPE_CLASS_PHONE
            else->InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
        }

//        Log.i("$labelName 2 inputType->$inputType")

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
        inputType=typedArray?.getInt(R.styleable.ExpandErrorPhoneText_input_type,inputType)

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

    override fun setInflatView(): Int = R.layout.item_error_expan_text

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}