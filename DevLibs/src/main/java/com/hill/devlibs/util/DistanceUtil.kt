package com.hill.devlibs.util

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.hill.devlibs.EnumClass.DistanceUnit
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2019/12/09
 */
object DistanceUtil {
    const val earthRadius = 6378.137 //appxoximate radius in miles
    @JvmOverloads
    fun calDistance(from: LatLng,to:LatLng,unit:DistanceUnit=DistanceUnit.M):Double{
        var result= arrayOf(0.0f).toFloatArray()
        Location.distanceBetween(from.latitude,from.longitude,to.latitude,to.longitude,result)

//        Log.w("calDistance from->$from to->$to result->${result[0]}")
        return result[0].toDouble()*unit.ratio
//        var factor = Math.PI / 180
//        var dLat = (to.latitude - from.latitude) * factor
//        var dLon = (to.longitude - from.longitude) * factor
//
//        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * factor) * Math.cos(lat2 * factor) * Math.Sin(dLon / 2) * Math.Sin(dLon / 2);
//        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    }
}