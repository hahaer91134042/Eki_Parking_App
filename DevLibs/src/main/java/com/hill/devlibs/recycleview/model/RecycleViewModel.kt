package com.hill.devlibs.recycleview.model

import androidx.annotation.LayoutRes
import com.hill.devlibs.impl.IRecycleViewModelSet


/**
 * Created by Hill on 2020/01/16
 */
open class RecycleViewModel<T>(override val viewType: Int, override val data: T?=null):IRecycleViewModelSet<T>{
}