package com.eki.parking.extension

import android.content.Context
import android.graphics.Color
import android.location.Location
import com.eki.parking.Controller.impl.IMapSearchSet
import com.eki.parking.Controller.listener.OnMultiPageResponseBack
import com.eki.parking.Controller.map.EkiMap
import com.eki.parking.Model.request.body.LoadLocationBody
import com.eki.parking.Model.response.LoadLocationResonse
import com.eki.parking.Model.DTO.LoadLocationInfo
import com.eki.parking.Controller.process.LoadLocationProcess
import com.google.android.gms.maps.model.LatLng
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.util.DistanceUtil

/**
 * Created by Hill on 2019/12/03
 */
fun EkiMap<*>.drawCircle(loc:LatLng, radius:Double){
    addCircle(loc,object:EkiMap.ICircleSet(){
        override val circleColor: Int
            get() = Color.argb(40,239,130,0)
        override val radius: Double
            get() = radius
    })
}

fun EkiMap<*>.goMyLocation(loc: Location?,zoom:Float=0f){
    loc.notNull {
        when (zoom) {
            0f -> moveCamera(it.latitude,it.longitude,true)
            else -> moveCamera(it.latitude,it.longitude,zoom,true)
        }
    }
}

fun IMapSearchSet.toLoadLocationBody():LoadLocationBody{
    val set=this
    return LoadLocationBody().apply {
        config=set.config.toData()
        if (set.config.Address.isNotEmpty()){
            address.detail=set.config.Address
        }else{
            lat=set.lat
            lng=set.lng
        }
    }
}

fun IMapSearchSet.searchFromServer(from:Context?,back:OnMultiPageResponseBack<LoadLocationResonse>){

    val body= this.toLoadLocationBody()

    from.notNull {
        object:LoadLocationProcess(it,true){
            override val body: LoadLocationBody
                get() = body
            override val onResponse: OnMultiPageResponseBack<LoadLocationResonse>?
                get() = back
        }.run()
//        MultiPageRequestTask<LoadLocationResonse>(it,request,true)
//                .setExecuteListener(back).start()
    }
}

fun IMapSearchSet.checkInRange(to:LatLng,back:((Double)->Unit)?=null):Boolean{
    val dis=DistanceUtil.calDistance(searchLatLng,to,config.unitEnum)
    back?.invoke(dis)
    return config.Range.toDouble()>=dis
}

fun Location?.getLatLng():LatLng{
    notNull {
        return LatLng(it.latitude,it.longitude)
    }
    return LatLng(0.0,0.0)
}
fun LoadLocationInfo?.getLatLng():LatLng{
    notNull {
        return LatLng(it.Lat,it.Lng)
    }
    return LatLng(0.0,0.0)
}

