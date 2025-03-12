package com.eki.parking.Controller.filter

import com.eki.parking.Controller.impl.IFilterSet
import com.eki.parking.Model.sql.EkiOrder
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.ext.monthEqual

/**
 * Created by Hill on 2020/02/13
 */
class OrderDateFilter(var date: DateUnit):IFilterSet.Filter<EkiOrder> {
    override fun filter(data: EkiOrder): Boolean {
        var start = data.ReservaTime.startDateTime().date
        return date.monthEqual(start)
//        return (start.year == date.year) && (start.month == date.month)
    }
}