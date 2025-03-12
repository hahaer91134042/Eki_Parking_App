package com.hill.devlibs.model.bean;


import com.hill.devlibs.annotation.parse.ApiParser;
import com.hill.devlibs.EnumClass.PostDataType;
import com.hill.devlibs.model.ValueObjContainer;
import com.hill.devlibs.model.ValueObject;
import com.hill.devlibs.tools.Log;
import com.hill.devlibs.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Created by Hill on 2017/11/28.
 */

public abstract class ServerRequest<BODY extends ServerRequestBody> extends ValueObject {


    //public String queryStr="";

    public String serialCode ="";
    private UrlPaireBuilder paireBuilder=new UrlPaireBuilder();
    public String url;
    public String scheme;
    public String token;
    public LinkedHashMap<String,String> headerPair =new LinkedHashMap<>();
    public ApiParser apiConfig;
//    public FormDataSet formDataSet =new FormDataSet();

    public BODY body;


    public  BODY getBody(){
        return body;
    }




    public <V> ServerRequest setSerialCode(ArrayList<V> list){
//        StringBuilder builder=new StringBuilder();
//        for (String code:
//             list) {
//            builder.append(code+",");
//        }
//        builder.replace(builder.length()-1,builder.length(),"");
        serialCode = StringUtil.linkWithSymbol(",",list);
        return this;
    }



    public ServerRequest setPair(String key,String value){
        paireBuilder.setPair(key,value);
        return this;
    }
    public ServerRequest setUrl(String s){
        url=s;
        return this;
    }
    public String getPairStr(){
        paireBuilder.build();
        return paireBuilder.pairStr;
    }

    public String getPairUrl(){
        return url+"?"+getPairStr();
    }




    @Override
    public void printValue() {
        Log.i("Request Value:body->"+body);
    }

//    @Override
//    public ValueObjContainer getContainer() {
//        return null;
//    }


//    public static class FormDataSet{
//        public PostDataType dataType=PostDataType.JPG;
//        public LinkedHashMap<String, File> filePair=new LinkedHashMap<>();
//    }

    public static class UrlPaireBuilder {
        public HashMap<String,String> map=new HashMap<>();
        public String pairStr="";

        public UrlPaireBuilder setPair(String k, String v){
            map.put(k,v);
            return this;
        }

        public UrlPaireBuilder build(){

            StringBuffer builder = new StringBuffer();
            for (HashMap.Entry<String, String> pair :
                    map.entrySet()) {
                builder.append(pair.getKey() + "=" + pair.getValue() + "&");
            }
            builder.replace(builder.length() - 1, builder.length(), "");
            pairStr = builder.toString();

            return this;
        }

        public void clean() {
            map.clear();
            pairStr="";
        }
    }

}
