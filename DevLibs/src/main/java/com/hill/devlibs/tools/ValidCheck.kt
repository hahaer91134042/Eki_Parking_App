package com.hill.devlibs.tools

import com.hill.devlibs.R
import com.hill.devlibs.extension.isEmpty
import com.hill.devlibs.util.StringUtil
import java.util.regex.Pattern

/**
 * Created by Hill on 2019/12/05
 */
abstract class ValidCheck {

    data class CheckResult(val valid:Boolean,val value:CheckError)
    protected class CheckRule<T>(vararg rule:T){
        var ruleList=rule.toMutableList()
    }

    private interface ICheckValid{
        fun check(input:String):CheckResult
    }
    enum class AccountCheck(val regex:String):ICheckValid{
        Phone("[0-9]") {
            override fun check(input: String):CheckResult {
                //這邊只能輸入數字
                var pattern=Pattern.compile(regex)
                var matcher=pattern.matcher(input)
//                Log.i("PhoneCheck->$matcher")
                var check=matcher.replaceAll("").trim().isEmpty()
                return CheckResult(check,when(check){
                    true->CheckError.SUCCESS
                    else->CheckError.PHONE_ERROE
                })
            }
        },
        Email("^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$") {
            override fun check(input: String):CheckResult {
                var check=Pattern.compile(regex).matcher(input).find()
                return CheckResult(check,when(check){
                    true->CheckError.SUCCESS
                    else->CheckError.MAIL_ERROR
                })
            }
        }
    }
    enum class PwdCheck(val regex:String):ICheckValid{
        One_Eng_Char("[a-zA-Z]") {
            override fun check(input: String): CheckResult {
                var check=Pattern.compile(regex).matcher(input).find()
                return CheckResult(check,when(check){
                    true->CheckError.SUCCESS
                    else->CheckError.NEED_A_ENG
                })
            }
        },
        One_Number_Char("[0-9]") {
            override fun check(input: String): CheckResult {
                var check=Pattern.compile(regex).matcher(input).find()
                return CheckResult(check,when(check){
                    true->CheckError.SUCCESS
                    else->CheckError.NEED_A_NUM
                })
            }
        },
        One_Upper_Case("[A-Z]") {
            override fun check(input: String): CheckResult {
                var check=Pattern.compile(regex).matcher(input).find()
                return CheckResult(check,when(check){
                    true->CheckError.SUCCESS
                    else->CheckError.NEED_UPPER_CASE
                })
            }
        },
        One_Lower_Case("[a-z]") {
            override fun check(input: String): CheckResult {
                var check=Pattern.compile(regex).matcher(input).find()
                return CheckResult(check,when(check){
                    true->CheckError.SUCCESS
                    else->CheckError.NEED_LOWER_CASE
                })
            }
        },
        Need_8_Char("") {
            override fun check(input: String): CheckResult {
                var check=input.length>=8
                return CheckResult(check,when(check){
                    true->CheckError.SUCCESS
                    else->CheckError.NEED_8_CHAR
                })
            }
        }
    }

    enum class CheckError(val errorStr:Int){
        SUCCESS(0),
        NO_PWD(R.string.Please_enter_password),
        NO_ACCOUNT(R.string.Please_enter_your_account),
        NO_EMAIL(R.string.Please_enter_email),
        NO_PHONE(R.string.Please_enter_phone_number),
        ACCOUNT_ERROR(R.string.Account_input_format_error),
        PWD_ERROR(R.string.Bad_password_format),
        MAIL_ERROR(R.string.Mail_format_error),
        PHONE_ERROE(R.string.Phone_format_error),
        NEED_A_ENG(R.string.Password_requires_at_least_one_English_word),
        NEED_A_NUM(R.string.Password_requires_at_least_one_number),
        NEED_UPPER_CASE(R.string.At_least_one_capitalized_English),
        NEED_LOWER_CASE(R.string.At_least_one_lowercase_English),
        NEED_8_CHAR(R.string.At_least_8_characters);
    }

    protected abstract val accountRule:CheckRule<AccountCheck>
    protected abstract val pwdRule:CheckRule<PwdCheck>

    fun checkAccountValid(input:String):CheckResult{
        if (input.isEmpty())
            return CheckResult(false,CheckError.NO_ACCOUNT)

        var resultList=ArrayList<CheckResult>()
        accountRule.ruleList.forEach {
            resultList.add(it.check(cleanChar(input)))
        }
//        Log.w("acc CheckList->$resultList")
        var check=resultList.any { it.valid }
        return CheckResult(check,when(check){
            true->CheckError.SUCCESS
            else->CheckError.ACCOUNT_ERROR
        })
    }

    fun checkPwdValid(input: String):CheckResult{
        if (input.isEmpty())
            return CheckResult(false,CheckError.NO_PWD)

//        var resultList=ArrayList<CheckResult>()
        pwdRule.ruleList.forEach {
            var result=it.check(cleanChar(input))
//            Log.i("pwd check->$it  result->$result")
            if (!result.valid)
                return result
        }
        return CheckResult(true,CheckError.SUCCESS)
    }
    private fun cleanChar(input: String):String=StringUtil.removeSpaceChars(StringUtil.cleanChar(input))

    companion object{
        fun mail(input:String):CheckResult{
            var mail=StringUtil.cleanChar(input)
            if (mail.isNullOrEmpty())
                return CheckResult(false,CheckError.NO_EMAIL)
            return AccountCheck.Email.check(mail)
        }
        fun phone(input:String):CheckResult{
            var phone=StringUtil.cleanChar(input)
            if (phone.isNullOrEmpty())
                return CheckResult(false,CheckError.NO_PHONE)
            return AccountCheck.Phone.check(phone)
        }
    }
}