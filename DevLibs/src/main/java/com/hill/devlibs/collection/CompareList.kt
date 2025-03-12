package com.hill.devlibs.collection

import com.hill.devlibs.impl.ICompareDuplicate
import com.hill.devlibs.impl.ICompareEqual

/**
 * Created by Hill on 03,10,2019
 */
open class CompareList<E>:ArrayList<E>(){

    fun contain(l:ICompareDuplicate<E>):Boolean{
        var isContain=false
        forEach{
            if (l.isDuplicate(it))
                isContain=true
        }
        return isContain
    }

    fun contain(l:(E)->Boolean):Boolean{
        return contain(object :ICompareDuplicate<E>{
            override fun isDuplicate(ori: E): Boolean =l(ori)
        })
    }

    fun find(l:ICompareEqual<E>):E?{
        forEach {
            if (l.isEuqal(it))
                return it
        }
        return null
    }
    fun find(l:(E)->Boolean):E?{
        return find(object :ICompareEqual<E>{
            override fun isEuqal(ori: E): Boolean=l(ori)
        })
    }

    fun addNoDuplicate(data:E,l:ICompareDuplicate<E>?=null){
        if (l==null){
            if (!this.any { it==data })
                add(data)
        }else{
            if (!contain(l))
                add(data)
        }
    }
    fun hasDuplicate(l:ICompareDuplicate<E>):Boolean{
        var hasD=false
        forEach {
            if(l.isDuplicate(it))
                hasD=true
        }
        return hasD
    }
    fun hasDuplicate(l:(E)->Boolean):Boolean{
        return hasDuplicate(object :ICompareDuplicate<E>{
            override fun isDuplicate(ori: E): Boolean =l(ori)
        })
    }
}