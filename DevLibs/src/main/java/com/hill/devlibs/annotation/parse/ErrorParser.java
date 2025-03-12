package com.hill.devlibs.annotation.parse;

import com.hill.devlibs.annotation.ErrorCodeSet;

import androidx.annotation.StringRes;

/**
 * Created by Hill on 2019/6/17
 */
public class ErrorParser {
    public Class<?> clazz;
    public Enum enumValue;
    public @StringRes int msgRes;


    public ErrorParser(Enum e) {
        this.clazz=e.getClass();
        enumValue=e;
        parse();
    }

    private void parse() {
        try {

            ErrorCodeSet set=clazz.getField(enumValue.name()).getAnnotation(ErrorCodeSet.class);
            msgRes=set.msgRes();

        }catch (Exception e){
            throw new NullPointerException("No Such Annotation!!");
        }
    }


    public static ErrorParser parse(Enum e){
        return new ErrorParser(e);
    }
}
