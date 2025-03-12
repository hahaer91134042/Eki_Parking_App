package com.eki.parking.Model

import com.google.gson.annotations.SerializedName
import com.hill.devlibs.model.ValueObject

/**
 * Created by Hill on 2018/10/31.
 */
abstract class ListInVO<DATA>:ValueObject() {
    @SerializedName("List")
    var List= ArrayList<DATA>()

    override fun clear() {
        List.clear()
    }

    override fun printValue() {
        printListData(List)
    }
}