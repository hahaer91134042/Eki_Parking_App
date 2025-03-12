package com.eki.parking.Controller.map

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import com.eki.parking.Model.sql.EkiLocation
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.extension.toBitmap

/**
 * Created by Hill on 25,09,2019
 */
class MapMarker(var position:LatLng) {

    constructor(lat:Double,lng:Double) : this(LatLng(lat,lng))

    var title=""
    var snippet=""
    var icon: BitmapDescriptor?=null
    var location:EkiLocation?=null

    fun setIcon(@DrawableRes res:Int):MapMarker{
        icon = BitmapDescriptorFactory.fromResource(res)
        return this
    }
    fun setIcon(drawable: Drawable):MapMarker{
        return setIcon(drawable.toBitmap())
    }
    fun setIcon(map:Bitmap):MapMarker{
        icon=BitmapDescriptorFactory.fromBitmap(map)
        return this
    }
    fun setIcon(view:View):MapMarker{
        return setIcon(view.toBitMap())
    }
//    fun setIcon(view:ViewGroup):MapMarker{
//        return setIcon(view.toBitMap())
//    }

//    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
//        val canvas = Canvas()
//        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
//        canvas.setBitmap(bitmap)
//        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
//        drawable.draw(canvas)
//        return BitmapDescriptorFactory.fromBitmap(bitmap)
//    }
}