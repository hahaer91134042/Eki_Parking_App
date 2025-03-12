package com.eki.parking.Controller.filter

import com.eki.parking.Controller.impl.IFilterSet
import com.eki.parking.Model.sql.EkiOrder

/**
 * Created by Hill on 2020/02/13
 */
class NoneFilter: IFilterSet.Filter<EkiOrder>{
    override fun filter(data: EkiOrder): Boolean =true
}