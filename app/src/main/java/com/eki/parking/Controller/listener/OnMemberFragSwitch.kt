package com.eki.parking.Controller.listener

/**
 * Created by Hill on 2019/6/25
 */
interface OnMemberFragSwitch {
    fun onSmsCheck()
    fun onRegister(country:String,phone:String)
    fun onFinish()
}