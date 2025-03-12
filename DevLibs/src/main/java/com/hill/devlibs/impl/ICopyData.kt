package com.hill.devlibs.impl

/**
 * Created by Hill on 2020/03/20
 */
interface ICopyData<T> {
    fun copyFrom(from:T): Boolean
}