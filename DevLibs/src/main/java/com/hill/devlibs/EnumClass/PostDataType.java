package com.hill.devlibs.EnumClass;

import okhttp3.MediaType;

/**
 * Created by Hill on 2019/6/21
 */
public enum PostDataType {
    PNG(MediaType.parse("image/png")),
    JPG(MediaType.parse("image/jpg")),
    JSON(MediaType.parse("application/json;charset=utf-8")),
    //JSON(MediaType.parse("application/json")),
    TEXT(MediaType.parse("text/plain;charset=utf-8")),
    STREAM(MediaType.parse("application/octet-stream")),
    MULTIPART_FORM(MediaType.parse("multipart/form-data;charset=utf-8")),
    NORAML_FORM(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"))
    ;

    public MediaType type;
    PostDataType(MediaType s) {
        type =s;
    }
}
