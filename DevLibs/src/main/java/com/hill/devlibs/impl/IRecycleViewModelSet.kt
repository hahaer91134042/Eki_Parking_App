package com.hill.devlibs.impl

import com.hill.devlibs.recycleview.model.RecycleViewModel

/**
 * Created by Hill on 2020/01/22
 */
interface IRecycleViewModelSet<T> {
    val viewType:Int
    val data:T?
}