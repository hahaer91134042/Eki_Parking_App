package com.eki.parking.Controller.sort

import com.eki.parking.Controller.impl.IFilterSet
import com.eki.parking.Model.sql.EkiOrder

/**
 * Created by Hill on 2020/02/13
 */
class OrderDescSort:IFilterSet.SortBySet<EkiOrder>{
    override fun sort(list: List<EkiOrder>): List<EkiOrder> =
            list.sortedByDescending { it.ReservaTime.startDateTime().toStamp() }
}