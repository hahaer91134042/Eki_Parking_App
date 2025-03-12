package com.eki.parking.Model.rule

import com.eki.parking.Controller.impl.IRuleCheck
import java.util.regex.Pattern

/**
 * Created by Hill on 2020/09/10
 */
class PhoneVectorRule:IRuleCheck<String> {
    override fun isInRule(factor: String): Boolean {

        var pattern= Pattern.compile("^\\/[0-9A-Z]{7}\$")
        if (!pattern.matcher(factor).find())
            return false

        return true
    }
}