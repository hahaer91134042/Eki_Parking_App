package com.hill.devlibs.listener

/**
 * Created by Hill on 2019/4/25
 */
interface OnPostExecuteListener<R> {
    fun onTaskPostExecute(result: R)
}