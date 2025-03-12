package com.hill.devlibs.collection;

import com.hill.devlibs.sql.SqlPair;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import androidx.annotation.Nullable;

/**
 * Created by Hill on 2019/6/17
 */
public class SqlList<E extends SqlPair> extends ArrayList<E> {
    private LinkedHashMap<Class<?>,E> clazzMap=new LinkedHashMap<>();

    //會閃退 不知道為啥
//    public SqlList(E... args){
//        for (E arg : args) {
//            clazzMap.put(arg.clazz,arg);
//        }
//    }

    @Override
    public boolean add(E e) {
        clazzMap.put(e.clazz,e);
        return super.add(e);
    }


    public boolean contains(@Nullable Class<?> o) {
        return clazzMap.containsKey(o);
    }

    public E get(Class<?> clazz){
        return clazzMap.get(clazz);
    }
}
