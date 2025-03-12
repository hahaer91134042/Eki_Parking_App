package com.hill.devlibs.manager;

import android.content.Context;


import com.hill.devlibs.BaseApp;
import com.hill.devlibs.impl.IHttpsSet;
import com.hill.devlibs.model.bean.FormDataBody;
import com.hill.devlibs.model.bean.ServerRequestBody;
import com.hill.devlibs.model.bean.ServerRequest;
import com.hill.devlibs.tools.AppGson;
import com.hill.devlibs.tools.Log;
import com.hill.devlibs.util.StringUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import androidx.collection.ArrayMap;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//import java.security.cert.X509Certificate;

public abstract class LibHttpManager<SR extends ServerRequest,Body extends ServerRequestBody> extends LibBaseManager {

    public static int HTTP_STATUS_CODE;
//    private OkHttpClient httpClient;
    private HttpConnectSetting connectSet=setConnectSetting();

    private OkHttpClient sslClient;
    private OkHttpClient httpClient;

    protected abstract HttpConnectSetting setConnectSetting();
//    public int connectTimeout = 10,
//            writeTimeout = 10,
//            readTimeout = 30;

    protected class HttpConnectSetting{
        public int connectTimeout,
                writeTimeout,
                readTimeout;
        public HttpConnectSetting(int connectTimeout,int writeTimeout,int readTimeout){
            this.connectTimeout=connectTimeout;
            this.writeTimeout=writeTimeout;
            this.readTimeout=readTimeout;
        }
    }

    public LibHttpManager(Context context) {
        super(context);
        sslClient=createSSLClient();
        httpClient=createDefaultClient();
    }

//    public LibHttpManager(Context context, boolean isSSL) {
//        super(context);
//        if (isSSL)
//            httpClient = createSSLClient();
//        else
//            httpClient = createDefaultClient();
//
//        errorMsg = getStringArr(R.array.responseMsg);
//    }

    public String sendGetRequest(SR serverRequest) throws IOException {

        String url = serverRequest.getPairUrl();

        OkHttpClient client= checkSslConnect(url);

//        url = url + "?" + car1Request.getPairStr();

        Log.INSTANCE.i(TAG + " connect url->" + url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            HTTP_STATUS_CODE = response.code();
            String bodyStr = response.body().string().trim();
            Log.INSTANCE.w(TAG + " get response->" + bodyStr + " status code->" + HTTP_STATUS_CODE);

            return bodyStr;
        } catch (IOException e) {
            throw e;
        }
    }

    public String sendGetRequest(String url, ArrayMap<String, String> pairs) throws IOException {

        OkHttpClient client= checkSslConnect(url);

        if (pairs != null) {
            StringBuffer builder = new StringBuffer(url + "?");
            for (ArrayMap.Entry<String, String> pair :
                    pairs.entrySet()) {
                builder.append(pair.getKey() + "=" + pair.getValue() + "&");
            }
            builder.replace(builder.length() - 1, builder.length(), "");
            url = builder.toString();
        }

        Log.INSTANCE.i(TAG + " connect url->" + url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            HTTP_STATUS_CODE = response.code();
            String bodyStr = response.body().string().trim();
            Log.INSTANCE.w(TAG + " get response->" + bodyStr + " status code->" + HTTP_STATUS_CODE);

            return bodyStr;
        } catch (IOException e) {
            throw e;
        }
    }


