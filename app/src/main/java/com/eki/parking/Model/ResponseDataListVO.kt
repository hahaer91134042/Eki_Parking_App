package com.eki.parking.Model

import com.google.gson.annotations.SerializedName

/**
 * Created by Hill on 2018/10/31.
 */
abstract class ResponseDataListVO<DATA>:ResponseVO() {
    @SerializedName("DataList")
    var dataList=ArrayList<DATA>()

    open fun sortData(){

    }

    override fun clear() {

        dataList.clear()
    }

    override fun printValue() {
        printListData(dataList)
    }
}