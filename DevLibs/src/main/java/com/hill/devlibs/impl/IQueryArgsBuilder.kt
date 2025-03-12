package com.hill.devlibs.impl

import com.hill.devlibs.util.DbUtil

/**
 * Created by Hill on 06,11,2019
 */
abstract class IQueryArgsBuilder:ISqlQueryArgs{

    fun build():DbUtil.QueryArgs{
        return DbUtil.getQueryArgs().also {
            it.select=select
            it.selectArgs=selectArgs
            it.where=where
        }
    }
}