    public String sendPostRequest(String url, ArrayMap<String, String> pairs) throws IOException {

        OkHttpClient client= checkSslConnect(url);

        FormBody.Builder formBody = new FormBody.Builder();
        if (pairs != null) {
            for (ArrayMap.Entry<String, String> pair :
                    pairs.entrySet()) {
                formBody.add(pair.getKey(), pair.getValue());
            }
        }

        Log.INSTANCE.i(TAG + " connect url->" + url);
        okhttp3.RequestBody body = formBody.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            HTTP_STATUS_CODE = response.code();
            String bodyStr = response.body().string().trim();
            Log.INSTANCE.largeW(TAG, " get response->" + bodyStr + " status code->" + HTTP_STATUS_CODE);

            return bodyStr;
        } catch (IOException e) {
            throw e;
        }
    }
    public String sendPostRequest(ServerRequest request) throws IOException{
        if(request==null)
            throw new NullPointerException(TAG+"->Server Request is Null!!");
        OkHttpClient client= checkSslConnect(request.url);

        HttpRequestBuilder builder=HttpRequestBuilder.Connect(request.url);

        if(request.apiConfig.isAuth){
            if (StringUtil.isEmptyString(request.token))
                throw new NullPointerException("No Such Authorization Token!!");
            builder.getHeader().addAuthorization(request.scheme,request.token);
        }
        builder.getHeader().addValuePair(request.headerPair);
        switch (request.apiConfig.bodyType){
            case JSON:
                builder.getJsonBody().addJson(request.body.getBodyStr());
                break;
            case FORMDATA:

                if (request.body instanceof FormDataBody){
                    FormDataBody body=(FormDataBody)request.body;
                    Log.w("FormData body->"+ AppGson.creat().toJson(body));
                    builder.getFormDataBody().addDataBody(body);
                }else {
                    throw new IllegalArgumentException("HttpFormDataBody class type is Illegal!!");
                }
                break;
        }
        Request httpRequest=builder.build();
        Call httpCall = client.newCall(httpRequest);
        
        Response response = httpCall.execute();

        HTTP_STATUS_CODE = response.code();

        String bodyStr = response.body().string().trim();
        Log.largeW(TAG, " post response->" + bodyStr + " status code->" + HTTP_STATUS_CODE);

        return bodyStr;
    }

    public String sendPostRequest(String urlStr, Body requestBody) throws IOException {

        if (requestBody == null)
            throw new NullPointerException(TAG + "->ServerRequestBody is null!!");

        OkHttpClient client= checkSslConnect(urlStr);

        MediaType bodyType = MediaType.parse("application/json; charset=utf-8");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(bodyType, requestBody.getBodyStr());
        Log.i(TAG + " connect url->" + urlStr);
        Request request = new Request.Builder()
                //.addHeader(HeadersContract.HEADER_AUTHONRIZATION, O_AUTH_AUTHENTICATION)
                .url(urlStr)
                .post(body)
                .build();
        Call httpCall = client.newCall(request);

        Response response = httpCall.execute();
        HTTP_STATUS_CODE = response.code();
        String bodyStr = response.body().string().trim();
        Log.largeW(TAG, " post response->" + bodyStr + " status code->" + HTTP_STATUS_CODE);

        return bodyStr;
    }


    private OkHttpClient checkSslConnect(String url) {
        String httpStr = url.substring(0, 5);
//        Log.w("Connect http String is->" + httpStr);
        if (httpStr.equals("https")) {
            if (sslClient==null)
                sslClient=createSSLClient();
            return sslClient;//要用的時候再new 一個新的 不然可能會被回收
        } else {
            if (httpClient==null)
                httpClient=createDefaultClient();
            return httpClient;
        }
    }

    public OkHttpClient createDefaultClient() {
        return getHttpClientBuilder().build();
    }

    private OkHttpClient.Builder getHttpClientBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(connectSet.connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(connectSet.writeTimeout, TimeUnit.SECONDS)
                .readTimeout(connectSet.readTimeout, TimeUnit.SECONDS);

        return clientBuilder;
    }

    //--------------------------Https----------------------
    //private static int keyStoreRes =setKeyStoreRes();
//    private static final String sslPwd = "!qaz2WSX#edc";


//    private static final String keyStoreInstance = "PKCS12",
//            sslInstance = "TLS";

    protected abstract IHttpsSet httpsSet();

//    protected abstract String sslPwd();
//    protected abstract @RawRes int setKeyStoreRes();

