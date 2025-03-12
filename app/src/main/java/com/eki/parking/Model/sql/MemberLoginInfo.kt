package com.eki.parking.Model.sql


import com.eki.parking.extension.sqlSave
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.collection.DataRow
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/12/14
 */
@SqlTableSet(table = "MemberLoginInfo")
class MemberLoginInfo:SqlVO<MemberLoginInfo>(){

    companion object{
        @JvmStatic
        fun save(acc:String){
            sqlSave(MemberLoginInfo().apply {
                account=acc
            })
        }
    }

    @SqlColumnSet(key="account",attr = SqlAttribute.TEXT,order = 1)
    var account=""

    override fun clear() {
        account=""
    }
}