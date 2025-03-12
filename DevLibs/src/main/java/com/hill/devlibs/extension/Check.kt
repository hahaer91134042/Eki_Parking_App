package com.hill.devlibs.extension

import java.io.Serializable

/**
 * Created by Hill on 2019/12/19
 */
open class Check<T>(var value:T):Serializable{

    companion object{
        fun <T> onTrue(value:T):Check<T> = CheckTrue(value)
        fun <T> onFalse(value:T):Check<T> = CheckFail(value)
    }
}
class CheckTrue<T>(v:T):Check<T>(v)
class CheckFail<T> (v:T):Check<T>(v)

//fun <T> Check<T>.onTrue(action:(T)->Unit){
//    if (this is CheckTrue) action.invoke(value)
//}
fun <T> Check<T>.onFalse(action:(T)->Unit){
    if (this is CheckFail) action.invoke(value)
}


open class CheckNull<T>(var value:T?=null):Serializable{
    companion object{
        fun <T> onNull():CheckNull<T> = CheckNull()
        fun <T> onNotNull(v:T):CheckNotNull<T> = CheckNotNull(v)
    }
}
class CheckNotNull<T>(v:T?):CheckNull<T>(v)

fun <T> CheckNull<T>.onNotNull(action: (T) -> Unit){
    if (this is CheckNotNull) action.invoke(value!!)
}



