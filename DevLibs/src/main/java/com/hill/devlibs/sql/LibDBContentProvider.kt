package com.hill.devlibs.sql

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.hill.devlibs.util.DbUtil
import java.lang.Exception


/**
 * Created by Hill on 2018/2/2.
 */
abstract class  LibDBContentProvider : ContentProvider() {

    protected var tag= javaClass.simpleName
    lateinit var mSqlDb: SQLiteDatabase

    companion object {
        const val URI_ROOT = 0
        const val DB_TABLE_EXIST = 1  //這個可以設定變數 就可以查詢他在List的位置 目前不用
    }


    protected var sUriMatcher = UriMatcher(URI_ROOT).apply {

    }

    override fun onCreate(): Boolean {
        // ---宣告 使用Class DbOpenHelper.java 作為處理SQLite介面
        // Content Provider 就是 data Server, 負責儲存及提供資料, 他允許任何不同的APP使用
        // 共同的資料(不同的APP用同一個SQLite).

        //        LibDBOpenHelper dbHelper = new LibDBOpenHelper(getContext(), LibDBOpenHelper.DB_NAME, null, LibDBOpenHelper.DB_VERSION);
        //        mSqlDb = dbHelper.getWritableDatabase();

        initUri()

        Thread { mSqlDb =initDB() }.start()

        return true
    }

    abstract fun initUri()

    protected abstract fun initDB(): SQLiteDatabase
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


    fun query(uri: Uri?, projection: Array<out String>?, queryArgs: DbUtil.QueryArgs?): Cursor? {
        if (sUriMatcher.match(uri) != DB_TABLE_EXIST) {
            throw IllegalArgumentException("Unknown URI $uri")
        }

        return onQuery(uri,projection,queryArgs)
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        if (sUriMatcher.match(uri) != DB_TABLE_EXIST) {
            throw IllegalArgumentException("Unknown URI $uri")
        }
        return onQuery(uri,projection,selection,selectionArgs,sortOrder)
    }
    
    override fun getType(uri: Uri): String? {
        // TODO Auto-generated method stub
        return null
    }

    //https://blog.csdn.net/Gpwner/article/details/53364282
    //增加insert 效率
    fun insert(uri:Uri,list:List<ContentValues>){
        if (sUriMatcher.match(uri) != DB_TABLE_EXIST) {
            throw IllegalArgumentException("Unknown URI $uri")
        }
        try {
            mSqlDb.beginTransaction()
            list.forEach {onInsert(uri,it)}
            mSqlDb.setTransactionSuccessful()
        }catch (e:Exception){
            throw e
        }finally {
            mSqlDb.endTransaction()
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (sUriMatcher.match(uri) != DB_TABLE_EXIST) {
            throw IllegalArgumentException("Unknown URI $uri")
        }
        return onInsert(uri,values)
    }
    
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if (sUriMatcher.match(uri) != DB_TABLE_EXIST) {
            throw IllegalArgumentException("Unknown URI $uri")
        }
        return onDelete(uri,selection,selectionArgs)
    }
    
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        if (sUriMatcher.match(uri) != DB_TABLE_EXIST) {
            throw IllegalArgumentException("Unknown URI $uri")
        }
        return onUpdate(uri,values,selection,selectionArgs)
    }

    protected abstract fun onQuery(uri: Uri?, projection: Array<out String>?, queryArgs: DbUtil.QueryArgs?): Cursor?
    protected abstract fun onQuery(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor?
    protected abstract fun onInsert(uri: Uri, values: ContentValues?): Uri?
    protected abstract fun onDelete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int
    protected abstract fun onUpdate(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int
}
