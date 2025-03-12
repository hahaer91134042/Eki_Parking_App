package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.eki.parking.R
import com.eki.parking.View.abs.FrameCustomView
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/09/30
 */
class SelectFrameImgView(context: Context?, attrs: AttributeSet?) : FrameCustomView(context, attrs) {

    var parentView=itemView.findViewById<FrameLayout>(R.id.parentView)

    var imgView=itemView.findViewById<ImageView>(R.id.imgView)

    init {

    }

    fun setSelect(b:Boolean){
        parentView.isSelected=b
    }

    override fun setInflatView(): Int = R.layout.item_img_select_frame

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}