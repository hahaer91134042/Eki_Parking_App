package com.eki.parking.Controller.asynctask.task

import android.content.Context
import com.eki.parking.Controller.asynctask.abs.BaseAsyncTask
import com.eki.parking.Model.impl.IMarkerFeature
import com.eki.parking.Controller.map.MapMarker
import com.hill.devlibs.listener.OnPostExecuteListener

/**
 * Created by Hill on 26,09,2019
 */

class CreatMapMarkerTask<M: IMarkerFeature<*>>(
        context: Context?,
        private var list:ArrayList<M>) : BaseAsyncTask<ArrayList<MapMarker>,OnPostExecuteListener<ArrayList<MapMarker>>>(context,false) {

    override fun doInBackground(vararg params: Void?): ArrayList<MapMarker> {
        val markerList=ArrayList<MapMarker>()
        list.forEach {
//            Log.d("add marker Ava ${it.getLocationData().Available}  enum->${it.getLocationData().available}")
            markerList.add(MapMarker(it.getPosition()).apply {
                title=it.getMarkerTitle()
                snippet=it.getSnippet()
                location=it.getLocationData()
                setIcon(it.getMarkerView(context))
            })
        }

        return markerList
    }


}