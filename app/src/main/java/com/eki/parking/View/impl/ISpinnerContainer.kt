package com.eki.parking.View.impl

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner

/**
 * Created by Hill on 2020/05/15
 */
interface ISpinnerContainer {
    val spinner:Spinner
    val icon:ImageView
    fun containerSet(parent:LinearLayout)
}