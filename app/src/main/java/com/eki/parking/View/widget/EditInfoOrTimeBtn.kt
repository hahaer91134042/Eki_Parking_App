package com.eki.parking.View.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.eki.parking.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by Hill on 2020/03/12
 */
class EditInfoOrTimeBtn(context: Context, attrs: AttributeSet?) : FloatingActionButton(context, attrs) {
    interface EditEvent{
        fun onInfo()
        fun onTime()
    }
    var editEvent:EditEvent?=null

    var isInfo=true
    init {
        setBackgroundColor(Color.WHITE)
        setImg()
        setOnClickListener {
            isInfo=!isInfo
            setImg()
            startEvent()
        }
    }

    private fun setImg() {
        setImageResource(when(isInfo){
            true->R.drawable.icon_clock_green
            else->R.drawable.icon_transparent_edit
        })
    }

    fun startEvent(){
        if (isInfo)
            editEvent?.onInfo()
        else
            editEvent?.onTime()
    }
}