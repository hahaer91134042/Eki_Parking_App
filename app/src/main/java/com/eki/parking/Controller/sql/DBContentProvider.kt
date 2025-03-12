package com.eki.parking.Controller.sql

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.hill.devlibs.sql.LibDBContentProvider
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.DbUtil


/**
 * Created by Hill on 2018/2/2.
 */
class DBContentProvider : LibDBContentProvider() {


//    companion object {
//
//        private const val URI_ROOT = 0
//        private const val DB_TABLE_EXIST = 1  //這個可以設定變數 就可以查詢他在List的位置 目前不用
//
////        @JvmStatic val CONTENT_URI_MainCatalog: Uri = Uri.parse("content://$AUTHORITY/$DB_TABLE_MAINCATALOG")
////        @JvmStatic val CONTENT_URI_SubCatalog: Uri = Uri.parse("content://$AUTHORITY/$DB_TABLE_SUBCATALOG")
////        @JvmStatic val CONTENT_URI_UserAccount: Uri = Uri.parse("content://$AUTHORITY/$DB_TABLE_USER")
////        @JvmStatic val CONTENT_URI_ShoppingCar: Uri = Uri.parse("content://$AUTHORITY/$DB_TABLE_SHOPPING_CAR")
////        @JvmStatic val CONTENT_URI_ShopInfo: Uri = Uri.parse("content://$AUTHORITY/$DB_TABLE_SHOPINFO")
////        @JvmStatic val CONTENT_URI_SearchList: Uri = Uri.parse("content://$AUTHORITY/$DB_TABLE_SEARCH_LIST")
////        @JvmStatic val CONTENT_URI_BrandList: Uri = Uri.parse("content://$AUTHORITY/$DB_TABLE_BRAND_LIST")
////        @JvmStatic val CONTENT_URI_BannerLink: Uri = Uri.parse("content://$AUTHORITY/$DB_TABLE_BANNER_LINK")
////        @JvmStatic val CONTENT_URI_TireSizeFormat: Uri=Uri.parse("content://$AUTHORITY/$DB_TABLE_TIRE_SIZE_FORMAT")
////        @JvmStatic val CONTENT_URI_SMS: Uri=Uri.parse("content://$AUTHORITY/$DB_TABLE_SMS")
//
//        private var sUriMatcher = UriMatcher(URI_ROOT).apply {
//
//        }
//
//    }


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


    override fun initUri() {

        for (i in 0 until SqlConfig.sqlTableList.size) {
            sUriMatcher.addURI(SqlConfig.AUTHORITY, SqlConfig.sqlTableList[i].tableName, DB_TABLE_EXIST)
        }

    }


    override fun initDB() = DBOpenHelper.creatSQL(context)!!

    override fun onQuery(uri: Uri?, projection: Array<out String>?, queryArgs: DbUtil.QueryArgs?): Cursor? {

//        Log.w("Select by->${queryBuilder?.select}")
//        Log.i("Order by->${queryBuilder?.orderBy}")
        Log.i("OnQuery Uri Path->${uri?.path}")

        try {
            var c = mSqlDb.query(true, getTable(uri), projection, queryArgs?.select, queryArgs?.selectArgs, queryArgs?.groupBy, queryArgs?.having, queryArgs?.orderBy, queryArgs?.limit) //"ASC DESC
            c?.setNotificationUri(context!!.contentResolver, uri)
            return c
        } catch (e: Exception) {
            Log.e("$tag-> OnQuery $e")
        }

        return null
    }

    override fun onQuery(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        Log.i("OnQuery Uri Path->${uri?.path}")

        try {
            var c = mSqlDb?.query(true, getTable(uri), projection, selection, selectionArgs, null, null, sortOrder, null) //"ASC DESC
            c?.setNotificationUri(context!!.contentResolver, uri)
            return c
        } catch (e: Exception) {
            Log.e("$tag-> OnQuery $e")
        }

        return null
    }

    override fun onInsert(uri: Uri, values: ContentValues?): Uri? {

        try {
            val rowId = mSqlDb?.insert(getTable(uri), null, values)
            if (rowId!! > 0) {
//                Log.e("--onInsert-- raw uri->$uri")
                val insertedRowUri: Uri = ContentUris.withAppendedId(uri, rowId)
//                Log.d("--onInsert-- insert Uri->$insertedRowUri")
                context?.contentResolver?.notifyChange(insertedRowUri, null)
                return insertedRowUri
            }
        } catch (e: Exception) {
            Log.e("$tag-> OnInsert $e")
            throw e
        }
        return null
    }

    override fun onDelete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {


        try {
            Log.i("OnDelete Uri Path->${uri?.path}")

            return mSqlDb?.delete(getTable(uri), selection, selectionArgs)
        } catch (e: Exception) {
            Log.e("$tag-> OnDelete $e")
        }

        return -1
    }

    override fun onUpdate(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {

        try {
            return mSqlDb?.update(getTable(uri), values, selection, selectionArgs)
        } catch (e: Exception) {
            Log.e("$tag-> OnUpdate $e")
        }

        return -1;
    }

    private fun getTable(uri:Uri?):String{
        return (uri?.path?:"").replace("/","")
    }

    override fun getType(uri: Uri): String? {
        // TODO Auto-generated method stub
        return null
    }

}
