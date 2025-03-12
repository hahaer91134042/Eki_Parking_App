package com.eki.parking.Controller.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.eki.parking.App
import com.eki.parking.AppConfig
import com.eki.parking.Controller.listener.OnSelectListener
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.R
import com.eki.parking.extension.color
import com.eki.parking.extension.equal
import com.eki.parking.utils.Logger
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.tools.Log
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Created by Hill on 2018/10/16.
 */

abstract class EkiMap<MAPTYPE> {
    var map: MAPTYPE? = null
    var zoom = 6.5f

    protected var listener: OnSelectListener<EkiLocation?>? = null

    abstract fun creat(map: MAPTYPE?, set: IMapSet): EkiMap<MAPTYPE>

    @Deprecated(message = "use MapMarker")
    abstract fun addMarker(lat: Double, lng: Double): EkiMap<MAPTYPE>

    @Deprecated(message = "use MapMarker")
    abstract fun addMarker(latlng: LatLng): EkiMap<MAPTYPE>

    abstract fun addMarker(marker: MapMarker): EkiMap<MAPTYPE>
    abstract fun checkRepeatMarker()
    abstract fun removeAllMarker()

    abstract fun addPolyLine(polyOptions: PolylineOptions): EkiMap<MAPTYPE>
    abstract fun betweenZoom(start: LatLng, end: LatLng): EkiMap<MAPTYPE>
    abstract fun moveCamera(lat: Double, lng: Double, isAnim: Boolean = false): EkiMap<MAPTYPE>
    abstract fun moveCamera(
        lat: Double,
        lng: Double,
        zoom: Float,
        isAnim: Boolean = false
    ): EkiMap<MAPTYPE>

    abstract fun moveCamera(latlng: LatLng, isAnim: Boolean = false): EkiMap<MAPTYPE>
    abstract fun addCircleInCenter(set: ICircleSet)
    abstract fun addCircle(loc: LatLng, set: ICircleSet)
    abstract fun centerLoc(): LatLng
    abstract fun setCircleRadius(range: Double)
    abstract val markerControl: MarkerControl

    abstract fun addOnCameraStopListener(l: OnCameraStopListener)
    abstract fun removeCameraStopListener(l: OnCameraStopListener)

    //    abstract fun moveCameraWithAnim(latlng: LatLng):EkiMap
//    abstract fun addMarker(lat:Double,lng:Double):EkiMap
    fun setOnMarkerSelectListener(l: (EkiLocation?) -> Unit) {
        listener = object : OnSelectListener<EkiLocation?> {
            override fun onSelect(data: EkiLocation?) = l(data)
        }
    }

    companion object {
        @JvmStatic
        fun load(map: GoogleMap?, set: IMapSet): EkiMap<GoogleMap?> {
            return Map().creat(map, set)
        }
    }

    interface IMapSet {
        val zoom: Float//數值越小 地圖範圍越大
        val mapType: Int
    }

    interface MarkerControl {
        fun startCycleMarker()
        fun stopCycleMarker()
    }

    interface OnCameraStopListener {
        fun onCameraMoveStop()
    }

    abstract class ICircleSet {
        abstract val circleColor: Int
        abstract val radius: Double

        open var strokeColor: Int = color(R.color.Eki_orange_2)
        open var strikeWidth: Float = 0f
    }


    private class Map : EkiMap<GoogleMap?>() {

        private data class MarkerPair(val ekiMarker: MapMarker, var gMarker: Marker)

        private var markerMap = LinkedHashMap<String, MarkerPair>()
        private var cycleShowMarkerTask = CycleShowMarkerTask()
        private var cameraStopListenerList = arrayListOf<OnCameraStopListener>()


