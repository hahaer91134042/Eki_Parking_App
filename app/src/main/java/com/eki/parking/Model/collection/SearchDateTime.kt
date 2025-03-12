package com.eki.parking.Model.collection

import com.haibin.calendarview.Calendar
import com.hill.devlibs.collection.CompareList
import com.hill.devlibs.time.TimeUnit

/**
 * Created by Hill on 03,10,2019
 */
class SearchDateTime: CompareList<Calendar>(){

    var startTime: TimeUnit = TimeUnit(0,0,0)
    var endTime:TimeUnit= TimeUnit(0,0,0)

    fun setStartTime(hour:Int,min:Int){
        startTime= TimeUnit(hour, min,0)
    }
    fun setEndTime(hour: Int,min: Int){
        endTime= TimeUnit(hour, min,0)
    }

    //假如發現有一樣的日期 就刪除?
    fun addOrRemoveDate(calendar: Calendar){
        if (contain { return@contain it == calendar }) {
            val r = find { return@find it == calendar }
            remove(r)
        } else{
            add(calendar)
        }
    }

    fun checkTime():Check{

        return when{
            //有設定日期
            size>0->{
                //檢查是否有設置時間
                if (isValidEnd()){
                    Check.OK
                }else{
                    Check.TimeError
                }
            }
            //沒有設置日期
            else->{
                if (startTime.toStamp()==0L&&endTime.toStamp()==0L){
                    Check.OK
                }else{
                    Check.DateError
                }
            }
        }
    }

    fun isValidEnd():Boolean{
        val start=startTime.toStamp()
        val end=endTime.toStamp()

        return end>start
    }

    enum class Check{
        OK,
        DateError,
        TimeError
    }
}