package com.eki.parking.View.libs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.eki.parking.extension.dpToPx
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/03/11
 */
class FloatingBtnBehavior : FloatingActionButton.Behavior {
    constructor():super(){
        isAutoHideEnabled = false
    }
    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs){
        isAutoHideEnabled = false
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
//        Log.d("btn dependsOn")
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
//        Log.e("btn ViewChange")

//        var lp=child.layoutParams as CoordinatorLayout.LayoutParams

//        var fabBottomMargin = lp.bottomMargin
        var distanceToScroll = (dependency.height+dependency.y)/2
        var ratio = dependency.y/dependency.height
        var transY=(distanceToScroll * ratio)*1.5f//1.5是測試出來的係數
//        Log.i("\ndependency now y->${dependency.y} \nh->${dependency.height}")
//        Log.w("distanceScroll->$distanceToScroll \nratio->$ratio  \ntransY->$transY")


        child.translationY = transY
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View) {
        super.onDependentViewRemoved(parent, child, dependency)
    }
}