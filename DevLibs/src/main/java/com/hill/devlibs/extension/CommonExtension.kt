package com.hill.devlibs.extension


import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Base64
import android.view.View
import com.hill.devlibs.util.TimeUtil
import com.hill.devlibs.annotation.parse.TableParser
import com.hill.devlibs.collection.DataRow
import com.hill.devlibs.encryption.MD5
import com.hill.devlibs.model.ValueObjContainer
import com.hill.devlibs.tools.AppGson
import com.hill.devlibs.util.StringUtil
import java.io.*
import java.lang.reflect.Field
import kotlin.collections.ArrayList


/**
 * Created by Hill on 2019/6/21
 */

inline fun <T> ByteArray.toObj():T?{
    val bis = ByteArrayInputStream(this)
    var data:T?=null
    try {
        var input = ObjectInputStream(bis)
        data= input.readObject() as T
        input.close()
        bis.close()
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return data
}

inline fun Serializable.toByteArray():ByteArray?{
    val bos = ByteArrayOutputStream()
    var data:ByteArray?=null
    try {
        var out = ObjectOutputStream(bos)
        out.writeObject(this)
        out.flush()
        data=bos.toByteArray()
        out.close()
        bos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return data
}

fun <T:Serializable> Intent.getParcel(flag:String):T{
    return getParcelableExtra<ValueObjContainer<T>>(flag)!!.valueObj
}
fun <T> List<T>.toArrayList():ArrayList<T> = ArrayList(this)
fun Any.toContentValue():ContentValues=TableParser.toContainValue(this)

fun DataRow.setObjData(obj:Any):Boolean=runCatching { TableParser.setData(obj,this@setObjData) }.isSuccess

fun <T> T.toJsonStr():String{
    return AppGson.creat().toJson(this)
}
fun Int.mod02d():String=StringUtil.format02d(this)


fun ByteArray.toMD5():String{
    return MD5.encode(this)
}
fun ByteArray.md5Digest():ByteArray?{
    return MD5.digest(this)
}

fun ByteArray.toBase64():String{
    return Base64.encodeToString(this,Base64.NO_WRAP)//這樣跟C#會一致
}



fun Field.getValue(obj:Any):Any{
    this.isAccessible=true
    return this.get(obj)
}



fun View.toBitMap():Bitmap{
    this.measure(View.MeasureSpec.makeMeasureSpec(this.layoutParams.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(this.layoutParams.height, View.MeasureSpec.EXACTLY))
    this.layout(0, 0, this.measuredWidth, this.measuredHeight)
    val b = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    this.draw(Canvas(b))
    return b
}



fun Long.toTimeStr(format:String?=null):String{
    format?.let { return TimeUtil.stampToString(this,it) }
    return TimeUtil.stampToString(this)
}
fun Boolean.isTrue(back:()->Unit){
    if (this)
        back()
}
fun Boolean.isFalse(back:()->Unit){
    if (!this)
        back()
}


//沒啥用
//fun <T> T?.notNull(f: (T) -> Unit)  {
//    this?.let{f(this)}
//}

inline fun <T:Any, R> T?.notNull(callback: (T)->R): R? {
    return this?.let(callback)
}
inline fun <T:Any> T?.whenNullBack(callback: ()->T):T{
    if (this==null)
       return callback()
    return this
}
inline fun <T:Any> T?.isNull(callback: ()->Unit):CheckNull<T>{
    return if (this==null){
        callback()
        CheckNull.onNull()
    }else{
        CheckNull.onNotNull(this)
    }
}
inline fun <reified T> Any.instanceIs(back:(T)->Unit){
    if (this is T)
        back(this)
}

//fun View.toBitMap():Bitmap{
////    return if(this.measuredHeight<=0){
////        this.measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
////        var map=Bitmap.createBitmap(this.measuredWidth,this.measuredHeight,Bitmap.Config.ARGB_8888)
////        this.layout(0,0,this.measuredWidth,this.measuredHeight)
////        this.draw(Canvas(map))
////        map
////    }else{
////    Log.i("View width->${this.layoutParams.width} height->${this.layoutParams.height}")
////    Log.d("View left->${this.left} top->${this.top} right->${this.right} bottom->${this.bottom}")
////    Log.w("View measure height->${this.measuredHeight} width->${this.measuredWidth}")
//    var map = Bitmap.createBitmap(this.layoutParams.width, this.layoutParams.height, Bitmap.Config.ARGB_8888)
//    this.layout(0, 0, this.layoutParams.width, this.layoutParams.height)
//    this.draw(Canvas(map))
//    return map
////    }
//}

