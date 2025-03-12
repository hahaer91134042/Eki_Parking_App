package com.hill.devlibs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by Hill on 2019/6/12
 */
@Target( { TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlTableSet {
    String table() default "";
    boolean isClear() default true;
}
