package com.eki.parking.Model.sql

import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 2019/12/30
 */
@SqlTableSet(table = "CarBrand")
class CarBrand:SqlVO<CarBrand>(){

    @SqlColumnSet(key="Id",attr = SqlAttribute.ID,order = 1)
    var Id=0
    @SqlColumnSet(key = "Name",attr = SqlAttribute.TEXT,order = 2)
    var name=""

    override fun initFromArray(datas: Array<String>): Boolean {
        return runCatching { name=datas[0] }.isSuccess
    }

    override fun clear() {

    }
}