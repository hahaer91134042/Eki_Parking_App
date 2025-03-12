package com.hill.devlibs.model.bean;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hill.devlibs.model.ValueObject;
import com.hill.devlibs.tools.AppGson;

/**
 * Created by Hill on 2018/4/12.
 */

public abstract class ServerRequestBody extends ValueObject {

//    public String bodyStr="";

//    @Override
//    public final boolean init(String msg) {
//        if (!StringUtil.isEmptyString(msg)){
//            bodyStr=msg;
//            return true;
//        }
//        return false;
//    }

    public String getBodyStr(){
        return AppGson.creat().toJson(this);
    }




//    public static class Builder<BODY extends ServerRequestBody>{
//
//        private ServerRequest<BODY> mRequest;
//
//        public Builder(ServerRequest<BODY> request) {
//            mRequest=request;
//        }
//
//        public ServerRequestBody creat(){
//            try {
//                BODY body=mRequest.getBody();
//
//                if (body.init(new Gson().toJson(body))){
//                    return body;
//                }else {
//                    return null;
//                }
//            }catch (Exception e){
//                Log.e(e.toString());
//            }
//            throw new InputMismatchException("key name miss paire!!");
//        }
//    }
}
