package com.eki.parking.Controller.impl

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.View.pager.view.TablePagerView

/**
 * Created by Hill on 14,11,2019
 */
abstract class IPagerTabContent<F:BaseFragment<*>> {
    abstract val title:String
    @get:DrawableRes
    abstract val tabBackgroundRes:Int

    @get:DrawableRes
    abstract val icon:Int

    abstract val frag:F
    open val tab:TablePagerView.ITabSet?=null

    @ColorInt open val textColorSelector:Int=0
}