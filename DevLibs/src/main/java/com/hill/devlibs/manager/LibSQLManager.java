package com.hill.devlibs.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.hill.devlibs.BaseApp;
import com.hill.devlibs.collection.DataRow;
import com.hill.devlibs.collection.DataTable;
import com.hill.devlibs.sql.LibDBContentProvider;
import com.hill.devlibs.tools.Log;
import com.hill.devlibs.util.DbUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Hill on 2018/2/2.
 */

public abstract class LibSQLManager<Provide extends LibDBContentProvider> extends LibBaseManager {




    private ContentResolver mContentResolver;
    protected Provide contentProvider;

    public LibSQLManager(Context app) {
        super(app);
        mContentResolver=app.getContentResolver();
        contentProvider=(Provide) mContentResolver
                .acquireContentProviderClient(dbUri()).getLocalContentProvider();

        Log.d("---provider---"+contentProvider);
    }

    protected abstract Uri dbUri();

    protected void printSqlData(String[][] data, String tag){
        Log.e("--- "+tag+" ---");
        for (int i = 0; i <data.length ; i++) {
            String[] rowData=data[i];
            Log.w("--- Row position->"+i+" columNum->"+rowData.length+" ---");
            for (int j = 0; j <rowData.length; j++) {
                Log.i("colum->"+j+" colum name->"+rowData[j]);
            }
        }
    }
    protected synchronized DataTable getSqlDataTable(Uri uri,String[] column){
        return getSqlDataTable(uri, column,null,null,null);
    }
    protected synchronized DataTable getSqlDataTable(Uri uri,String[] column,String select,String[] selectionArgs, String sort){
        Cursor c=sqlQuery(uri,column,select,selectionArgs,sort);
        return cursorToTable(c);
    }
    protected synchronized DataTable getSqlDataTable(Uri uri, String[] column,@NonNull DbUtil.QueryArgs queryArgs) {
        Cursor c = sqlQuery(uri,column,queryArgs);
        return cursorToTable(c);
    }

    //這是全部丟出
    protected synchronized String[][] getSQLiteData( Uri uri, String[] column) {
        return getSQLiteData(uri, column,null,null,null);
    }
    protected synchronized String[][] getSQLiteData( Uri uri, String[] column,String select,String[] selectionArgs, String sort) {
        Cursor c = sqlQuery(uri,column,select,selectionArgs,sort);
        return cursorToArray(c);
    }
    protected synchronized String[][] getSQLiteData(Uri uri, String[] column,@NonNull DbUtil.QueryArgs queryArgs) {
        Cursor c = sqlQuery(uri,column,queryArgs);
        return cursorToArray(c);
    }

    private DataTable cursorToTable(Cursor c){
        if(c!=null){
            DataTable table=new DataTable();
            int columNum=c.getColumnCount();
            int rowCount=c.getCount();
            for (int i = 0; i < rowCount; i++) {
                c.moveToPosition(i);
                DataRow row=new DataRow();
                for (int j = 0; j < columNum; j++) {
                    row.put(c.getColumnName(j),c.getString(j));
                }
                table.add(row);
            }
            c.close();
            return table;
        }
        return null;
    }

    private String[][] cursorToArray(Cursor c){
        String[][] array = null ;
        if (c!=null){
            int columNum=c.getColumnCount();
            int rowCount=c.getCount();
//            Log.d("getInitData--> rowCount=>"+rowCount+" ColumNUm=>"+columNum);
            if(rowCount>0&&columNum>0){
                array = new String[rowCount][columNum] ;
                for(int i=0;i<rowCount;i++){
                    c.moveToPosition(i);
                    for(int j=0;j<columNum;j++){
                        array[i][j]=c.getString(j);
                    }
                }
//                Log.d("getName=>"+getName.length);
            }
            c.close();
        }
        return array;
    }

    protected abstract void cleanAll();

    protected Cursor sqlQuery(Uri uri, String[] columns, String select, String[] selectionArgs) {
        return sqlQuery(uri,columns,select,selectionArgs,null);
    }

