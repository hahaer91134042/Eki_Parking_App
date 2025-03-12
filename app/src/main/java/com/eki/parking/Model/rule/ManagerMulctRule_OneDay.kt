package com.eki.parking.Model.rule

import com.eki.parking.Controller.impl.IMulctRule
import com.hill.devlibs.time.TimeSpan

/**
 * Created by Hill on 2020/09/01
 */
class ManagerMulctRule_OneDay:IMulctRule {
    override fun isInRule(factor: TimeSpan): Boolean =1 < factor.totalDays && factor.totalDays <= 3

    override fun mulctRatio(): Double =3.0

//    override fun compensationRatio(): Double = 1.0 / 2.0
//
//    override fun addtionalCompensation(priceSet: IPriceSet<Double>): Double =0.0
}