        override fun creat(m: GoogleMap?, set: IMapSet): EkiMap<GoogleMap?> {
            zoom = set.zoom
            this.map = m


            // 設定地圖類型--------------------
            map?.mapType = set.mapType

            // --------------------------
            // 顯示我的位置圖示

            if (ActivityCompat.checkSelfPermission(
                    App.getInstance(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    App.getInstance(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                map?.isMyLocationEnabled = true // 顯示自己位置
            }

            map?.setOnMarkerClickListener {

                Log.d("Marker click position->${it.position} snippet->${it.snippet}")
//                Log.i("Select LocationData->${markerMap[it.snippet]?.location}")
//                selectListener?.onSelect(markerMap[it.snippet]?.location)
                listener?.onSelect(markerMap[it.snippet]?.ekiMarker?.location)
                false
            }


            val mUiSettings = map?.uiSettings

            // Keep the UI Settings state in sync with the checkboxes.
            // 顯示縮放按鈕
            //mUiSettings?.isZoomControlsEnabled = true
            // 顯示指北針
            mUiSettings?.isCompassEnabled = true
            // 顯示我的位置按鈕
            mUiSettings?.isMyLocationButtonEnabled = false
            //關閉點marker會浮出的工具欄
            mUiSettings?.isMapToolbarEnabled = false

            mUiSettings?.isScrollGesturesEnabled = true// 開啟地圖捲動手勢
            mUiSettings?.isZoomGesturesEnabled = true// 開啟地圖縮放手勢
//					mUiSettings.setTiltGesturesEnabled(isChecked(R.id.tilt_toggle));// 開啟地圖傾斜手勢
            mUiSettings?.isRotateGesturesEnabled = true// 開啟地圖旋轉手勢


            //camera閒置才觸發
            map?.setOnCameraIdleListener {
                cameraStopListenerList.forEach { it.onCameraMoveStop() }
                //保持同一個zoom
                zoom = map?.cameraPosition?.zoom ?: zoom
            }


            return this
        }

        override fun addMarker(marker: MapMarker): EkiMap<GoogleMap?> {
            try {
                map.notNull { gmap ->
//                Log.w("map add marker snippet->${marker.snippet}")
                    markerMap[marker.snippet] = MarkerPair(
                        marker,
                        gmap.addMarker(marker.toGoogleMarker())!!
                    )
                }
            } catch (e:Exception) {
                Logger.e("error:${e.message}")
            }

            return this
        }

        override fun checkRepeatMarker() {
            cycleShowMarkerTask.checkRepeatMarker()
        }

        private fun MapMarker.toGoogleMarker(): MarkerOptions = MarkerOptions().also {
            it.position(position)
            it.title(title)
            it.snippet(snippet)//這個不會顯示出來 用來當作紀錄識別碼用
            it.icon(icon)
        }

        private inner class CycleShowMarkerTask : Runnable, MarkerControl {

            private var handler = Handler()
            private var repeatList = arrayListOf<RepeatMarkerPair>()

            override fun run() {
                repeatList.filter { it.hasRepeat() }.forEach {
                    it.refreshMarker()
                }

                handler.postDelayed(this, AppConfig.markerRefreshTime)
            }

            fun checkRepeatMarker() {
                markerMap.forEach { p ->
                    val snippet = p.key
                    val markerPair = p.value
                    if (repeatList.any { it.position.equal(markerPair.gMarker.position) }) {
                        //如果是同地點的marker
                        val repeatPair =
                            repeatList.first { it.position.equal(markerPair.gMarker.position) }
                        //判斷是不是相同的marker
                        if (!repeatPair.markers.any { it.gMarker.snippet == snippet })
                            repeatPair.markers.add(markerPair)

                    } else {
                        repeatList.add(RepeatMarkerPair(markerPair.gMarker.position).apply {
                            markers.add(
                                markerPair
                            )
                        })
                    }
                }
            }

            fun clearRepeatMarker() {
                repeatList.clear()
            }

            override fun startCycleMarker() {
                handler.post(this)
            }

            override fun stopCycleMarker() {
                handler.removeCallbacks(this)
            }
        }

        private inner class RepeatMarkerPair(val position: LatLng) {
            val markers = ArrayList<MarkerPair>()
            var it = 0//計數

            var nowMarker: Marker? = null

            fun hasRepeat() = markers.size > 1

            fun refreshMarker() {
                markers.forEach { it.gMarker.remove() }
                nowMarker?.remove()
                if (it < markers.size) {
                    val pair = markers[it]
                    nowMarker = map?.addMarker(pair.ekiMarker.toGoogleMarker())

                    it++
                } else {
                    it = 0
                    val pair = markers[it]
                    nowMarker = map?.addMarker(pair.ekiMarker.toGoogleMarker())
                }
            }
        }

        override fun addMarker(lat: Double, lng: Double): EkiMap<GoogleMap?> {
            val latlng = LatLng(lat, lng)
            addMarker(latlng)
            return this
        }

        override fun addMarker(latlng: LatLng): EkiMap<GoogleMap?> {
            map?.addMarker(MarkerOptions().apply {
                position(latlng)
            })
            Log.i("--add marker--")
            return this
        }

        override fun removeAllMarker() {
            map?.clear()
            markerMap.clear()
            cycleShowMarkerTask.clearRepeatMarker()
        }

        override fun moveCamera(lat: Double, lng: Double, isAnim: Boolean): EkiMap<GoogleMap?> {
            moveCamera(LatLng(lat, lng), isAnim)
            return this
        }

        override fun moveCamera(
            lat: Double,
            lng: Double,
            zoom: Float,
            isAnim: Boolean
        ): EkiMap<GoogleMap?> {
            this.zoom = zoom
            moveCamera(LatLng(lat, lng), isAnim)
            return this
        }

        override fun moveCamera(latlng: LatLng, isAnim: Boolean): EkiMap<GoogleMap?> {
            if (isAnim)
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom))
            else
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom))
            return this
        }


