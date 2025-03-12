package com.eki.parking.Controller.impl

import kotlinx.coroutines.*

/**
 * Created by Hill on 2020/02/10
 */
abstract class IFilterSet<T>:CoroutineScope by MainScope(){
    class FilterList<T>(vararg set:Filter<T>) : ArrayList<Filter<T>>() {
        init { addAll(set) }
    }
    interface Filter<T>{
        fun filter(data:T):Boolean
    }
    interface SortBySet<T>{
        fun sort(list:List<T>):List<T>
    }

    var filters:FilterList<T> =filters()

    //預設可以不排序
    var sortBy:SortBySet<T> =sortBy()


    open fun sortBy():SortBySet<T> =object :SortBySet<T>{
        override fun sort(list: List<T>):List<T> = list
    }
    open fun filters():FilterList<T> = FilterList()

    private fun runFilterSet(list:List<T>): List<T> {
        var result=list
        filters.forEach {f->
            result=result.filter { f.filter(it) }
        }
        return result
    }

    fun filter(list:List<T>):List<T> =
            sortBy.sort(runFilterSet(list))
    //大量過濾再用
    fun filterAsync(list: List<T>,back:(List<T>)->Unit)=
            async(Dispatchers.IO) {
                var result= filter(list)
                launch(Dispatchers.Main){
                    back(result)
                }
            }
}