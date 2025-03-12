package com.eki.parking.Model.sql

import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 2021/01/21
 */
@SqlTableSet(table = "ManagerLvPercent")
class ManagerLvPercent:SqlVO<ManagerLvPercent>() {

    @SqlColumnSet(key="ManagerLv",attr = SqlAttribute.INT,order = 1)
    var ManagerLv=0
    @SqlColumnSet(key = "Percent",attr = SqlAttribute.INT,order = 2)
    var Percent=30

    override fun clear() {

    }
}