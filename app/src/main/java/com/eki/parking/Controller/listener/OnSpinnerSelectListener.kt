package com.eki.parking.Controller.listener

/**
 * Created by Hill on 2019/6/24
 */
interface OnSpinnerSelectListener<SELECT> {
    fun onItemSelect(position:Int,item:SELECT)
}