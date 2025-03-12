package com.hill.devlibs.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.TextViewCompat

/**
 * Created by Hill on 2021/01/08
 */
open class AutoSizeEditText : AppCompatEditText {

    constructor(context: Context?):super(context){
        startAutoSize()
    }
    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs){
        startAutoSize()
    }
    constructor(context:Context?,attrs:AttributeSet?,defStyleAttr:Int):super(context, attrs, defStyleAttr){
        startAutoSize()
    }
    private fun startAutoSize() {
        TextViewCompat.setAutoSizeTextTypeUniformWithPresetSizes(
            this,
            arrayOf(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32).toIntArray(),
            TypedValue.COMPLEX_UNIT_DIP
        )
    }
    init {
//        TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }
}