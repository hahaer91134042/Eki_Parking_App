package com.hill.devlibs.annotation

/**
 * Created by Hill on 25,09,2019
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionFieldSet(
        val Name:String,
        val Code:Int
)