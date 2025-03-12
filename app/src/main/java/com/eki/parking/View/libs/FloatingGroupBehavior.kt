package com.eki.parking.View.libs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.ViewGroupUtils
import com.google.android.material.appbar.AppBarLayout
import com.hill.devlibs.tools.Log
import com.hill.devlibs.widget.libs.CircleImageView


/**
 * Created by Hill on 2020/03/11
 */
@Deprecated("待開發 目前view縮上去會被蓋住而消失")
class FloatingGroupBehavior : CoordinatorLayout.Behavior<LinearLayout> {
    constructor():super(){
    }

    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs){

    }


    override fun layoutDependsOn(parent: CoordinatorLayout, child: LinearLayout, dependency: View): Boolean {
        return true
    }
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: LinearLayout, dependency: View): Boolean {

//        Log.e("group ViewChange")
//        child.getChildAt(0).visibility=View.VISIBLE

//        var lp=child.layoutParams as CoordinatorLayout.LayoutParams

//        var fabBottomMargin = lp.bottomMargin

        var distanceToScroll = (dependency.height+dependency.y)/2
        var ratio = dependency.y/dependency.height
        var transY=(distanceToScroll * ratio)*1.5f//1.5是測試出來的係數
//        Log.i("\ndependency now y->${dependency.y} \nh->${dependency.height}")
//        Log.w("distanceScroll->$distanceToScroll \nratio->$ratio  \ntransY->$transY")


        child.translationY = transY
        return true
    }

}