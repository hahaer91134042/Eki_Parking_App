package com.hill.devlibs.list

import com.hill.devlibs.impl.IConvertToSql
import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 13,11,2019
 */
class ConvertSqlList<FROM:IConvertToSql<TO>,TO:SqlVO<*>>:ArrayList<FROM>() {

    fun toSqlList():ArrayList<TO> =ArrayList<TO>().also{list->
        forEach { data->
            list.add(data.toSql())
        }
    }
}