package com.eki.parking.View.viewControl

import android.widget.ImageView
import com.eki.parking.R
import com.eki.parking.extension.drawable
import com.hill.devlibs.extension.resize
import com.hill.devlibs.impl.IViewControl

/**
 * Created by Hill on 2020/04/15
 */
abstract class OrangeArrowDownClick: IViewControl<ImageView>(){
    override fun clickViewSet(reactView: ImageView) {
        var img= drawable(R.drawable.icon_arrow_down_orange)?.resize(160,90)
        reactView.setImageDrawable(img)
    }
}