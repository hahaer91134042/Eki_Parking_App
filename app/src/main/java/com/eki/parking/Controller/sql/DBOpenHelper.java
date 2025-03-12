package com.eki.parking.Controller.sql;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.hill.devlibs.sql.LibDBOpenHelper;
import com.hill.devlibs.collection.SqlList;
import com.hill.devlibs.tools.Log;


/**
 * Created by Hill on 2018/2/2.
 */
public class DBOpenHelper extends LibDBOpenHelper<SqlUriPair> {

	private static DBOpenHelper instance;

	public DBOpenHelper(Context context){
	    super(context,new DbSet(SqlConfig.DB_NAME,SqlConfig.DB_VERSION));
    }


    public static SQLiteDatabase creatSQL(Context c){
		try{
			instance = new DBOpenHelper(c);
			SQLiteDatabase db=instance.getWritableDatabase();
			// TODO: 2018/2/7 沒有data base 會自己執行
//			`is`.onCreate(db);

			return db;
		}catch (SQLiteException e){
			Log.e(e.toString());
		}
		return null;
	}

	public static DBOpenHelper getInstance(Context c){
		if (instance==null)
			creatSQL(c);

		return instance;
	}


    @Override
    protected SqlList<SqlUriPair> setTableList() {
        return SqlConfig.getSqlTableList();
    }
}
