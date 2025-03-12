package com.eki.parking.Model.rule

import com.eki.parking.Controller.impl.IRuleCheck
import com.eki.parking.Model.sql.EkiOrder

/**
 * Created by Hill on 2020/09/14
 */
class CheckOrderAllCheckoutRule:IRuleCheck<List<EkiOrder>> {
    //這邊要檢查使用者的訂單是否都checkout了 跟付款 沒有要強制付款

    override fun isInRule(factor: List<EkiOrder>): Boolean {

        if (factor.any { it.isBeSettle() || it.isOverCheckOut() }){
            return true
        }

        return false
    }
}