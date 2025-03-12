package com.eki.parking.Controller.broadcastReceiver

import android.content.Context
import android.content.Intent
import com.eki.parking.Controller.impl.ISysAlarmSet
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.extension.string
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.toByteArray
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.formatNoSec
import java.io.Serializable


/**
 * Created by Hill on 2020/09/21
 */

abstract class EkiReservaAlarm(private var context: Context, private var time: DateTime) : ISysAlarmSet {

    class Flag {
        companion object {
            //            const val title="AlarmTitle"
//            const val message="AlarmMsg"
            const val action = "EkiReservaAlarm"
            const val data = "ReservaData"
        }
    }

    enum class MsgType(val title: String, val templete: String) {
        ReservaStart(
            string(R.string.Appointment_is_about_to_start),
            string(R.string.Appointment_detail)
        ),
        ComeToEnd("停車時間即將結束", "15分鐘後，停車於 {0} 期滿，請務必於期滿10分鐘內退租離場已避免高額罰款，如已取消訂單請不予理會此訂單"),
        ReservaEnd("預約期滿", "預約於{0} {1} 停車時間終止，請於10分鐘內退租離場，逾期將會被收取高額罰款，詳情請參考車主規章，如已取消訂單請不予理會此訂單")
    }

    init {

    }

    abstract fun msgType(): MsgType
    abstract fun order(): EkiOrder

    override fun alarmTime(): DateTime = time

    open val showClass: Class<*>? = null

    override fun intent(): Intent {
        val type = msgType()
        val order = order()
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = Flag.action

        //這邊由於要發送給alarm manager 再由alarm manager 發送回receiver 所以不能直接放入serialable
        //會讀取不到資料 所以先轉成bytearray 再轉回來
        intent.putExtra(
            Flag.data,
            Data(
                type.title,
                when (type) {
                    MsgType.ReservaStart -> type.templete.messageFormat(
                        order.ReservaTime.startDateTime().formatNoSec(), order.Address.shortName
                    )
                    MsgType.ComeToEnd -> type.templete.messageFormat(order.Address.shortName)
                    MsgType.ReservaEnd -> type.templete.messageFormat(
                        "${
                            order.ReservaTime.startDateTime().formatNoSec()
                        } - ${order.ReservaTime.endDateTime().formatNoSec()}",
                        order.Address.shortName
                    )
                },
                showClass,
                order.ReservaTime.EndTime
            ).toByteArray()
        )

        return intent
    }

    class Data(
        var title: String,
        var msg: String,
        var showClazz: Class<*>? = null,
        var time: String = "2022-01-01 00:00:00"
    ) : Serializable {

    }
}