package com.eki.parking.Controller.notify

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.eki.parking.AppConfig
import com.eki.parking.R
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/10/20
 */

class EkiNotification private constructor(private val context:Context) {
    var content: Content? = null

    companion object {
        fun from(from: Context): Builder = Builder(from)
    }

    class Builder internal constructor(from:Context) {
        private val noti=EkiNotification(from)
        fun setContent(c: Content): Builder {
            noti.content=c
            return this
        }
        fun build(): EkiNotification = noti
    }

    interface Content {
        val title: String
        val msg: String
        val showClass:Class<*>?
        val time:String?
    }

    private val now = DateTime.now()
    private var intent = Intent().setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)


    init {

    }

    fun send() {

        val builder = NotificationCompat.Builder(context, AppConfig.notifyChannelId)
            .setSmallIcon(R.drawable.papaya_logo)
//                .setLargeIcon(getLargeIcon())
//                .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)

        content.notNull {

            builder.setContentTitle(it.title)
                    .setContentText(it.msg)
                    .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(it.msg)
                    .setBigContentTitle(it.title))
           //println("取得notify " + it.title + " " + it.time + " " + it.id + " " + it.serialNumber)
//           if( it.title == EkiReservaAlarm.MsgType.ComeToEnd.title || it.title == EkiReservaAlarm.MsgType.ReservaEnd.title){
//               val bundle = Bundle()
//
//               bundle.putString("Time", it.time)
//               bundle.putString("SerialNumber", it.serialNumber)
//               intent.putExtras(bundle)
//
//           }

            it.showClass.notNull { clazz->
                intent.setClass(context,clazz)
            }

        }
        //                .setLargeIcon(getLargeIcon())
//                .setDefaults(Notification.DEFAULT_ALL)
        builder.setContentIntent(creatPendingIntent(intent))

        //                .setFullScreenIntent(mPendingintent,true)
        //.setStyle(bigTextStyle)

        val notification = builder.build() // 建立通知
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManager.notify(now.toStamp().toInt(), notification)

    }

    private fun creatPendingIntent(intent: Intent): PendingIntent {
        var flag=when{
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.S->PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            else->PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(
            context, now.toStamp().toInt(),
            intent,
            flag)
    }

}