package com.eki.parking.Controller.listener

/**
 * Created by Hill on 08,10,2019
 */
interface OnSqlAsyncLoad<E>{
    fun OnSuccess(data:E)
}