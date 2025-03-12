package com.eki.parking.Controller.impl

/**
 * Created by Hill on 2020/02/12
 */
interface IFilterChangeBack<T> {
    fun onFilterChange(filters:IFilterSet.FilterList<T>)
    fun onSortChange(sortby:IFilterSet.SortBySet<T>)
}