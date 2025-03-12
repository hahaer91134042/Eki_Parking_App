package com.eki.parking.Controller.impl

/**
 * Created by Hill on 2020/02/21
 */
abstract class IMultiOverCheck {
    var isCheckOk=false
    var onCheckAll:(()->Unit)?=null

    protected open fun onOver(){
        isCheckOk=true
        onCheckAll?.invoke()
    }
}