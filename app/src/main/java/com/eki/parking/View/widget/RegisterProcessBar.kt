package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import com.eki.parking.R
import com.eki.parking.View.abs.FrameCustomView

/**
 * Created by Hill on 2019/12/10
 */
class RegisterProcessBar(context: Context?, attrs: AttributeSet?) : FrameCustomView(context, attrs) {

    private val baseDataImg: ImageView = findViewById(R.id.baseDataImg)

    fun toBaseDataImg(){
        baseDataImg.setImageResource(R.drawable.icon_base_data_select)
    }

    override fun setUpInflatView(typedArray: TypedArray?) {

    }

    override fun setInflatView(): Int = R.layout.item_register_process_bar
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}