package com.hill.devlibs.extension

import com.hill.devlibs.collection.CompareList

/**
 * Created by Hill on 2020/03/25
 */
fun <E> List<E>.cleanDuplic():ArrayList<E> =
        CompareList<E>().also { l-> forEach {l.addNoDuplicate(it)}}