        override fun addPolyLine(polyOptions: PolylineOptions): EkiMap<GoogleMap?> {
            map?.addPolyline(polyOptions)
            return this
        }

        //這個暫時沒用
        override fun betweenZoom(start: LatLng, end: LatLng): EkiMap<GoogleMap?> {
            val startLat = start.latitude
            val startLng = start.longitude

            val endLat = end.latitude
            val endLng = end.longitude

            val latLength = abs(startLat - endLat)
            val lngLength = abs(startLng - endLng)
            val slashLength = sqrt(latLength.pow(2.0) + lngLength.pow(2.0))

//            Log.w("Start lat->$startLat lng->$startLng || End lat->$endLat lng->$endLng")
//            Log.i("Lat between->$latLength || Lng between->$lngLength")
//            Log.d("slash sild->$slashLength")
            zoom = calculatorZoom((slashLength.toFloat() * 100.0f).toInt())
//            Log.d("zoom->$zoom")

            moveCamera((startLat + endLat) / 2, (startLng + endLng) / 2, true)

            return this
        }

        private fun calculatorZoom(length: Int): Float {
            return return when (length / 10) {
                0 -> {
                    when (length / 5) {
                        0 -> 12f
                        1 -> 11.5f
                        else -> 11.5f
                    }
                }
                1 -> 11f
                2 -> 10.5f
                3 -> 10f
                4 -> 9.5f
                5 -> 9f
                6 -> 8.5f
                else -> 7f

            }
        }

        private var mapCircle: Circle? = null
        override fun addCircleInCenter(set: ICircleSet) {

            mapCircle.notNull { it.remove() }
            map?.cameraPosition?.target.notNull { centerLoc ->
//                Log.w("centerLoc->$centerLoc")
                addCircle(centerLoc, set)
            }
        }

        override fun centerLoc(): LatLng {
            map?.cameraPosition?.target.notNull {
                return it
            }
            return LatLng(0.0, 0.0)
        }

        override fun addCircle(loc: LatLng, set: ICircleSet) {
            map?.clear()

            mapCircle = map?.addCircle(
                CircleOptions()
                    .center(loc)
                    .radius(set.radius)
                    .fillColor(set.circleColor)
                    .strokeColor(set.strokeColor)
                    .strokeWidth(set.strikeWidth)
            )
        }

        override fun setCircleRadius(range: Double) {
            mapCircle.notNull {
                it.radius = range
            }
        }

        override val markerControl: MarkerControl
            get() = cycleShowMarkerTask

        override fun addOnCameraStopListener(l: OnCameraStopListener) {
            cameraStopListenerList.add(l)
        }

        override fun removeCameraStopListener(l: OnCameraStopListener) {
            cameraStopListenerList.remove(l)
        }
    }
}