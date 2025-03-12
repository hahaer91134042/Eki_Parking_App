package com.hill.devlibs.model.sql

import android.content.ContentValues
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.collection.DataRow

/**
 * Created by Hill on 2019/7/2
 */
abstract class SqlWithIdVO<VO : SqlWithIdVO<VO>> : SqlVO<VO>(){

    @SqlColumnSet(key="id",attr = SqlAttribute.ID,order = 1)
    var id=0

    override fun initFromRow(row: DataRow): Boolean {
        id=row.getInt("id")
        return super.initFromRow(row)
    }

    override fun contentValues(): ContentValues {
        return ContentValues().apply {
            put("id",id)
        }
    }
}