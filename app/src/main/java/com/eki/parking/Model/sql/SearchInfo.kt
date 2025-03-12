package com.eki.parking.Model.sql

import com.eki.parking.AppConfig
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.impl.ISearchableItem
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 23,10,2019
 */
@SqlTableSet(table = "SearchInfo")
class SearchInfo: SqlVO<SearchInfo>(),ISearchableItem {


    @SqlColumnSet(key="Id",attr = SqlAttribute.ID,order = 1)
    var Id=0
    @SqlColumnSet(key = "Content",attr = SqlAttribute.TEXT,order = 2)
    var Content=""
    @SqlColumnSet(key = "cDate",attr = SqlAttribute.TEXT,order = 3)
    var cDate=""

    var craetTime: DateTime
        set(value) {
            cDate = value.toString()
        }
        get() = DateTime.parse(cDate, AppConfig.ServerSet.dateTimeFormat)


    override fun searchContent(): String =Content




    override fun clear() {

    }

    override fun printValue() {
        printObj(this)
    }
}