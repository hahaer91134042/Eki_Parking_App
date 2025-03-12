package com.hill.devlibs.util

import com.hill.devlibs.tools.Log
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Hill on 2018/9/26.
 */
object TimeUtil {
    @JvmStatic val dateTimeFormat="yyyy-MM-dd HH:mm:ss"
    @JvmStatic val dateFormat="yyyy/MM/dd"
    @JvmStatic val dateFormat2="yyyy-MM-dd"
    @JvmStatic val timeFormat="HH:mm:ss"
    @JvmStatic val shortTimeFormat="HH:mm"
    @JvmStatic val timeMsgTemplete1="{0}-{1}-{2}"

    @JvmStatic fun getNow():Long=Calendar.getInstance().timeInMillis
    @JvmStatic fun getNowYear():Int=Calendar.getInstance().get(Calendar.YEAR)
    @JvmStatic fun getNowMonth():Int=Calendar.getInstance().get(Calendar.MONTH)+1
    @JvmStatic fun getNowWeek():Int=Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)
    @JvmStatic fun getNowDay():Int=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    @JvmStatic fun getNowHour():Int=Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    @JvmStatic fun getNowMinute():Int=Calendar.getInstance().get(Calendar.MINUTE)
    @JvmStatic fun getNowTimeStr():String= stampToString(Calendar.getInstance().timeInMillis)

    /**
     * 当地时间 ---> UTC时间
     * @return
     * 原文：https://blog.csdn.net/Burn_yourself/article/details/71195241
     */
    @JvmStatic fun local2UTC():String{
        var sdf = SimpleDateFormat(dateTimeFormat)
        sdf.timeZone = TimeZone.getTimeZone("gmt")
        return sdf.format(Date())
    }

    /**
     * UTC时间 ---> 当地时间
     * @param utcTime   UTC时间
     * @return
     * 時區問題還有待研究 先不用
     */
    @JvmStatic fun utc2Local(utcTime:String):String {
//        var utcFormater = SimpleDateFormat(timeFormat);//UTC时间格式
//        utcFormater.timeZone = TimeZone.getTimeZone("UTC")
//        var gpsUTCDate:Date?
//        try {
//            gpsUTCDate = utcFormater.parse(utcTime)
//            var localFormater =SimpleDateFormat(timeFormat)//当地时间格式
//            localFormater.timeZone = TimeZone.getDefault()
//            var localTime = localFormater.format(gpsUTCDate!!.time);
//            return localTime
//        } catch (e:Exception) {
//            e.printStackTrace()
//        }
//        return ""
        return utc2Local(dateTimeFormat,utcTime)
    }

    @JvmStatic fun utc2Local(format:String,utcTime:String):String {
        var utcFormater = SimpleDateFormat(format);//UTC时间格式
        utcFormater.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate:Date?
        try {
            gpsUTCDate = utcFormater.parse(utcTime)
            var localFormater =SimpleDateFormat(format)//当地时间格式
            localFormater.timeZone = TimeZone.getDefault()
            var localTime = localFormater.format(gpsUTCDate!!.time);
            return localTime
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @JvmStatic fun dotNetTicksToTime(ticks:Long):String{

        var date= fromDotNetTicks(ticks)
        Log.i("$ticks to->${date.time}")

        return stampToString(date.time)
    }

    @JvmStatic fun fromDotNetTicks(ticks: Long): Date {
        var ticks = ticks
        // Rebase to Jan 1st 1970, the Unix epoch
        ticks -= 621355968000000000L
        val millis = ticks / 10000
        return Date(millis)
    }
    @JvmStatic
    fun stringToCalendar(time:String, format: String= dateTimeFormat):Calendar{
        var sdf=SimpleDateFormat(format)
        sdf.parse(time)
        return sdf.calendar
    }

    @JvmStatic fun stringToStamp(time:String,format:String= dateTimeFormat):Long{
        return parseTime(time,format)?.time
    }


    @JvmStatic fun stampToString(timeMillis:Long,format:String= dateTimeFormat):String{
        var simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(Date(timeMillis))
    }
    @JvmStatic
    fun parseTime(time: String, formate: String= dateTimeFormat): Date {
        val sdf = SimpleDateFormat(formate)
        return sdf.parse(time)
    }


//    fun GetTicks(epochStr: String): Long {
//        //convert the target-epoch time to a well-format string
//        val date = java.text.SimpleDateFormat("yyyy/MM/dd/HH/mm/ss").format(Date(java.lang.Long.parseLong(epochStr)))
//        val ds = date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//
//        //start of the ticks time
//        val calStart = Calendar.getInstance()
//        calStart.set(1, 1, 3, 0, 0, 0)
//
//        //the target time
//        val calEnd = Calendar.getInstance()
//        calEnd.set(Integer.parseInt(ds[0]), Integer.parseInt(ds[1]), Integer.parseInt(ds[2]), Integer.parseInt(ds[3]), Integer.parseInt(ds[4]), Integer.parseInt(ds[5]))
//
//        //epoch time of the ticks-start time
//        val epochStart = calStart.time.time
//        //epoch time of the target time
//        val epochEnd = calEnd.time.time
//
//        //get the sum of epoch time, from the target time to the ticks-start time
//        val all = epochEnd - epochStart
//        //convert epoch time to ticks time
//
//
//        return all / 1000 * 1000000 * 10
//    }


}