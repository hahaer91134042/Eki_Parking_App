package com.hill.devlibs.impl

import com.hill.devlibs.BaseApp

/**
 * Created by Hill on 2019/4/25
 */
interface AppFeature<APP : BaseApp<*>> {
    fun getApp(): APP
    fun getScreenHeight():Int
    fun getScreenWidth():Int
}