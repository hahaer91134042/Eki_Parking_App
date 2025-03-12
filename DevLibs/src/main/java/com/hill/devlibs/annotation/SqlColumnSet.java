package com.hill.devlibs.annotation;

import com.hill.devlibs.annotation.feature.SqlAttribute;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Created by Hill on 2019/6/12
 */
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlColumnSet {
    String key();
    SqlAttribute attr();
    int order() default 99;

}
