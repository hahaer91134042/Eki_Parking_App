package com.eki.parking.View.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView

/**
 * Created by Hill on 2020/02/07
 */
class MinOffsetView(context: Context?) : LinearCustomView(context) {

    private val minOffsetTex: TextView = findViewById(R.id.minOffsetTex)
    private val areaView: View = findViewById(R.id.areaView)

    var min:String=""
        set(value) {minOffsetTex.text=value}
    @ColorRes
    var color:Int=0
        set(value) {areaView.setBackgroundColor(getColor(value))}

    var clickView=areaView

    init {

    }

    override fun setInflatView(): Int = R.layout.item_min_offset_view
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}