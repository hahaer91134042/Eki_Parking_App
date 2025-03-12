package com.eki.parking.Model.sql

import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 2020/04/20
 */
@SqlTableSet(table = "BankCode")
class BankCode:SqlVO<BankCode>(){
    @SqlColumnSet(key="Id",attr = SqlAttribute.ID,order = 1)
    var Id=0
    @SqlColumnSet(key="Name",attr = SqlAttribute.TEXT,order = 2)
    var name=""
    @SqlColumnSet(key="Code",attr = SqlAttribute.TEXT,order = 3)
    var code=""

    override fun initFromArray(datas: Array<String>): Boolean =
            runCatching {
                datas.notNull { array->
                    name=array[0]
                    code=array[1]
                }
            }.isSuccess

    override fun clear() {

    }
}