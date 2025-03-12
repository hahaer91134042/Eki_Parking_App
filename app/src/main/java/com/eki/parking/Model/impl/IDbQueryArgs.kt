package com.eki.parking.Model.impl

import com.hill.devlibs.impl.IQueryArgsBuilder

/**
 * Created by Hill on 31,10,2019
 */
interface IDbQueryArgs {
    fun queryBuilder():IQueryArgsBuilder
}