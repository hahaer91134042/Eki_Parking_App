package com.hill.devlibs.annotation.parse;

import com.hill.devlibs.EnumClass.HttpBodyType;
import com.hill.devlibs.EnumClass.HttpProtocol;
import com.hill.devlibs.annotation.ApiConfigSet;
import com.hill.devlibs.annotation.ServerSet;

import androidx.annotation.NonNull;

/**
 * Created by Hill on 2019/6/17
 */
public class ApiParser {
    public Class<?> clazz;
    public Enum enumValue;
    public String pathStr="";
    public String serverUrl="";
    public @NonNull
    Class<?> response;
    public HttpProtocol protocol=HttpProtocol.GET;
    public boolean isAuth=false;
    public HttpBodyType bodyType;


    public ApiParser(Enum e) {
        this.clazz=e.getClass();
        enumValue=e;
        parse();
    }

    private void parse() {
        try {
            ServerSet serverSet=clazz.getAnnotation(ServerSet.class);
//            Log.i("clazz->"+clazz+" path->"+set.path()+" protocol->"+set.protocal());
//            Log.d("enum name->"+enumValue.name());
            serverUrl=serverSet.url();

            ApiConfigSet apiSet=clazz.getField(enumValue.name()).getAnnotation(ApiConfigSet.class);
            //Log.w("enum type set path->"+set2.path()+" protocol->"+set2.protocal());
            //pathStr= StringUtil.getFormateMessage("/{0}/{1}",set.path(),set2.path());
            pathStr=apiSet.path();
            response=apiSet.response();
            isAuth=apiSet.isAuth();
            bodyType=apiSet.bodyType();

            //Log.d("complete path->"+pathStr);
            protocol=apiSet.protocal();
        }catch (Exception e){
            throw new NullPointerException("No Such Annotation!!");
        }
    }


    public static ApiParser parse(Enum e){
        return new ApiParser(e);
    }
}
