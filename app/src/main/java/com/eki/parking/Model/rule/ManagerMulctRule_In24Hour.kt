package com.eki.parking.Model.rule

import com.eki.parking.Controller.impl.IMulctRule
import com.hill.devlibs.time.TimeSpan

/**
 * Created by Hill on 2020/09/01
 */
class ManagerMulctRule_In24Hour:IMulctRule {
    override fun isInRule(factor: TimeSpan): Boolean = factor.totalHour <= 24

    override fun mulctRatio(): Double =5.0
}