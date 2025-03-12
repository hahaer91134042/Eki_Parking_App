package com.eki.parking.Controller.impl


/**
 * Created by Hill on 2020/09/01
 */
interface IRuleCheck<T> {
    fun isInRule(factor:T): Boolean
}