package com.hill.devlibs.annotation;

import com.hill.devlibs.EnumClass.HttpBodyType;
import com.hill.devlibs.EnumClass.HttpProtocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Hill on 2019/6/17
 */
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiConfigSet {
    String path();
    HttpProtocol protocal() default HttpProtocol.GET;
    Class<?> response();
    boolean isAuth() default false;
    HttpBodyType bodyType() default HttpBodyType.JSON;
}
