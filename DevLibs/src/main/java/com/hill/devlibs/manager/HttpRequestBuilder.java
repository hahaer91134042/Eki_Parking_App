package com.hill.devlibs.manager;

import com.hill.devlibs.EnumClass.PostDataType;
import com.hill.devlibs.impl.BuildExecuter;
import com.hill.devlibs.model.bean.FormDataBody;
import com.hill.devlibs.tools.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Hill on 2019/6/21
 */
public class HttpRequestBuilder implements BuildExecuter<Request> {

    /**
     * "application/x-www-form-urlencoded"，是默认的MIME内容编码类型，一般可以用于所有的情况，但是在传输比较大的二进制或者文本数据时效率低。
     这时候应该使用"multipart/form-data"。如上传文件或者二进制数据和非ASCII数据。
     */
    public static final MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

    //既可以提交普通键值对，也可以提交(多个)文件键值对。
    public static final MediaType MEDIA_TYPE_MULTIPART_FORM = MediaType.parse("multipart/form-data;charset=utf-8");

    //只能提交二进制，而且只能提交一个二进制，如果提交文件的话，只能提交一个文件,后台接收参数只能有一个，而且只能是流（或者字节数组）
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain;charset=utf-8");

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");

    public static HttpRequestBuilder Connect(String url){
        return new HttpRequestBuilder(url);
    }

    Request.Builder builder = new Request.Builder();

    private String url;
    private HttpHeader header=new HttpHeader(this);
    private HttpBody body;
    HttpRequestBuilder(String url){
        this.url=url;
    }

    public HttpHeader getHeader(){
        return header;
    }



    public HttpJsonBody getJsonBody(){
        body=HttpBody.jsonBody(this);
        return (HttpJsonBody) body;
    }

    public HttpFormDataBody getFormDataBody(){
        body=HttpBody.dataBody(this);
        return (HttpFormDataBody)body;
    }

    public HttpRequestBuilder addBody(HttpBody body){
        this.body=body;
        return this;
    }

    @Override
    public Request build(){
        builder.url(url);

        header.into(builder);

        builder.post(body.getBody());

        return builder.build();
    }

    public static class HttpHeader implements BuildExecuter<Request>{
        private LinkedHashMap<String,String> headerMap=new LinkedHashMap<>();
        private HttpRequestBuilder parent;
        public HttpHeader(HttpRequestBuilder builder) {
            parent=builder;
        }

        public HttpHeader addAuthorization(String scheme,String token){
            headerMap.put("Authorization",scheme+" "+token);
            return this;
        }
        public HttpHeader addValuePair(String key,String value){
            headerMap.put(key,value);
            return this;
        }
        public HttpHeader addValuePair(Map<String,String> map){
            headerMap.putAll(map);
            return this;
        }

        public HttpJsonBody getJsonBody(){
            return parent.getJsonBody();
        }

        public HttpFormDataBody getDataBody(){
            return parent.getFormDataBody();
        }

        public HttpRequestBuilder addBody(HttpBody body){
            return parent.addBody(body);
        }


        public void into(Request.Builder builder) {
            for (Map.Entry<String,String> pair:
                    headerMap.entrySet()) {
                //Log.d("request add head key->"+pair.getKey()+" value->"+pair.getValue());
                builder.addHeader(pair.getKey(),pair.getValue());
            }
        }

        @Override
        public Request build() {
            return parent.build();
        }
    }


    public static abstract class HttpBody implements BuildExecuter<Request>{
        MediaType bodyType;
        HttpRequestBuilder parent;

        HttpBody(HttpRequestBuilder builder){
            parent=builder;
        }

        public HttpHeader getHeader(){
            return parent.getHeader();
        }

        public static HttpJsonBody jsonBody(HttpRequestBuilder builder){
            return new HttpJsonBody(builder);
        }
        public static HttpFormDataBody dataBody(HttpRequestBuilder builder){
            return new HttpFormDataBody(builder);
        }

        @Override
        public Request build() {
            return parent.build();
        }

        public abstract okhttp3.RequestBody getBody();
    }

    public static class HttpJsonBody extends HttpBody{
        public String jsonStr="";

        HttpJsonBody(HttpRequestBuilder builder) {
            super(builder);
            bodyType=PostDataType.JSON.type;
        }

        public void addJson(String json){
            jsonStr=json;
        }

        @Override
        public RequestBody getBody() {
            //Log.d("request add json type->"+bodyType+" value->"+jsonStr);
            return RequestBody.create(bodyType,jsonStr);

//            String encodStr="";
//            try {
//                encodStr=URLEncoder.encode(jsonStr,"UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                encodStr=URLEncoder.encode(jsonStr,"UTF-8").replace(" ", "%20");
//            }finally{
//                return RequestBody.create(bodyType,encodStr);
//            }

//            try {
//                return RequestBody.create(bodyType,new String(jsonStr.getBytes("ISO-8859-1"),"UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                return RequestBody.create(bodyType,jsonStr);
//            }
        }


    }

    public static class HttpFormDataBody extends HttpBody{

//        private LinkedHashMap<String,File> fileMap=new LinkedHashMap<>();


        public HttpFormDataBody(HttpRequestBuilder builder) {
            super(builder);
            //formdata 不用 因為要個別傳入
//            bodyType=data.type;
        }

//        public HttpFormDataBody addFile(String key, File file){
//            fileMap.put(key,file);
//            return this;
//        }
//        public HttpFormDataBody addFile(Map<String,File> pair){
//            fileMap.putAll(pair);
//            return this;
//        }

        private FormDataBody dataBody;
        public void addDataBody(FormDataBody body) {
            dataBody=body;
        }

        @Override
        public RequestBody getBody() {
            MultipartBody.Builder multiPartBody = new MultipartBody.Builder();
            multiPartBody.setType(PostDataType.MULTIPART_FORM.type);

            for (Map.Entry<String, FormDataBody.IFormDataSet> pair:
                 dataBody.getDataPair().entrySet()) {
                FormDataBody.IFormDataSet dataSet=pair.getValue();
                String key=pair.getKey();

                switch (dataSet.getType()){
                    case JPG:
                    case PNG:
                        RequestBody fileBody = RequestBody.create(dataSet.getType().type, dataSet.getFile());
                        multiPartBody.addFormDataPart(key,dataSet.getFile().getName(),fileBody);
                        break;
                    case TEXT:
                        multiPartBody.addFormDataPart(key,dataSet.getText());
                        break;
                        default:
                            throw new IllegalArgumentException("Not Support this type -"+dataSet.getType()+"- yet!!");
                }

            }

            return multiPartBody.build();
        }


    }
}
