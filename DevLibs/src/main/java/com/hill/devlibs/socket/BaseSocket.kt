package com.hill.devlibs.socket

import android.content.Context
import com.hill.devlibs.impl.IHttpsSet
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.StringUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.internal.ws.RealWebSocket
import java.security.GeneralSecurityException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


/**
 * Created by Hill on 2020/11/09
 */
abstract class BaseSocket(private var from: Context): WebSocketListener(), CoroutineScope by MainScope() {


    protected abstract fun onSocketOpen(response: Response)
    protected abstract fun onSocketMsg(text: String)
    protected abstract fun onSocketClosing(code: Int)
    protected abstract fun onSocketClosed(code: Int)
    protected abstract fun httpsSet(): IHttpsSet

    private lateinit var socket:RealWebSocket

    private enum class SocketClient(val value: String){
        WS("ws"),
        WSS("wss")
    }

    fun connect(config: ISocketConfig){
        launch(Dispatchers.IO){


            var url="${config.server}:${config.port}${
                when {
                    config.path.isNullOrEmpty() -> ""
                    else -> "/${config.path}"
                }
            }"
//            var url="${config.server}:${config.port}/${config.path}"
            Log.d("Socket url->$url")
            var request = Request.Builder()
                    .url(url)
                    .build()

            var client=creatSocketClient(url)
                    .readTimeout(0, TimeUnit.MILLISECONDS)//A value of 0 means no timeout
                    .connectTimeout(0, TimeUnit.MILLISECONDS)
                    .pingInterval(10, TimeUnit.SECONDS)
                    .build()

            socket=client.newWebSocket(request,this@BaseSocket) as RealWebSocket

//            socket= RealWebSocket(request, this@BaseSocket, Random(), 10000)
//            socket.connect(client)


        }
    }


    fun close(code:Int=1000,back:(()->Unit)?=null){
        launch(Dispatchers.IO){
            socket.close(code, null)
            launch(Dispatchers.Main){
                back?.invoke()
            }
        }
    }

    private fun creatSocketClient(url: String):OkHttpClient.Builder{
        var scheme=url.substring(0, 3).toLowerCase()
//        Log.d("Socket scheme->$scheme")

        return when(scheme){
            SocketClient.WSS.value -> createSSLClient()
            else->OkHttpClient.Builder()
        }
    }

    open fun createSSLClient(): OkHttpClient.Builder {
        var trustManager: X509TrustManager?=null
        var sslSocketFactory: SSLSocketFactory?=null
        try {
            val httpsSet = httpsSet()

            val keyStore = httpsSet.keyStore()
            var kms: Array<KeyManager?>? = null
            if (!StringUtil.isEmptyString(httpsSet.sslPwd())) {
                val keyManagerFactory = KeyManagerFactory.getInstance(
                        KeyManagerFactory.getDefaultAlgorithm())

                keyManagerFactory.init(keyStore, httpsSet.sslPwd().toCharArray())
                kms = keyManagerFactory.keyManagers
            }
            // Use it to build an X509 trust manager.
            val trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            val trustManagers = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                ("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers))
            }
            trustManager = trustManagers[0] as X509TrustManager
            val sslContext = SSLContext.getInstance(httpsSet.httpSsl().instance)
            sslContext.init(kms, trustManagers, null)


//            SSLContext sslContext = trustManagerForCertificates(trustedCertificatesInputStream()); //SSLContext.getInstance("TLS");
            sslSocketFactory = sslContext.socketFactory
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
        return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(CustomHostNameVerifier())
    }

    private inner class CustomHostNameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            // TODO:Edit by Hill 2018/7/24 這邊以後要確認 DomainName 使否依樣
            Log.d("Socket HostName->$hostname")
            return httpsSet().hostName() == hostname
        }
    }

    fun sendMsg(msg:String):Boolean{
        return socket.send(msg)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        onSocketOpen(response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onSocketMsg(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        onSocketClosing(code)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        onSocketClosed(code)
    }

}