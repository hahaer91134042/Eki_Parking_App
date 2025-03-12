package com.eki.parking.Controller.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.content.Intent
import com.eki.parking.AppConfig
import com.eki.parking.AppFlag
import com.eki.parking.Controller.impl.ISocketMsg
import com.eki.parking.Controller.manager.JobManager
import com.eki.parking.Controller.notify.EkiNotification
import com.eki.parking.Controller.socket.SocketMsg
import com.eki.parking.Model.impl.INotifyMsg
import com.eki.parking.Model.serverMsg.SocketEvent
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.extension.sqlData
import com.eki.parking.extension.sqlHasData
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IHttpsSet
import com.hill.devlibs.receiver.BaseReceiver
import com.hill.devlibs.socket.BaseSocket
import com.hill.devlibs.socket.ISocketConfig
import com.hill.devlibs.tools.Log
import okhttp3.Response

/**
 * Created by Hill on 2020/11/09
 */

class EkiSocketJob:BaseJobService(), JobManager.JobConfig{

    private var socket:PPYPSocket?=null
    private lateinit var receiver:SocketReceiver
    private var isStop=false

    private var isRefreshSocket=false

    companion object{
        @JvmStatic
        var instance:EkiSocketJob?=null
    }

    init {
        instance=this
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("$tag start")

        receiver=SocketReceiver()
        receiver.register()

        connectSocket()

        return true
    }

    fun sendMsg(msg:String):Boolean=socket?.sendMsg(msg)?:false

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i("$tag stop")
        isStop=true
        closeSocket()
        receiver.unRegister()

        return false
    }

    private fun refreshSocket(){
        isRefreshSocket=true
        socket?.close{
            socket=null
            connectSocket()
            isRefreshSocket=false
        }
    }

    private fun closeSocket(){
        socket?.close()
    }

    private fun connectSocket(){
        socket=PPYPSocket()
        socket?.connect()
    }


    private inner class SocketReceiver:BaseReceiver(this,AppFlag.OnLogout,AppFlag.OnLogin){
        override fun onCatchReceiver(action: String?, intent: Intent?) {
//            Log.d("$tag get socket broadcast action->$action")
            //關掉舊的
            refreshSocket()
        }

    }

    private inner class PPYPSocket:BaseSocket(this){

        fun connect(){
            val config=object :ISocketConfig{
                override val server: String
                    get() = AppConfig.Socket.config.server
                override val port: Int
                    get() = AppConfig.Socket.config.port
                override val path: String
                    get() = sqlData<EkiMember>()?.token?:""
//                    get() = ""
            }
//            Log.w("$tag path->${config.path}")
            connect(config)
        }


        override fun onSocketOpen(response: Response) {
//            Log.d("$tag Socket open msg->${response.message()}")
        }

        override fun onSocketMsg(text: String) {
            Log.w("$tag Socket onMsg->$text")

            SocketMsg.parse(text).notNull {content->

//                Log.i("$tag msg Content->$content")
//                content.printValue()

                when(content.socketContentType){
                    SocketMsg.ContentType.SocketEvent->{
                        val event=content as SocketEvent.Content
                        when(event.Event){
                            SocketEvent.close->{
                                if (!isStop){
                                    refreshSocket()
                                }else{

                                }
                            }
                            else->{

                            }
                        }
                    }
                    else->{
                        content.socketContentType.notifyMsg.notNull { noti->
                            when(noti.needLogin){
                                true->{
                                    if (sqlHasData<EkiMember>())
                                        sendNoti(noti,content)
                                }
                                else->{
                                    sendNoti(noti,content)
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun sendNoti(noti: INotifyMsg, content: ISocketMsg.Content) {
            EkiNotification.from(this@EkiSocketJob)
                    .setContent(object :EkiNotification.Content{
                        override val title: String
                            get() = noti.title
                        override val msg: String
                            get() = noti.mapMsg(content as INotifyMsg.Content)
                        override val showClass: Class<*>?
                            get() = null
                        override val time: String?
                            get() = null
                    }).build()
                    .send()
        }

        override fun onSocketClosing(code: Int) {
            Log.i("$tag Socket closing->$code")
        }

        override fun onSocketClosed(code: Int) {
            Log.i("$tag Socket closed->$code  isRefresh->$isRefreshSocket")
            //
//            if (!isStop&&!isRefreshSocket){
//                refreshSocket()
//            }
        }

        override fun httpsSet(): IHttpsSet =AppConfig.HttpSet.ppyp
    }


    override val jobId: Int
        get() = 1001
    override val jobClazz: Class<*>
        get() = EkiSocketJob::class.java
//    override val destroyByApp: Boolean
//        get() = true
    override val runInBackground: Boolean
        get() = true

    override fun setUpJobInfo(info: JobInfo.Builder): JobInfo.Builder {
        info.setPersisted(true) // 重開機後是否執行
                .setMinimumLatency(5000) // 延遲多久執行
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                .setImportantWhileForeground(true)
        //.setOverrideDeadline(20000)  //最晚延遲時間，若其他條件未滿足，一樣會執行
        //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) //網路條件
        // .setRequiresCharging(true) //在充電情況下才執行

        return info
    }
}