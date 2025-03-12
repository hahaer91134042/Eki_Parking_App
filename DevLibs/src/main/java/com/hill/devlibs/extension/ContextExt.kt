package com.hill.devlibs.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.hill.devlibs.R
import com.hill.devlibs.impl.IActivityIntent

/**
 * Created by Hill on 2020/01/02
 */
fun Context.getBitmap(@DrawableRes res:Int):Bitmap = BitmapFactory.decodeResource(resources,res)

fun Context?.showToast(str:String){
    this.notNull { Toast.makeText(it,str, Toast.LENGTH_LONG).show() }
}
fun Context?.showShortToast(str:String): Toast?{
    var toast: Toast? = null
    this.notNull {
    toast = Toast.makeText(it,str, Toast.LENGTH_SHORT)
    toast?.show()
    }
    return toast
}

inline fun Context.startActivityAnim(intent: Intent,isAnim:Boolean=true){
    this.startActivity(intent)
    //有強轉 要注意
    if (isAnim)
        if (this is Activity){
            this.overridePendingTransition(R.anim.from_black_to_white, R.anim.from_white_to_black)
        }
}
inline fun Context.startActivityAnim(impl: IActivityIntent,isAnim:Boolean=true){
    this.startActivityAnim(impl.intent,isAnim)
}