package com.eki.parking.Model.sql

import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 2019/12/11
 */
@SqlTableSet(table = "CountryCode")
class CountryCode: SqlVO<CountryCode>(){

    @SqlColumnSet(key="Id",attr = SqlAttribute.ID,order = 1)
    var Id=0
    @SqlColumnSet(key="FullEn",attr = SqlAttribute.TEXT,order = 2)
    var fullEn=""
    @SqlColumnSet(key="FullCn",attr = SqlAttribute.TEXT,order = 3)
    var fullCn=""
    @SqlColumnSet(key="Short2",attr = SqlAttribute.TEXT,order = 4)
    var short2=""
    @SqlColumnSet(key="Short3",attr = SqlAttribute.TEXT,order = 5)
    var short3=""
    @SqlColumnSet(key="Code",attr = SqlAttribute.TEXT,order = 6)
    var code=""

    override fun initFromArray(datas: Array<String>): Boolean {
        return runCatching {
            datas.notNull { array->
                fullEn=array[0]
                fullCn=array[1]
                short2=array[2]
                short3=array[3]
                code=array[4]
            }
        }.isSuccess
    }

    override fun clear() {

    }


}