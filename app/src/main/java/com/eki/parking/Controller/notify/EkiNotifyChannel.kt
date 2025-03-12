package com.eki.parking.Controller.notify

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.eki.parking.AppConfig

/**
 * Created by Hill on 2019/9/17
 */
class EkiNotifyChannel(app: Application) {

    companion object{
        @JvmStatic
        fun register(app: Application): EkiNotifyChannel {
            return EkiNotifyChannel(app)
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var manager=app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var channel=NotificationChannel(AppConfig.notifyChannelId,"EkiParkingChannel",
                    NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
//            channel.description="1111"
            channel.lightColor=Color.RED
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)
        }
    }
}