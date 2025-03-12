package com.eki.parking.View.impl

import android.view.View
import androidx.annotation.LayoutRes

/**
 * Created by Hill on 2020/05/15
 */
interface ISpinnerItem {

    @get:LayoutRes
    val itemRes:Int
    fun setUpItem(view:View,position:Int)
}