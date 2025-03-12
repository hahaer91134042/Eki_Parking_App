package com.eki.parking

import com.hill.devlibs.tools.ValidCheck

/**
 * Created by Hill on 2019/12/17
 */
class EkiAccountCheck: ValidCheck() {
    override val accountRule: CheckRule<AccountCheck>
        get() = CheckRule(AccountCheck.Phone,
                AccountCheck.Email)

    override val pwdRule: CheckRule<PwdCheck>
        get() = CheckRule(PwdCheck.One_Number_Char,
                PwdCheck.One_Upper_Case,
                PwdCheck.One_Lower_Case,
                PwdCheck.Need_8_Char)
}