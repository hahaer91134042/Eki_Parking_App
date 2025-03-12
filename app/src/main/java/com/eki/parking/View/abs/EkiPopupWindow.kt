package com.eki.parking.View.abs

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.eki.parking.Controller.impl.IAnimateSet
import com.hill.devlibs.extension.notNull
import razerdp.basepopup.BasePopupWindow

/**
 * Created by Hill on 28,10,2019
 */
abstract class EkiPopupWindow(context: Context?) : BasePopupWindow(context),
                                                   IAnimateSet {
    val TAG:String by lazy { javaClass.simpleName }

    abstract fun popGravity():Int
    abstract fun blurBackground():Boolean
    protected abstract fun setUpView()
    @LayoutRes
    protected abstract fun layoutRes():Int
    override fun onCreateContentView(): View =createPopupById(layoutRes())
    override fun showPopupWindow() {
        setUpCondition()
        super.showPopupWindow()
    }

    override fun showPopupWindow(anchorView: View?) {
        setUpCondition()
        super.showPopupWindow(anchorView)
    }

    override fun showPopupWindow(x: Int, y: Int) {
        setUpCondition()
        super.showPopupWindow(x, y)
    }
    private fun setUpCondition(){
        setUpView()
        setBlurBackgroundEnable(blurBackground())
        popupGravity=popGravity()
        setAnim()
        setOutSideDismiss(outSideDismiss())
        setBackPressEnable(backEnable())
    }

    private fun setAnim(){
        showAnim().notNull { showAnimation = it }
        closeAnim().notNull { dismissAnimation = it }
    }

    protected open fun outSideDismiss():Boolean{
        return true
    }
    protected open fun backEnable():Boolean{
        return true
    }
}