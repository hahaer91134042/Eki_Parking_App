package com.hill.devlibs.impl

import androidx.annotation.MenuRes

/**
 * Created by Hill on 14,11,2019
 */
abstract class IActivityFeatureSet {

//    var isMain:Boolean=isMainActivity()
    @get:MenuRes
    abstract val menuRes:Int

//    open fun isMainActivity():Boolean=false
}