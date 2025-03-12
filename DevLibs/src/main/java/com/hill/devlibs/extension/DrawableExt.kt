package com.hill.devlibs.extension

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * Created by Hill on 2020/01/02
 */

/**
 * initial target density by resource
 */
fun Drawable.resize(width:Int, height:Int,res:Resources?=null): Drawable {
    val bitmap=toBitmap()
    return BitmapDrawable(res,Bitmap.createScaledBitmap(bitmap,width,height,true))
}

fun Drawable.toBitmap():Bitmap{
    val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
    this.setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
    this.draw(Canvas().apply { setBitmap(bitmap) })
    return bitmap
}