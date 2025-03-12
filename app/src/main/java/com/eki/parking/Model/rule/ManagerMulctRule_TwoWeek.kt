package com.eki.parking.Model.rule

import com.eki.parking.Controller.impl.IMulctRule
import com.hill.devlibs.time.TimeSpan

/**
 * Created by Hill on 2020/09/01
 */
class ManagerMulctRule_TwoWeek:IMulctRule {
    override fun isInRule(factor: TimeSpan): Boolean =factor.totalDays>14

    override fun mulctRatio(): Double =0.0

//    override fun compensationRatio(): Double =0.0
//
//    override fun addtionalCompensation(priceSet: IPriceSet<Double>): Double =0.0
}