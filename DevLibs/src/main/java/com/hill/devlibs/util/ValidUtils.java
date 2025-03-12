package com.hill.devlibs.util;


import com.hill.devlibs.R;
import com.hill.devlibs.tools.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

public class ValidUtils {


    public enum PhoneValidityEnum{
        SUCCESS(0),
        NO_PHONE_NUM(R.string.Please_enter_phone_number);

        public String cleanPhone = "";
        public int errorMsgRes ;
        PhoneValidityEnum(@StringRes int res) {
            errorMsgRes=res;
        }
        public PhoneValidityEnum setPhone(String num){
            cleanPhone=num;
            return this;
        }
    }

    public enum PwdValidityEnum {
        SUCCESS(0),
        NO_PWD(R.string.Please_enter_password),
        BETWEEN_6_12_CHAR(R.string.Password_must_be_between_6_and_12_characters),
        NEED_A_ENG(R.string.Password_requires_at_least_one_English_word),
        NEED_A_NUM(R.string.Password_requires_at_least_one_number),
        NEED_UPPER_CASE(R.string.At_least_one_capitalized_English),
        NEED_LOWER_CASE(R.string.At_least_one_lowercase_English),
        NEED_8_CHAR(R.string.At_least_8_characters);

        public String cleanPwd = "";
        public int errorMsgRes ;

        PwdValidityEnum(int r) {
            errorMsgRes = r;
        }

        public PwdValidityEnum setPwd(String p) {
            cleanPwd = p;
            return this;
        }
    }

    //mail valid=true else false
    public static Boolean checkMailValid(String mail){
        String regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
        Pattern p=Pattern.compile(regex);
        return p.matcher(mail).find();
    }

    public static String getCleanPhoneNum(String input) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(input);
        return String.valueOf(m.replaceAll("").trim());
    }

    public static String getCleanPwd(String input) {
        //去除字符串中的空格、回車、換行符、製表符 random, code
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        Matcher m = p.matcher(input);
        return String.valueOf(m.replaceAll("").trim());
    }

    public static PhoneValidityEnum checkPhoneValid(String phone){
        String cleanPhone=getCleanPhoneNum(phone);

        if (cleanPhone.length()<1)
            return PhoneValidityEnum.NO_PHONE_NUM;

        return PhoneValidityEnum.SUCCESS.setPhone(cleanPhone);
    }


    public static PwdValidityEnum checkPwdValid(String pwd) {

        return checkPwdValid(pwd,
                PwdValidityEnum.NEED_LOWER_CASE,PwdValidityEnum.NEED_UPPER_CASE,PwdValidityEnum.NEED_A_NUM,PwdValidityEnum.NEED_8_CHAR);
    }

    public static PwdValidityEnum checkPwdValid(String pwd , PwdValidityEnum... valids ){
        //String cleanPwd=getCleanPwd(pwd);

        if (pwd.length()<1)
            return PwdValidityEnum.NO_PWD;

        for (PwdValidityEnum valid : valids) {
            switch (valid){
                case BETWEEN_6_12_CHAR:
                    if (pwd.length()<6||pwd.length()>12)
                        return PwdValidityEnum.BETWEEN_6_12_CHAR;
                    break;
                case NEED_A_ENG:
                    if(!isOneEngCase(pwd))
                        return PwdValidityEnum.NEED_A_ENG;
                    break;
                case NEED_A_NUM:
                    if(!isOneNumber(pwd))
                        return PwdValidityEnum.NEED_A_NUM;
                    break;
                case NEED_UPPER_CASE:
                    if(!isOneUpperCase(pwd))
                        return PwdValidityEnum.NEED_UPPER_CASE;
                    break;
                case NEED_LOWER_CASE:
                    if (!isOneLowerCase(pwd))
                        return PwdValidityEnum.NEED_LOWER_CASE;
                    break;
                case NEED_8_CHAR:
                    if (!checkLength(pwd,8))
                        return PwdValidityEnum.NEED_8_CHAR;
                    break;
            }
        }

        return PwdValidityEnum.SUCCESS.setPwd(pwd);
    }

    private static boolean checkLength(String input,int length){
        return input.length()>=length;
    }

    /**
     * 判定數字
     *
     * @param input
     */
    private static boolean isOneNumber(String input) {
        /*
         * 判定數字格式
         */
        String regEx = "[^0-9]";//取出非0-9
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        String numberTex = String.valueOf(m.replaceAll("").trim());
//        Log.d("get number->" + numberTex);

        return numberTex.length() > 0;
    }

    private static boolean isOneUpperCase(String input) {
        String regEx = "[^A-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        String engTex = String.valueOf(m.replaceAll("").trim());
        //Log.d("Upper group count->"+m.groupCount());
        return engTex.length() > 0;
    }

    private static boolean isOneLowerCase(String input) {
        String regEx = "[^a-z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        String engTex = String.valueOf(m.replaceAll("").trim());
        //Log.d("Lower engTex->"+engTex+" length->"+engTex.length());
        return engTex.length() > 0;
    }

    /**
     * 判定英文
     *
     * @param input
     */
    private static boolean isOneEngCase(String input) {

        String regEx = "[^a-zA-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        String engTex = String.valueOf(m.replaceAll("").trim());

        return engTex.length() > 0;
    }

    private static boolean isOnlyOneEngCase(String input) {

        String regEx = "[^a-zA-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        String engTex = String.valueOf(m.replaceAll("").trim());

        return engTex.length() == 1;
    }

    public static Boolean checkIdNumber(String id) {

        if (id.length() != 10)//檢查全長
            return false;
        if (!isOnlyOneEngCase(id))//檢查始不是只有一個英文字
            return false;
        if (!isOneEngCase(id.substring(0, 1)))//檢查第一個字是不是英文
            return false;

//        if (isOneEngCase(id.substring(0,1)))
//            if (isOneEngCase(id.substring(1,2)))
//                return false;

        Boolean result = false;

        String v[] = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "X", "Y", "W", "Z", "I", "O"};
        int inte = -1;

        String s1 = String.valueOf(Character.toUpperCase(id.charAt(0)));

        for (int i = 0; i < 26; i++) {
            if (s1.compareTo(v[i]) == 0) {
                inte = i;
                break;
            }
        }
        int total = 0;
        int all[] = new int[11];
        String E = String.valueOf(inte + 10);
        int E1 = Integer.parseInt(String.valueOf(E.charAt(0)));
        int E2 = Integer.parseInt(String.valueOf(E.charAt(1)));
        all[0] = E1;
        all[1] = E2;

        for (int j = 2; j <= 10; j++)
            all[j] = Integer.parseInt(String.valueOf(id.charAt(j - 1)));
        for (int k = 1; k <= 9; k++)
            total += all[k] * (10 - k);
        total += all[0] + all[10];


        if (total % 10 == 0)
            result = true;    //驗証成功
//        else
//            result = false;    //驗証失敗
        return result;
    }
}
