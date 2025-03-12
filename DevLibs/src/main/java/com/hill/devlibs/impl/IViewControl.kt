package com.hill.devlibs.impl

import android.view.View

/**
 * Created by Hill on 2019/12/04
 */
abstract class IViewControl<ClickView:View>{
    interface IActionView<ActionView:View>{
        fun onClickActionView(actionView:ActionView)
    }
    interface IClickViewSet<VIEW:View>{
        val view:VIEW
    }



    abstract fun clickViewSet(clickView: ClickView)
    open fun viewAfterClick(clickView:ClickView){}//以後改成viewOnClick好了

    fun <V:View> onAction(click:ClickView,view:V?=null){
        viewAfterClick(click)
        if (this is IActionView<*> && view!=null){
            var action=this as IActionView<V>
            action.onClickActionView(view)
        }
    }

    fun useDefaultSet(clickView:ClickView): IViewControl<ClickView> {
        clickViewSet(clickView)
        clickView.setOnClickListener { viewAfterClick(clickView) }
        return this
    }
//    abstract fun onViewClick(clickView:ClickView,actionView:ActionView)
}