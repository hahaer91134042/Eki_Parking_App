package com.eki.parking.Controller.impl

import com.eki.parking.Model.sql.LoadLocationConfig
import com.google.android.gms.maps.model.LatLng

/**
 * Created by Hill on 2019/12/06
 */
abstract class IMapSearchSet{

    //要用來搜尋的中心點
    abstract val searchLatLng:LatLng

    val lat:Double by lazy { searchLatLng.latitude }
    val lng:Double by lazy { searchLatLng.longitude }
//    open val address:String=""
    abstract val config: LoadLocationConfig
    open val isMove=true
//    open val isAddress:Boolean=false

    fun changeLoc(loc:LatLng):IMapSearchSet=
            object :IMapSearchSet(){
                override val searchLatLng: LatLng
                    get() = loc
//                override val address: String
//                    get() = this@IMapSearchSet.address
                override val config: LoadLocationConfig
                    get() = this@IMapSearchSet.config
                override val isMove: Boolean
                    get() = this@IMapSearchSet.isMove
//                override val isAddress: Boolean
//                    get() = this@IMapSearchSet.isAddress
            }
}