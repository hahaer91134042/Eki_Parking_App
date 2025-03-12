package com.eki.parking.Controller.activity.abs

/**
 * Created by Hill on 2020/02/10
 */
interface IActivityBackEvent {
    /*
    * return true 表示要走後續的onToolBarBackPress 回退fragment
    * false 表示自己控制 不做預設處置
    * */
    fun onBackPress():Boolean
}