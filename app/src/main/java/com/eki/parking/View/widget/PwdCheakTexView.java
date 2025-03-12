package com.eki.parking.View.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;


import com.eki.parking.R;
import com.hill.devlibs.tools.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Hill on 2018/4/14.
 */

public class PwdCheakTexView extends AppCompatEditText
                             implements View.OnFocusChangeListener {

    private CheckListener listener;

    public enum PwdValidityEnum {
        SUCCESS(0),
        NEED_6_CHAR(R.string.Password_must_be_between_6_and_12_characters),
        LESS_12_CHAR(R.string.Password_must_be_between_6_and_12_characters),
        NEED_A_ENG(R.string.Password_requires_at_least_one_English_word),
        NEED_A_NUM(R.string.Password_requires_at_least_one_number);

        public String cleanPwd="";
        public int errorMsgRes=0;
        PwdValidityEnum(int r) {
            errorMsgRes=r;
        }

        public PwdValidityEnum setPwd(String p) {
            cleanPwd=p;
            return this;
        }
    }

    public interface CheckListener {
        void onValid(String pwd);

        void onInValidPwd(PwdValidityEnum validity);
    }

    public PwdCheakTexView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setBackground(null);
        setOnFocusChangeListener(this);
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public PwdCheakTexView setCheckListener(CheckListener l) {
        listener = l;
        return this;
    }

    // TODO:Edit by Hill 2018/4/16 這之後要改成用RegisterUtils
    public void checkPwdVality(){

        String pwd = cleanInput(getText().toString().trim());
        Log.INSTANCE.i("input TEX->" + pwd);
        if (!isOneNumber(pwd)){
            if (listener != null)
                listener.onInValidPwd(PwdValidityEnum.NEED_A_NUM);
        }else if (!isOneEngCase(pwd)){
            if (listener != null)
                listener.onInValidPwd(PwdValidityEnum.NEED_A_ENG);
        }else if (pwd.length()<6){
            if (listener != null)
                listener.onInValidPwd(PwdValidityEnum.NEED_6_CHAR);
        }else if (pwd.length()>12){
            if (listener != null)
                listener.onInValidPwd(PwdValidityEnum.LESS_12_CHAR);
        }else {
            if (listener != null)
                listener.onValid(pwd);
        }
        listener.onValid("");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.INSTANCE.w("hasFocuse->" + hasFocus);
        if (!hasFocus) {
            checkPwdVality();
        }
    }

    private String cleanInput(String input) {
        //去除字符串中的空格、回車、換行符、製表符 random, code
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        Matcher m = p.matcher(input);
        return String.valueOf(m.replaceAll("").trim());
    }

//    private boolean isRandom(String input){
//        // 只允許字母和數字
//        // String regEx = "[^a-zA-Z0-9]";
//        // 清除掉所有特殊字符
//        String regEx= "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（） ——+|{}【】'；：”“'。，、？]" ;
//        Pattern p = Pattern.compile(regEx);
//        Matcher m = p.matcher(ratio);
//        return    m.replaceAll( "" ).trim();
//    }

    /**
     * 判定數字
     *
     * @param input
     */
    private boolean isOneNumber(String input) {
        /*
		 * 判定數字格式
		 */
        String regEx = "[^0-9]";//取出非0-9
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        String numberTex = String.valueOf(m.replaceAll("").trim());
//        PrintLogKt.INSTANCE.d("get number->" + numberTex);
        if (numberTex.length() > 0)
            return true;
        return false;
    }

    /**
     * 判定英文
     *
     * @param input
     */
    private boolean isOneEngCase(String input) {

        String regEx = "[^a-zA-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        String engTex = String.valueOf(m.replaceAll("").trim());
        if (engTex.length() > 0)
            return true;
        return false;
    }
}
