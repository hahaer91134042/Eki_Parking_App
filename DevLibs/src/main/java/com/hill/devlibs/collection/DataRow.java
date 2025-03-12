package com.hill.devlibs.collection;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.hill.devlibs.tools.AppGson;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import androidx.annotation.Nullable;

/**
 * Created by Hill on 2019/6/21
 */
public class DataRow extends LinkedHashMap<String,String> {
    private ArrayList<String> list=new ArrayList<>();

    @Nullable
    @Override
    public String put(String key, String value) {
        list.add(value);
        return super.put(key, value);
    }

    public String getString(String key){
        return TextUtils.isEmpty(get(key))?"":get(key);
    }

    public int getInt(String key){
        try {
            return Integer.parseInt(get(key));
        }catch (Exception e){
            return 0;
        }
    }
    public boolean getBoolean(String key){
        try{
            return Boolean.parseBoolean(get(key));
        }catch (Exception e){
            return false;
        }
    }
    public Double getDouble(String key){
        try {
            return Double.parseDouble(get(key));
        }catch (Exception e){
            return 0.0d;
        }
    }
    public float getFloat(String key){
        try {
            return Float.parseFloat(get(key));
        }catch (Exception e){
            return 0.0f;
        }
    }
    public Long getLong(String key){
        try {
            return Long.parseLong(get(key));
        }catch (Exception e){
            return 0L;
        }
    }
    public <VO> VO getObj(String key,Class<VO> clazz){
        try {
            Gson gson=AppGson.creat();
            return gson.fromJson(get(key),clazz);
        }catch (Exception e){
            return null;
        }
    }

    public String getString(int index){
        return TextUtils.isEmpty(get(index))?"":get(index);
    }

    public int getInt(int index){
        try {
            return Integer.parseInt(get(index));
        }catch (Exception e){
            return 0;
        }
    }
    public boolean getBoolean(int index){
        try{
            return Boolean.parseBoolean(get(index));
        }catch (Exception e){
            return false;
        }
    }
    public <VO> VO getObj(int index,Class<VO> clazz){
        try {
            Gson gson= AppGson.creat();
            return gson.fromJson(get(index),clazz);
        }catch (Exception e){
            return null;
        }
    }

    public String get(int index){
        return list.get(index);
    }


}
