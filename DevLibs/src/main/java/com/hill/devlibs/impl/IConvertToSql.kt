package com.hill.devlibs.impl

import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 16,10,2019
 */
interface IConvertToSql<T:SqlVO<*>>{
    fun toSql():T
}