package com.eki.parking.Controller.activity.abs

import android.view.MenuItem
import androidx.annotation.IdRes

/**
 * Created by Hill on 22,10,2019
 */
interface IToolBarItemSet {
    @IdRes
    fun itemRes():Int
    fun initMenuItem(item:MenuItem)
}