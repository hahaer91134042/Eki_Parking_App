package com.eki.parking.Model.impl

import android.content.Context
import android.view.View
import com.eki.parking.Model.sql.EkiLocation
import com.google.android.gms.maps.model.LatLng

/**
 * Created by Hill on 26,09,2019
 */
interface IMarkerFeature<ICON:View> {
    fun getPosition():LatLng
    fun getMarkerTitle():String
    fun getSnippet():String
    fun getMarkerView(context:Context?):ICON
    fun getLocationData():EkiLocation
}