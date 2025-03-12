package com.hill.devlibs.util

import android.R.attr.src
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format


/**
 * Created by Hill on 20,11,2019
 */
object BitmapUtil {

    @JvmStatic
    fun getTimeWaterMarker(time:DateTime= DateTime()):Bitmap{
        val markerMap = Bitmap.createBitmap(500,70, Bitmap.Config.ARGB_8888)

        var canvas=Canvas(markerMap)
        canvas.drawColor(Color.BLACK)
        canvas.drawText(time.format(),10f,60f,Paint().apply {
            color=Color.WHITE
            textSize=50f
            style=Paint.Style.FILL
        })
//        canvas.save()
//        canvas.restore()
        return markerMap
    }

}