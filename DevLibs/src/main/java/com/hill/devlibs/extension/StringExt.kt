package com.hill.devlibs.extension

import android.util.Base64
import com.hill.devlibs.util.TimeUtil
import com.hill.devlibs.encryption.MD5
import com.hill.devlibs.encryption.SHA1
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.TimeUnit
import com.hill.devlibs.time.ext.*
import com.hill.devlibs.util.RandomTemplete
import com.hill.devlibs.util.RandomUtil
import com.hill.devlibs.util.StringUtil
import java.util.*


/**
 * Created by Hill on 2019/12/12
 */
inline fun randomStr(length: Int,input:RandomTemplete=RandomTemplete.all):String=RandomUtil.randomString(length, input)
fun RandomTemplete.randomStr(length:Int):String=RandomUtil.randomString(length,this)
fun String?.isNotEmpty(back:(String)->Unit):Check<String?>{
    return if (!this.isNullOrEmpty()){
        back(this)
        Check.onTrue(this)
    }else{
        Check.onFalse(this)
    }
}
fun  String?.isNullOrEmpty(back:()->Unit):Check<String?>{
    return if (this.isNullOrEmpty()){
        back()
        Check.onTrue(this)
    }else{
        Check.onFalse(this)
    }
}
//fun String.textCheck(back: (String) -> Unit):Check<String>{
//
//
//
//    return if (this.isNullOrEmpty()){
//        back(this)
//        Check.onTrue(this)
//    }else{
//        Check.onFalse(this)
//    }
//}

fun String.isEmpty():Boolean{
    return StringUtil.isEmptyString(this)
}

fun String.toSHA1(): String {
    return SHA1.encodeToString(this)
}
fun String.decodeBase64():ByteArray = Base64.decode(this,Base64.NO_WRAP)

fun String.toMD5():String{
    return MD5.encode(this)
}
fun String.toBase64():String{
    return Base64.encodeToString(this.toByteArray(Charsets.ISO_8859_1), Base64.DEFAULT)
}
fun String.messageFormat(vararg obj: Any):String=StringUtil.getFormateMessage(this,*obj)

fun String.toASCII():ByteArray{
    return this.toByteArray(Charsets.US_ASCII)
}
fun String.toCalendar(format:String?=null): Calendar {
    format.notNull { return TimeUtil.stringToCalendar(this,it) }
    return TimeUtil.stringToCalendar(this)
}
fun String.toStamp(format: String?=null):Long{
    format.notNull { return TimeUtil.stringToStamp(this,it) }
    return TimeUtil.stringToStamp(this)
}
fun String.toDate(format:String= TimeUtil.dateFormat2): DateUnit {
    var c= Calendar.getInstance().also { it.time= TimeUtil.parseTime(this,format) }
    return DateUnit(c.getYear(),c.getMonth(),c.getDay())
}
fun String.toTime(format: String= TimeUtil.timeFormat):TimeUnit{
    if (this.isNullOrEmpty())
        return TimeUnit(0,0,0)
    var c= Calendar.getInstance().also { it.time= TimeUtil.parseTime(this,format) }
    return TimeUnit(c.getHour24(),c.getMin(),c.getSec())
}
fun String.toDateTime(format: String?=null): DateTime = DateTime(this.toCalendar(format))

fun String.toEmoji():String{
    var code=this
    // offset between uppercase ascii and regional indicator symbols
    val OFFSET = 127397
    // validate code
    if (code == null || code.length !== 2) {
        return ""
    }
    //fix for uk -> gb
    if (code.equals("uk",ignoreCase = true)) {
        code = "gb"
    }
    // convert code to uppercase
    code = code.toUpperCase()
    val emojiStr = StringBuilder()
    //loop all characters
    for (i in code.indices) {
        emojiStr.appendCodePoint(code[i].toInt() + OFFSET)
    }
    // return emoji
    return emojiStr.toString()
}