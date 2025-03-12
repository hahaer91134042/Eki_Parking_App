package com.hill.devlibs.annotation.feature;

/**
 * Created by Hill on 2019/6/12
 */
public enum SqlAttribute {
    ID("INTEGER PRIMARY KEY"),
    INT("INTEGER DEFAULT 0"),
    DOUBLE("TEXT DEFAULT 0.0"),//sqlite 沒有double可用的
    FLOAT("TEXT DEFAULT 0.0"),
    LONG("TEXT DEFAULT 0"),
    BOOLEAN("TEXT DEFAULT false"),
    TEXT("TEXT NOT NULL"),
    Obj("TEXT NOT NULL"),
    Array("TEXT NOT NULL");

    public String value;
    SqlAttribute(String v) {
        value=v;
    }
}
