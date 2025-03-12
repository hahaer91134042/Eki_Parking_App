package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.sql.EkiOrder
import com.hill.devlibs.extension.printList
import com.hill.devlibs.list.ConvertSqlList

/**
 * Created by Hill on 12,11,2019
 */
class LoadOrderResponse:ResponseVO() {

    var info=ConvertSqlList<ResponseInfo.OrderResult,EkiOrder>()

    override fun printValue() {
        super.printValue()
        info.printList()
    }
}