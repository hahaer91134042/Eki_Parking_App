package com.eki.parking.Controller.impl

/**
 * Created by Hill on 2020/04/13
 */
interface IOnSerialContent<T>{
    fun onNext(next:T)
    fun onPrevious(pre:T)
}