//    private OkHttpClient createSSLClient() {
//        X509TrustManager x509trustManager=null;
//        SSLSocketFactory sslSocketFactory = null;
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//
//            InputStream sslFileStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.car1_key));
//
//            KeyStore keyStore = KeyStore.getInstance("PKCS12");
//
//            keyStore.load(sslFileStream, sslPwd.toCharArray());
//
//
//            sslFileStream.close();
//
//
//            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(algorithm);
//            trustManager.init(keyStore);
//
//            x509trustManager=(X509TrustManager) trustManager.getTrustManagers()[0];
//
//            sslContext.init(null, trustManager.getTrustManagers(), null);
//
//
//            sslSocketFactory = sslContext.getSocketFactory();
//        } catch (Exception ignored) {
//            ignored.printStackTrace();
//        }
//
////        return ssfFactory;
//        return new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .sslSocketFactory(sslSocketFactory, x509trustManager)
//                .build();
//    }

    public OkHttpClient createSSLClient() {

        X509TrustManager trustManager = null;
        SSLSocketFactory sslSocketFactory = null;

        try {
            IHttpsSet httpsSet=httpsSet();
            if (httpsSet==null)
                throw new NullPointerException("Https set con`t be null!");

            // Put the certificates a key store.
//            InputStream keyInput = trustedCertificatesInputStream(httpsSet.keyStoreRes());

//            char[] password = sslPwd().toCharArray(); // Any password will work.
//            KeyStore keyStore = newEmptyKeyStore(password);
//            keyStore.load(keyInput, sslPwd().toCharArray());

            KeyStore keyStore=httpsSet.keyStore();

            KeyManager[] kms=null;

            if (!StringUtil.isEmptyString(httpsSet.sslPwd())){
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                        KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, httpsSet.sslPwd().toCharArray());
                kms=keyManagerFactory.getKeyManagers();
            }
            // Use it to build an X509 trust manager.


            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }

            trustManager = (X509TrustManager) trustManagers[0];

            SSLContext sslContext = SSLContext.getInstance(httpsSet.httpSsl().getInstance());
            sslContext.init(kms, trustManagers, null);


//            SSLContext sslContext = trustManagerForCertificates(trustedCertificatesInputStream()); //SSLContext.getInstance("TLS");

            sslSocketFactory = sslContext.getSocketFactory();


        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return getHttpClientBuilder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(new CustomHostNameVerifier())
                .build();
    }


    /**
     * connect url->https://192.168.2.29:8443/api/v1/Search
     * Car1HttpManagerHostName->192.168.2.29
     */
    private class CustomHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO:Edit by Hill 2018/7/24 這邊以後要確認 DomainName 使否依樣
//            Log.d(TAG + "HostName->" + hostname);
            return httpsSet().hostName().equals(hostname);
        }
    }

//    private InputStream trustedCertificatesInputStream(@RawRes int res) {
//        return context.getResources().openRawResource(res);
//    }

//    private SSLContext trustManagerForCertificates(InputStream in)
//            throws GeneralSecurityException, IOException {
//
//        // Put the certificates a key store.
//        char[] password = sslPwd().toCharArray(); // Any password will work.
//        KeyStore keyStore = newEmptyKeyStore(password);
//        keyStore.load(in, sslPwd().toCharArray());
//
//        // Use it to build an X509 trust manager.
//        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
//                KeyManagerFactory.getDefaultAlgorithm());
//        keyManagerFactory.init(keyStore, password);
//        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
//                TrustManagerFactory.getDefaultAlgorithm());
//        trustManagerFactory.init(keyStore);
//        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//            throw new IllegalStateException("Unexpected default trust managers:"
//                    + Arrays.toString(trustManagers));
//        }
//
//        SSLContext ssContext = SSLContext.getInstance(sslInstance);
//        ssContext.init(keyManagerFactory.getKeyManagers(), trustManagers, null);
//        return ssContext;
//    }

//    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
//        try {
//
//            KeyStore keyStore = KeyStore.getInstance(keyStoreInstance);
//            InputStream in = null; // By convention, 'null' creates an empty key store.
//            keyStore.load(in, password);
//            return keyStore;
//        } catch (IOException e) {
//            throw new AssertionError(e);
//        }
//    }

}
