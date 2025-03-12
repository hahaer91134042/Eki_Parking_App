package com.hill.devlibs.annotation.parse;

import android.content.ContentValues;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.hill.devlibs.annotation.GsonSkip;
import com.hill.devlibs.annotation.SqlColumnSet;
import com.hill.devlibs.annotation.SqlTableSet;
import com.hill.devlibs.annotation.feature.SqlAttribute;
import com.hill.devlibs.collection.DataRow;
import com.hill.devlibs.model.sql.SqlVO;
import com.hill.devlibs.tools.AppGson;
import com.hill.devlibs.tools.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.Nullable;

/**
 * Created by Hill on 2019/6/14
 */
public class TableParser {

    public static void setData(Object obj, DataRow row){
        Class<?> clazz=obj.getClass();
        Field[] fields=clazz.getDeclaredFields();
        Gson gson=AppGson.creat();
//        Log.w("data type->" + row);

        for (Field field:
                fields) {
            try {
                SqlColumnSet set = field.getAnnotation(SqlColumnSet.class);
                if (set == null)
                    continue;
                field.setAccessible(true);
//                Log.d("Field name->" + field.getName() + "  Set Key->" + set.key() + "  Set data type->" + set.type());
//                Log.i("Field class->"+field.getType());
                switch (set.attr()){
                    case ID:
                    case INT:
                        field.set(obj,row.getInt(set.key()));
                        break;
                    case LONG:
                        field.set(obj,row.getLong(set.key()));
                        break;
                    case DOUBLE:
                        field.set(obj,row.getDouble(set.key()));
                        break;
                    case FLOAT:
                        field.set(obj,row.getFloat(set.key()));
                        break;
                    case BOOLEAN:
                        field.set(obj,row.getBoolean(set.key()));
                        break;
                    case TEXT:
                        field.set(obj,row.getString(set.key()));
                        break;
                    case Obj:
                        try {
                            field.set(
                                    obj,
                                    gson.fromJson(
                                            new JsonParser().parse(row.getString(set.key())).getAsJsonObject(),
                                            field.getGenericType()
                                    )
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Array:
                        try {
//                            Log.e("---Table parser field name->"+field.getName()+" field type->"+field.getType()+
//                                    "  genericType->"+field.getGenericType());

//                            Type userListType = new TypeToken<User>(){}.getType();
                            field.set(
                                    obj,
                                    gson.fromJson(
                                            new JsonParser().parse(row.getString(set.key())).getAsJsonArray(),
                                            field.getGenericType()
                                    )
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public static ContentValues toContainValue(Object data){
        Class<?> clazz=data.getClass();
        Field[] fields=clazz.getDeclaredFields();
        ContentValues cv=new ContentValues();
        Gson gson= AppGson.creat();

        for (Field field:
             fields) {
            try {
                SqlColumnSet set = field.getAnnotation(SqlColumnSet.class);
                if (set == null)
                    continue;
                field.setAccessible(true);
//                Log.d("Field name->" + field.getName() + "  Set Key->" + set.key() + "  Set data type->" + set.type());
//                Log.w("data type->" + field.get(data));

                switch (set.attr()) {
                    case ID:
                        //略過
                        break;
                    case INT:
                        cv.put(set.key(), (int) field.get(data));
                        break;
                    case Obj:
                    case Array:
                        cv.put(set.key(), gson.toJson(field.get(data)));
                        break;
                    default:
                        cv.put(set.key(), field.get(data).toString());
                        break;
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return cv;
    }

    public static TableParser parse(Class<?> clazz) {
        return new TableParser(clazz);
    }

    public static TableParser parse(SqlVO obj) {
        return new TableParser(obj);
    }

    private Class<?> clazz;
    public String tableName;
    public ArrayList<FieldSetPair> columnList = new ArrayList<>();
    public String[] tableColumn;
    public SqlTableSet tableSet;
    private SqlVO data;

    public TableParser(Class<?> clazz) {
        this.clazz = clazz;
        //Log.d("className->"+clazz.getName());
        parse();
    }

    public TableParser(SqlVO obj) {
        clazz = obj.getClass();
        //Log.d("object class->" + clazz.getName());
        data = obj;
        parse();
    }

    private void parse() {
        try {
            tableSet = clazz.getAnnotation(SqlTableSet.class);
            tableName = tableSet.table();
//            Log.i("parse tableName->" + tableName);
            Field[] fields = clazz.getDeclaredFields();


            for (int i = 0; i < fields.length; i++) {
//                if (field.isSynthetic()||field.getName().equals("serialVersionUID"))//去除Studio Instance Run 變數 $change
//                    continue;
                //Log.i("parse field name->" + field.getName());
                //沒有這個annotation就跳過
                SqlColumnSet set = fields[i].getAnnotation(SqlColumnSet.class);
                if (set == null)
                    continue;

                columnList.add(new FieldSetPair(fields[i], set));
            }

            Collections.sort(columnList, (o1, o2) -> o1.set.order() < o2.set.order() ? -1 : 1);

            tableColumn=new String[columnList.size()];
            for (int i = 0; i < columnList.size(); i++) {
                tableColumn[i]=columnList.get(i).set.key();
            }

        } catch (Exception e) {
//            Log.e("parse exception->"+e);
            throw new NullPointerException("Annotation Error!!");
        }
    }

    public FieldSetPair getIdFieldSet(){
        for (FieldSetPair pair : columnList) {
            if(pair.set.attr()== SqlAttribute.ID)
                return pair;
        }
        return null;
    }

    public String getIdFieldName(){
        for (FieldSetPair pair : columnList) {
            if(pair.set.attr()== SqlAttribute.ID)
                return pair.field.getName();
        }
        return "";
    }

//    public void getData() {//之後再改
//        try {
//            ContentValues content=new ContentValues();
//
//            for (FieldSetPair pair : columnList) {
//                Field field = pair.field;
//                SqlColumnSet set = pair.set;
//
//                field.setAccessible(true);//java預設都是關閉的 要取value要開
//                Log.i("object field->" + field.getName() + " field type->" + field.getType());
//                Log.w("field type->" + field.get(data) + " set key->" + set.key());
//                if (field.getType() instanceof Object)
//                    Log.d("is object field");
//                else
//                    Log.e("not object");
//            }
//
//        } catch (IllegalAccessException e) {
//            Log.i(e.toString());
//        }
//    }

    public String creatTableCmd() {
        try {

            StringBuilder cmd = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            //SqlTableSet tableSet= clazz.getAnnotation(SqlTableSet.class);

            //Log.d("find Annotation "+tableSet+" table="+tableSet.table());
            cmd.append(tableName + " (");

            for (FieldSetPair pair : columnList) {
                cmd.append(pair.set.key() + " " + pair.set.attr().value + ",");
            }

            cmd.deleteCharAt(cmd.length() - 1);
            cmd.append(");");
            Log.i("sql table cmd->" + cmd.toString());

            return cmd.toString();
        } catch (Exception e) {
            Log.e(e.toString());
            return "";
        }
    }


    public class FieldSetPair {
        public Field field;
        public SqlColumnSet set;

        public FieldSetPair(Field f, SqlColumnSet s) {
            field = f;
            set = s;
        }
    }

}
