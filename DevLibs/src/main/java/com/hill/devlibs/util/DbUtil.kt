package com.hill.devlibs.util

import android.os.Bundle
import com.hill.devlibs.EnumClass.OrderBy
import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 2018/6/14.
 */
//	Cursor DbList=mFriendDB.query(distinct=ture 重複的資料只取一個  false全show
//  , table=資料表名稱
//  , columns=欄位名稱
//  , selection  指定查詢條件
//  , selectionArgs  指定查尋條件 的參數
//  , groupBy  指定分組
//  , having  指定分組條件
//  , orderBy 指定排序條件
//  , limit   指定查詢結果顯示多少條紀錄
//  , cancellationSignal          )
/**
 * query(boolean distinct, String table, String[] columns,
 *String selection, String[] selectionArgs, String groupBy,
 *String having, String orderBy, String limit) {
 */
object DbUtil {
    @JvmStatic val SELECT="select"
    @JvmStatic val SELECT_ARGS="selectArgs"
    @JvmStatic val GROUP_BY="groupBy"
    @JvmStatic val HAVING="having"
    @JvmStatic val ORDER_BY="orderBy"
    @JvmStatic val LIMIT="limit"

//    @JvmStatic fun where(key: String, value: String): String {
//        return "$key='$value'"
//    }
    @JvmStatic fun where(key: String, value: Any): String {
        return "$key='$value'"
    }

    @JvmStatic fun select(vararg strings: String): String {
        val builder = StringBuilder(strings[0] + "=?")
        for (i in 1 until strings.size) {
            builder.append(" AND " + strings[i] + "=?")
        }
//        Log.i("select->" + builder.toString())
        return builder.toString()
    }

    @JvmStatic fun selectArgs(vararg args:Any): Array<out String> {
//        for (i in args.indices) {
//            Log.d("select args->" + args[i])
//        }
        return args.map { it.toString() }.toTypedArray()
    }
    @JvmStatic fun selectArgs(vararg args: String): Array<out String> {
//        for (i in args.indices) {
//            Log.d("select args->" + args[i])
//        }
        return args
    }

    @JvmStatic fun getQueryArgs(): QueryArgs {
        return QueryArgs()
    }


    class QueryArgs: SqlVO<QueryArgs>(){
        var where:String?=null
        var select:String?=null
        var selectArgs:Array<out String>?=null
        var groupBy:String?=null
        var having:String?=null
        var orderBy:String?=null
            private set

        var limit:String?=null//"ASC DESC"


        fun setLimit(value:Int){
            limit=value.toString()
        }
        fun setOrderBy(orderBy:String,isDESC:Boolean){
            this.orderBy=StringBuilder(orderBy).run {
                if (isDESC)
                    append(" DESC")
                else
                    append(" ASC")
                toString()
            }
        }

        override fun getBundle(): Bundle? {
            return Bundle().apply {
                putString(SELECT,select)
                putStringArray(SELECT_ARGS,selectArgs)
                putString(GROUP_BY,groupBy)
                putString(HAVING,having)
                putString(ORDER_BY,orderBy)
                putString(LIMIT,limit)
            }
        }

        override fun clear() {

        }

        override fun printValue() {

        }
    }
}