package com.hill.devlibs.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.hill.devlibs.R
import com.hill.devlibs.extension.notNull

/**
 * Created by Hill on 2021/01/08
 */
open class AutoSizeTextView: AppCompatTextView {

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

    }
}