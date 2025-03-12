package com.hill.devlibs.impl

import com.hill.devlibs.util.DbUtil

/**
 * Created by Hill on 06,11,2019
 */
interface ISqlQueryArgs {
    val select:String
    val selectArgs:Array<out String>
    val where:String
}