    protected Cursor sqlQuery(Uri uri, String[] columns, String select, String[] selectionArgs, String sort) {
//        LibDBContentProvider provider= (LibDBContentProvider) mContentResolver.acquireContentProviderClient(uri).getLocalContentProvider();
        Cursor c=mContentResolver.query(uri, columns,select, selectionArgs, sort);
        if (c!=null)
            c.moveToFirst();//todo 一定要寫，不然會出錯
        return c;
    }
    protected Cursor sqlQuery(Uri uri, String[] columns,@NonNull DbUtil.QueryArgs queryArgs) {
//        Provide provider= (Provide) mContentResolver
//                .acquireContentProviderClient(uri).getLocalContentProvider();

        Cursor c=contentProvider.query(uri, columns,queryArgs);
        if (c!=null)
            c.moveToFirst();//todo 一定要寫，不然會出錯
        return c;
    }
    protected void sqlInsert( Uri uri,ContentValues newRow) {
        mContentResolver.insert(uri, newRow);
    }
    protected void sqlInsert(Uri uri, List<ContentValues> list){
        contentProvider.insert(uri,list);
    }
    protected void sqlUpdate( Uri uri,ContentValues newRow, String where) {
        mContentResolver.update(uri, newRow, where, null);
    }
    protected void sqlUpdate( Uri uri,ContentValues newRow, String selectWhere,String[] selectArgs) {
        mContentResolver.update(uri, newRow, selectWhere, selectArgs);
    }
    protected void sqlDeleteAll(Uri uri){
        mContentResolver.delete(uri, null, null); // 刪除所有資料
    }
    protected void sqlDelete(Uri uri,String where) {
        mContentResolver.delete(uri, where,null); // 刪除所有資料
    }
    protected void sqlDelete(Uri uri,String select,String[] selectArgs) {
        mContentResolver.delete(uri, select,selectArgs); // 刪除所有資料
    }

//    private String where(String... keys){
//        StringBuilder builder=new StringBuilder(keys[0]+"=?");
//        for (int i = 1; i <keys.length ; i++) {
//            builder.append(" AND "+keys[i]+"=?");
//        }
//        Log.i("select->"+builder.toString());
//        return builder.toString();
//    }
    protected String where(String key, String value) {
        return key+"='"+value+"'";
    }

    protected String select(String... strings) {
        StringBuilder builder=new StringBuilder(strings[0]+"=?");
        for (int i = 1; i <strings.length ; i++) {
            builder.append(" AND "+strings[i]+"=?");
        }
//        Log.i("select->"+builder.toString());
        return builder.toString();
    }
    protected String[] selectArgs(String... args){
//        for (int i = 0; i <args.length ; i++) {
//            Log.d("select args->"+args[i]);
//        }
        return args;
    }


//    public boolean hasData(DbColumn column,int id) {
//        Uri uri;
//        switch (column){
//            case MAIN_CATALOG:
//                uri= LibDBContentProvider.getCONTENT_URI_MainCatalog();
//                return hasDataInSql(uri,column.TABLE);
//            case SUB_CATALOG:
//                uri= LibDBContentProvider.getCONTENT_URI_SubCatalog();//search main cata id
//                return hasDataInSql(uri,column.TABLE,select(column.TABLE[0]),selectArgs(String.valueOf(id)));
//            case USER_ACCOUNT:
//                uri= LibDBContentProvider.getCONTENT_URI_UserAccount();
//                return hasDataInSql(uri,column.TABLE);
//            case SHOPPING_CAR:
//                uri= LibDBContentProvider.getCONTENT_URI_ShoppingCar();
//                return hasDataInSql(uri,column.TABLE,select(column.TABLE[0]),selectArgs(String.valueOf(id)));
//            case SHOP_INFO:
//                uri= LibDBContentProvider.getCONTENT_URI_ShopInfo();
//                return hasDataInSql(uri,column.TABLE,select(column.TABLE[1]),selectArgs(String.valueOf(id)));
//
//        }
//        return false;
//    }

    public abstract <T> boolean hasData(Class<T> clazz);
    public abstract <T> boolean hasData(Class<T> clazz,DbUtil.QueryArgs args);

    protected boolean hasDataInSql(Uri uri, String[] columns){
        return hasDataInSql(uri,columns,null,null);
    }
    protected synchronized boolean hasDataInSql(Uri uri, String[] columns, String select, String[] selectionArgs){
        // TODO:Edit by Hill 2018/5/18  其實這邊Cursor找出來都不會是null 所以改成檢查有幾筆資料
        Cursor c=sqlQuery(uri,columns,select,selectionArgs);
//        Log.e("hasData Cursor->"+c+" column name->"+c.getColumnNames()+" column count->"+c.getCount());
        if (c!=null) {
            int count=c.getCount();
            c.close();
            if (count>0)
                return true;
        }
        return false;
    }

}
