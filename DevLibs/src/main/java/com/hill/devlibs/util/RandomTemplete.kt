package com.hill.devlibs.util

/**
 * Created by Hill on 2020/04/22
 */
class RandomTemplete(val templete: String) {
    companion object{
        val number=RandomTemplete("0123456789")
        val upperCase=RandomTemplete("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        val lowerCase=RandomTemplete("abcdefghijklmnopqrstuvwxyz")
        val all=RandomTemplete("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
    }
}