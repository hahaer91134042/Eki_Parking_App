package com.hill.devlibs.impl

/**
 * Created by Hill on 2020/04/23
 */
interface ICryptoSet:IEncodeSet {
    fun key():String
    fun cipher():String
}