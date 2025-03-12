package com.eki.parking.Model.impl

/**
 * Created by Hill on 2020/11/16
 */

interface INotifyMsg {

    val title:String

    fun mapMsg(content:Content):String

    val needLogin:Boolean

    interface Content

}