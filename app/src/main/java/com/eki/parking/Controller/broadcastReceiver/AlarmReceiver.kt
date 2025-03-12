package com.eki.parking.Controller.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.eki.parking.AppConfig
import com.eki.parking.Controller.activity.OnlyCheckoutActivity
import com.eki.parking.Controller.notify.EkiNotification
import com.hill.devlibs.extension.toCalendar
import com.hill.devlibs.extension.toObj
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.tools.Log
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val now = DateTime.now()
        Log.w("Alarm Get Receiver time->$now")

        if (intent.action == EkiReservaAlarm.Flag.action) {

            intent.getByteArrayExtra(EkiReservaAlarm.Flag.data)
                ?.toObj<EkiReservaAlarm.Data>()?.let { data ->
                    EkiNotification.from(context)
                        .setContent(object : EkiNotification.Content {
                            override val title: String
                                get() = data.title
                            override val msg: String
                                get() = data.msg
                            override val showClass: Class<*>?
                                get() = data.showClazz
                            override val time: String?
                                get() = data.time
                        })
                        .build()
                        .send()

                    val cal: Calendar = Calendar.getInstance()
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    cal.time = sdf.parse(data.time)

                    if (now >= DateTime(data.time.toCalendar(AppConfig.ServerSet.dateTimeFormat))) {
                        val mIntent = Intent(context, OnlyCheckoutActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(mIntent)
                    }
                }
        }
    }
}
