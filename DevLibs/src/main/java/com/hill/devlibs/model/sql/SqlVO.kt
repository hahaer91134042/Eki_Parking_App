package com.hill.devlibs.model.sql

import android.content.ContentValues
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.feature.SqlAttribute

import com.hill.devlibs.model.ValueObject
import com.hill.devlibs.collection.DataRow
import com.hill.devlibs.collection.DataTable
import com.hill.devlibs.extension.setObjData
import com.hill.devlibs.extension.toContentValue
import com.hill.devlibs.impl.ICopyData
import com.hill.devlibs.impl.ISqlContentValue
import com.hill.devlibs.impl.IValueObjContainer
import com.hill.devlibs.model.ValueObjContainer
import com.hill.devlibs.tools.AppGson


abstract class SqlVO<VO : SqlVO<VO>> : ValueObject(),ISqlContentValue,ICopyData<VO> {

//    companion object{
//        @JvmStatic
//        fun <VO:SqlVO<*>> creatList(table:DataTable,clazz:Class<VO>):ArrayList<VO>{
//
//        }
//    }

    override fun printValue() {
        printObj(this)
    }

    override fun copyFrom(from: VO): Boolean {
        return false
    }


//    open fun copyFrom(vo: VO): Boolean {
//        return false
//    }
    override fun contentValues(): ContentValues {
        return this.toContentValue()
    }


    open fun initFromArray(datas: Array<String>): Boolean {
        return false
    }
    @Deprecated(message = "Use initFromRow")
    open fun initFromDB(datas: Array<Array<String>>): Boolean {
        return false
    }

    open fun initFromRow(row: DataRow):Boolean{
        return row.setObjData(this)
    }
    open fun initFromTable(table: DataTable):Boolean{
        return false
    }

    fun getJsonStr():String{
        return AppGson.creat().toJson(this)
    }

}
