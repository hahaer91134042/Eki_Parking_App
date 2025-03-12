package com.hill.devlibs.extension

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.hill.devlibs.impl.IConvertToSql
import com.hill.devlibs.list.ConvertSqlList
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.AppGson
import com.hill.devlibs.tools.ContentPrinter
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.StringUtil
import org.json.JSONException

/**
 * Created by Hill on 25,09,2019
 */
//inline fun <reified T> List<T>.toArray():Array<T> {
//    var array= arrayOfNulls<T>(size)
//    return this.toTypedArray()
//}

inline val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
inline val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
inline val Int.px:Float
    get() = (this/Resources.getSystem().displayMetrics.density)

inline val String.cleanTex:String
    get() =StringUtil.cleanChar(this)

inline fun View.getColorList(@ColorRes res:Int): ColorStateList?{
    return this.context?.resources?.getColorStateList(res)
}
inline fun Fragment.getColorList(@ColorRes res:Int): ColorStateList?{
    return this.context?.resources?.getColorStateList(res)
}
inline fun Fragment.getColor(@ColorRes res:Int): Int {
    return  this.context?.resources?.getColor(res)?: Color.BLACK
}

inline fun Any.printException(e:Exception){
    Log.e(e.toString())
}
inline fun Any.printException(msg:String){
    Log.e(msg)
}
fun <T> String.toObj(clazz: Class<T>):T{
    try {
        var obj= JsonParser().parse(this).asJsonObject
//                var type=object : TypeToken<VO>(){}.type
        return AppGson.creat().fromJson<T>(obj,clazz)//Gson的bug 注意一定要用class
    }catch (e:Exception){
        Log.e(e.toString())
    }
    throw JSONException("parse ${clazz.simpleName} fail !!")
}
inline fun <reified DATA> String.toObj():DATA{
    try {
        var obj= JsonParser().parse(this).asJsonObject
//                var type=object : TypeToken<VO>(){}.type
        return AppGson.creat().fromJson<DATA>(obj,DATA::class.java)//Gson的bug 注意一定要用class
    }catch (e:Exception){
        Log.e(e.toString())
    }
    throw JSONException("parse ${DATA::class.java.simpleName} fail !!")
}

inline fun <reified T : Enum<*>> enumValueOrNull(name: String): T? =
        T::class.java.enumConstants?.firstOrNull { it.name == name }

inline fun View.getDrawable(@DrawableRes res:Int):Drawable?{
    return this.context?.getDrawable(res)
}
inline fun Fragment.getDrawable(@DrawableRes res:Int):Drawable?{
    return this.context?.getDrawable(res)
}
inline fun View.getColor(@ColorRes res:Int):Int{
    return this.context?.resources?.getColor(res)!!
}

inline fun  printContent(obj:Any){
    var clazz=obj.javaClass
    var fields=clazz.declaredFields
    var content= ContentPrinter()

    fields.forEach {
        try {
            content.addKey(it.name)
                    .addValue(it.getValue(obj))
        }catch (e:Exception){
            Log.e("Field name->${it.name} cant print")
            content.addValue("null")
        }
    }
    val keys = content.keys
    val values = content.values

    if (keys.size == values.size) {
        Log.w("----${clazz.simpleName}----")
        for (i in keys.indices) {
            if (values[i]!=null)
                Log.i(keys[i], values[i].toString())
            else
                Log.i(keys[i], null.toString())
        }
        Log.w("----Print Value End----")
    } else {
        Log.e("Key Value none paire!")
    }
}
inline fun <T:Any> List<T>.printList(){
    forEach { printContent(it) }
}

fun <T:SqlVO<*>> List<IConvertToSql<T>>.toSqlList():ArrayList<T>{
    return ConvertSqlList<IConvertToSql<T>,T>()
            .also { it.addAll(this) }.toSqlList()
}

inline fun Any.printValue(){
    printContent(this)
}
