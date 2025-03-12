package com.eki.parking.View.widget

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.eki.parking.Model.EnumClass.LocAvailable
import com.eki.parking.R
import com.eki.parking.extension.dpToPx
import com.eki.parking.View.abs.ConstrainCustomView
import com.eki.parking.extension.drawable

/**
 * Created by Hill on 25,09,2019
 */

class LocationMarkerView(context: Context?) : ConstrainCustomView(context) {

    fun setImg(@DrawableRes res:Int){
        val iconView = findViewById<ImageView>(R.id.iconView)
        iconView.setImageDrawable(drawable(res))
//        initImage(context, iconView, res)
    }

    fun setAvailable(enum:LocAvailable){
//        Log.d("marker set avable->$enum")
        when(enum){
            LocAvailable.Available->{
                setBackgroundResource(R.drawable.shape_circle_stroke_green2_5dp)
            }
            else -> {
                setBackgroundResource(R.drawable.shape_circle_stroke_gray5_5dp)
            }
        }
    }

    fun setText(text:String){
        val markerText = findViewById<TextView>(R.id.markerText)
        markerText.text = text
//        setTextColorBackground(context, markerText, R.color.color_black, R.color.color_transparent, 14f)
    }

    override fun setAttachToRoot(): Boolean {
        return true
    }
    override fun setInflatView(): Int =R.layout.item_location_marker
    override fun initNewLayoutParams(): ViewGroup.LayoutParams =ViewGroup.LayoutParams(dpToPx(45f),dpToPx(45f))
}