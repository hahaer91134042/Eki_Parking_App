package com.hill.devlibs.sql;

import com.hill.devlibs.annotation.parse.TableParser;
import com.hill.devlibs.tools.Log;

/**
 * Created by Hill on 2019/6/17
 */
public class SqlPair {
    public Class<?> clazz;
    public TableParser parser;
    public String tableName;
    public SqlPair(Class<?> clazz){
        this.clazz=clazz;
        parser=TableParser.parse(clazz);
        tableName=parser.tableName;
    }
}
