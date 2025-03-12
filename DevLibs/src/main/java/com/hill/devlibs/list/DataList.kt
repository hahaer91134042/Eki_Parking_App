package com.hill.devlibs.list

/**
 * Created by Hill on 2020/03/10
 */
class DataList<T>(vararg valus:T):ArrayList<T>(){
    init {
        addAll(valus)
    }
}