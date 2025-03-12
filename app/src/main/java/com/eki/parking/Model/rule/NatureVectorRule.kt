package com.eki.parking.Model.rule

import com.eki.parking.Controller.impl.IRuleCheck
import java.util.regex.Pattern

/**
 * Created by Hill on 2020/09/10
 */
class NatureVectorRule:IRuleCheck<String> {
    /*
    * 1.16碼
    * 2.開頭2位大寫英文
    * 3.14位數字
    * */
    override fun isInRule(factor: String): Boolean {
        if (factor.length!=16)
            return false

        var pattern=Pattern.compile("^[a-zA-Z]{2}[0-9]{14}\$")
        if (!pattern.matcher(factor).find())
            return false

        return true
    }
}