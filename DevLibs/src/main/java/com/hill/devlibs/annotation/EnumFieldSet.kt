package com.hill.devlibs.annotation

/**
 * Created by Hill on 25,09,2019
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnumFieldSet(
        val Int:Int=0,
        val Name:String=""
)