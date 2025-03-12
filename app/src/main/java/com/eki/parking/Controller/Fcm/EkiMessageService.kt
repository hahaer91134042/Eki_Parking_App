package com.eki.parking.Controller.Fcm

import com.eki.parking.App
import com.eki.parking.Controller.notify.EkiNotification
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.extension.sqlHasData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2018/10/9.
 */

class EkiMessageService : FirebaseMessagingService(){
    var TAG= javaClass.simpleName
//    override fun zzd(p0: Intent?) {
//        PrintLogKt.d("$TAG onZzd->${p0} ")
//    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        FirebaseMessaging.getInstance().token
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        //Log.w( "getInstanceId failed")
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result
                    //Log.w("Fcm Token on New Token->$token")
                    App.getInstance().preferenceManager.settingPreference?.fcmToken= token?:""
                })
    }
    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)

//        Log.d("$TAG onMessageReceived->$msg ")
        Log.w("$TAG form->${msg?.from}\n data->${msg?.data}\n notify->${msg?.notification}\n notify body->${msg?.notification?.body}")

//        msg?.data?.get("Method")

        msg?.data?.notNull {msg->
            FcmMsg.parse(msg).notNull {noti->
                when(noti.needLogin){
                    true->{
                        if (sqlHasData<EkiMember>())
                            sendNoti(noti)
                    }
                    else->sendNoti(noti)
                }
            }

//            val intent = Intent()
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//
//            val mPendingintent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//            val notification = NotificationCompat.Builder(this,AppConfig.notifyChannelId)
//                    .setSmallIcon(R.drawable.papaya_logo)
////                .setLargeIcon(getLargeIcon())
//                    //.setDefaults(Notification.DEFAULT_ALL)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setContentTitle(notiString?.title)
//                    .setContentText(notiString?.msg)
//                    .setStyle(
//                            NotificationCompat.BigTextStyle()
//                                    .bigText(notiString?.msg)
//                                    .setBigContentTitle(notiString?.title)
////                                    .setSummaryText("Wonderful summary here")
//                    )
////                .setFullScreenIntent(mPendingintent,true)
//                    //.setStyle(bigTextStyle)
//                    .setAutoCancel(true)
//                    .setContentIntent(mPendingintent)
//                    .build() // 建立通知
//
//            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)
//            notificationManager.notify(DateTime().toStamp().toInt(), notification)


        }


//        val notificationManager = getSystemService(
//                Context.NOTIFICATION_SERVICE) as NotificationManager // 取得系統的通知服務
//        notificationManager.notify(1, notification) // 發送通知

    }

    private fun sendNoti(noti: FcmMsg.Result) {
        EkiNotification.from(this)
                .setContent(object : EkiNotification.Content{
                    override val title: String
                        get() = noti.title
                    override val msg: String
                        get() = noti.msg
                    override val showClass: Class<*>?
                        get() = null
                    override val time: String?
                        get() = null
                })
                .build()
                .send()
    }

    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
    }
}