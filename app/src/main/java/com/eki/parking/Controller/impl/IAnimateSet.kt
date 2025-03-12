package com.eki.parking.Controller.impl

import android.view.animation.Animation

/**
 * Created by Hill on 28,10,2019
 */
interface IAnimateSet {
    fun showAnim():Animation?
    fun closeAnim():Animation?
}