package com.hill.devlibs.impl

/**
 * Created by Hill on 27,09,2019
 */
interface ISetSelectData<T> {
    fun  setOnSelectListener(l:(T)->Unit)
}