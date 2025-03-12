package com.hill.devlibs.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.hill.devlibs.collection.SqlList;
import com.hill.devlibs.tools.Log;


/**
 * Created by Hill on 2018/2/2.
 */
public abstract class LibDBOpenHelper<E extends SqlPair> extends SQLiteOpenHelper {

    protected static class DbSet {
        public String DB_Name;
        public CursorFactory factory = null;
        public int DB_Version;

        public DbSet(String name, int version) {
            DB_Name = name;
            DB_Version = version;
        }
    }

    //    public LibDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
//        super(context, name, factory, version);
//        // TODO Auto-generated constructor stub
//    }
    public LibDBOpenHelper(Context context, DbSet set) {
        super(context, set.DB_Name, set.factory, set.DB_Version);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        SqlList<E> list = setTableList();
        for (E e : list) {
            db.execSQL(e.parser.creatTableCmd());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("start upgrade DB!!");
        SqlList<E> list = setTableList();
        for (E e : list) {
            db.execSQL("DROP TABLE IF EXISTS " + e.tableName);
        }
        onCreate(db);
    }

    protected abstract SqlList<E> setTableList();

}
