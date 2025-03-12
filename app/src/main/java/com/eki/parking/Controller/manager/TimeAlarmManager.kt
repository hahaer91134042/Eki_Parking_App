package com.eki.parking.Controller.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.eki.parking.Controller.activity.OnlyCheckoutActivity
import com.eki.parking.Controller.broadcastReceiver.AlarmReceiver
import com.eki.parking.Controller.broadcastReceiver.EkiReservaAlarm
import com.eki.parking.Controller.impl.ISysAlarmSet
import com.eki.parking.Model.sql.EkiOrder
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.time.ext.minSpan
import com.hill.devlibs.tools.Log


/**
 * Created by Hill on 2020/09/17
 */
class TimeAlarmManager(context: Context?) :BaseManager(context) {

    private val alarmManager:AlarmManager? by lazy { context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager? }

    init {
        context?.packageManager?.setComponentEnabledSetting(
                ComponentName(context, AlarmReceiver::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )
    }


    @JvmOverloads
    fun addOrder(order: EkiOrder,vararg excepType:EkiReservaAlarm.MsgType){

        if(order.isReserved()){
            var alarmList=ArrayList<EkiReservaAlarm>()

            alarmList.add(ReservaStartAlarm(order))
            alarmList.add(ComeToEndAlarm(order,OnlyCheckoutActivity::class.java))
            alarmList.add(ReservaEndAlarm(order))

            alarmList.filter { alarm->
                //去除例外清單的
                when{
                    excepType.isEmpty() ->true
                    else->!excepType.any { it==alarm.msgType() }
                }
            }.forEach { alarm->
                setAlarm(alarm)
            }
        }

    }

    @JvmOverloads
    fun removeOrder(order:EkiOrder,excepList:List<EkiReservaAlarm.MsgType>?=null){
        var alarmList=ArrayList<EkiReservaAlarm>()

        alarmList.add(ReservaStartAlarm(order))
        alarmList.add(ComeToEndAlarm(order))
        alarmList.add(ReservaEndAlarm(order))

        alarmList.filter { alarm->
            //去除例外清單的
            when(excepList){
                null->true
                else->!excepList.any { it==alarm.msgType() }
            }
        }.forEach { alarm->
            cancelAlarm(alarm)
        }
    }

    fun setAlarm(set:ISysAlarmSet){

        Log.d("Set Alarm time->${set.alarmTime()} stamp->${set.alarmTime().toStamp().toInt()}")

        alarmManager.notNull { am->
            val time=set.alarmTime()
            val intent=set.intent()
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                time.toStamp().toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            am[AlarmManager.RTC_WAKEUP, time.toStamp()] = pendingIntent
        }

    }
    fun cancelAlarm(set:ISysAlarmSet){
        Log.e("Cancel Alarm time->${set.alarmTime()} stamp->${set.alarmTime().toStamp().toInt()}")
        alarmManager.notNull { am->
            val time=set.alarmTime()
            val intent=set.intent()
            var pendingIntent=PendingIntent.getBroadcast(context,time.toStamp().toInt(),intent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            am.cancel(pendingIntent)
        }
    }

    private inner class ReservaStartAlarm(private var order:EkiOrder) :
            EkiReservaAlarm(context,
                    order.ReservaTime.startDateTime()-15.minSpan) {
        override fun msgType(): MsgType =MsgType.ReservaStart
        override fun order(): EkiOrder=order
    }
    private inner class ComeToEndAlarm(private var order:EkiOrder,private var clazz: Class<*>?=null):
            EkiReservaAlarm(context,
                    order.ReservaTime.endDateTime()-15.minSpan) {
        override fun msgType(): MsgType =MsgType.ComeToEnd
        override fun order(): EkiOrder =order
        override val showClass: Class<*>?
            get() = clazz
    }
    private inner class ReservaEndAlarm(private var order: EkiOrder) :
            EkiReservaAlarm(context,order.ReservaTime.endDateTime()) {
        override fun msgType(): MsgType=MsgType.ReservaEnd
        override fun order(): EkiOrder =order
    }
}