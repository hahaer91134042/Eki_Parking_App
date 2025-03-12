package com.eki.parking.View.libs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/07/16
 */
class FloatingToBottomBehavior: FloatingActionButton.Behavior {
    constructor():super(){
        isAutoHideEnabled = false
    }
    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs){
        isAutoHideEnabled = false
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {

//        var distanceToScroll = (dependency.height+dependency.y)/2
//        var ratio = dependency.y/dependency.height
//        var transY=(distanceToScroll * ratio)
//
////        Log.w("bottom float distanceToScroll->${distanceToScroll}")
//        Log.i("dependency y->${dependency.y} height->${dependency.height}")
//        Log.w("child y->${child.y} height->${child.height} ")
//        Log.d("ratio->$ratio  transY->$transY")



//        Log.i("\ndependency now y->${dependency.y} \nh->${dependency.height}")
//        Log.w("distanceScroll->$distanceToScroll \nratio->$ratio  \ntransY->$transY")


//        child.translationY = transY
        child.translationY=-(child.height/2f+30f)
        return super.onDependentViewChanged(parent, child, dependency)
    }
}