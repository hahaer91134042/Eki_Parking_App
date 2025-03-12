package com.eki.parking.Controller.impl

/**
 * Created by Hill on 2020/10/12
 */
interface IMail {
    //收mail的對象
    fun sendMailTo():ArrayList<String>
    //標題
    fun mailSubject():String
    //內文
    fun contentText():String
}