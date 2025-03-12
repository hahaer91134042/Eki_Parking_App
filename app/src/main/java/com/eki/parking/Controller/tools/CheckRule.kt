package com.eki.parking.Controller.tools

import com.eki.parking.Controller.impl.IMulctRule
import com.eki.parking.Model.rule.*

/**
 * Created by Hill on 2020/09/01
 */

object CheckRule {

    @JvmStatic
    val managerMulctRules=ArrayList<IMulctRule>().also {
        it.add(ManagerMulctRule_In24Hour())
        it.add(ManagerMulctRule_OneDay())
        it.add(ManagerMulctRule_ThreeDay())
        it.add(ManagerMulctRule_OneWeek())
        it.add(ManagerMulctRule_TwoWeek())
    }

    @JvmStatic
    val natureVectorRule=NatureVectorRule()
    @JvmStatic
    val phoneVectorRule=PhoneVectorRule()

    @JvmStatic
    val orderAllCheckoutRule=CheckOrderAllCheckoutRule()

}