package com.eki.parking.Controller.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import com.eki.parking.App

import com.eki.parking.Controller.listener.GpsListener
import com.eki.parking.Controller.listener.GpsPlusListener
import com.eki.parking.R
import com.eki.parking.extension.string
import com.hill.devlibs.extension.showToast
import com.hill.devlibs.tools.Log

@SuppressLint("MissingPermission")
class GPS @JvmOverloads constructor(
    private var app: App,
    listener: GpsListener? = null
) : ForegroundCallbacks.LifeCycleListener {

//    constructor(context: App):this(context,null)
    //    private GoogleApiClient googleApiClient;
    //        implements
    //                    GoogleApiClient.ConnectionCallbacks,
    //                    GoogleApiClient.OnConnectionFailedListener{

    interface MotionConfig {
        /*
        * meters
        * */
        val offsetDistance: Float

        /*
        * milliseconds
        * */
        val offsetTime: Long
    }

    companion object {
        var instance: GPS? = null
        private val listenerList = ArrayList<GpsListener?>()

        @JvmStatic
        fun addListener(l: GpsListener?) {
            if (l != null) {
                if (!listenerList.contains(l))
                    listenerList.add(l)
                if (l is GpsPlusListener)
                    instance?.updateLocationListener(l)
            }
        }

        @JvmStatic
        fun removeListener(l: GpsListener?) {
            if (l != null) {
                if (listenerList.contains(l))
                    listenerList.remove(l)
                if (l is GpsPlusListener)
                    instance?.setDefaultGpsListener()
            }
        }
    }

    private object GpsConfig {
        val minTime: Long = 300// ms
        val minDist = 0.0f// meter
    }


    private var activity: Activity? = null

    // flag for GPS status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false
    private var canGetLocation = false

    // Helper for GPS-Position
    private val mlocListener = MyLocationListener()
    private var mlocManager: LocationManager? = null
    var location: Location? = null
    //    String provider;

    var isRunning: Boolean = false
        private set

    var latitude = 0.0 // latitude
    var longitude = 0.0 // longitude


    init {
//        ForegroundCallbacks.getInstance(app).setForegroundListener(this)
        instance = this
        addListener(listener)
    }

    fun start() {
        //<!--------------GPS取得位置的步驟------------------------------------>
        // GPS Position先宣告一個LocationManager來取得系統定位服務
        mlocManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        setDefaultGpsListener()

        //        Criteria criteria = new Criteria();//行動準則
        //		criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //		 provider = mlocManager.getBestProvider(criteria, true);//GPS提供者
        //        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime, minDist, mlocListener);//開啟監聽
        //(provoder,更新時間,最少移動距離才更新,監聽者)
        //        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
        // GPS Position END
        this.isRunning = true
        getGpsLocation()
    }

    /*
    * 目前沒用
    * */
    fun updateLocationListener(config: MotionConfig) {
        updateLocationListener(config.offsetTime, config.offsetDistance)
    }

    private fun updateLocationListener(time: Long, distance: Float) {
        //Log.i("set gps time->${time} distance->${distance}")
        mlocManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            time,
            distance,
            mlocListener
        )

        mlocManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            time,
            distance,
            mlocListener
        )
    }

    private fun setDefaultGpsListener() {
        updateLocationListener(GpsConfig.minTime, GpsConfig.minDist)
    }

    fun checkNowLocation() {
        Log.w("--GPS-- checkNowLocation->$location")
        if (location != null) {
            listenerList.forEach {
                it?.locationChanged(location, longitude, latitude)
            }
        } else {
//            Log.d("--GPS-- activity->$activity")

            openGpsSetting()
        }
    }

    fun haveGps(): Boolean = location != null

    fun openGpsSetting() {
        activity?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    /**
     * Function to get the user's current location
     * 這個位置抓取的會是晶片上一開始的位置 不要開放呼叫
     * 直接使用 location就好
     * @return
     */
//    @SuppressLint("MissingPermission")
    private fun getGpsLocation(): Location? {
        try {


            // getting GPS status
            isGPSEnabled = mlocManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false

            Log.v("GPS->isGPSEnabled=$isGPSEnabled")

            // getting network status
            isNetworkEnabled = mlocManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ?: false

            Log.v("GPS->isNetworkEnabled=$isNetworkEnabled")

            if (!isGPSEnabled && !isNetworkEnabled) {
                location = null
            } else {
                this.canGetLocation = true

                var netLocation: Location? = null
                var gpsLocation: Location? = null
                if (isNetworkEnabled)
                    netLocation =
                        mlocManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (isGPSEnabled)
                    gpsLocation = mlocManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                location = gpsLocation ?: netLocation

                if (location != null) {
                    latitude = location!!.latitude
                    longitude = location!!.longitude
                }
            }

            //            getCurrentLocation();

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    //Getting current location
    //    private void getCurrentLocation() {
    //        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
    //                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    //            // TODO: Consider calling
    //            //    ActivityCompat#requestPermissions
    //            // here to request the missing permissions, and then overriding
    //            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //            //                                          int[] grantResults)
    //            // to handle the case where the user grants the permission. See the documentation
    //            // for ActivityCompat#requestPermissions for more details.
    //            return;
    //        }
    //        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    //        if (location != null) {
    //            //Getting longitude and latitude
    //            longitude = location.getLongitude();
    //            latitude = location.getLatitude();
    //
    //            //moving the map to location
    //        }
    //    }

    fun stopGPS() {
        if (isRunning) {
            mlocManager?.removeUpdates(mlocListener)//取消監聽
            this.isRunning = false
        }
    }

    @Deprecated("Not use now")
    @SuppressLint("MissingPermission")
    fun resumeGPS() {
        mlocManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            GpsConfig.minTime,
            GpsConfig.minDist,
            mlocListener
        )//重新開啟監聽
        mlocManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            GpsConfig.minTime,
            GpsConfig.minDist,
            mlocListener
        )
        this.isRunning = true
    }

    //    @Override
    //    public void onConnected(@Nullable Bundle bundle) {
    //        getCurrentLocation();
    //    }
    //
    //    @Override
    //    public void onConnectionSuspended(int i) {
    //
    //    }
    //
    //    @Override
    //    public void onConnectionFailed(@NonNull ConnectionResult result) {
    //
    //    }


    private inner class MyLocationListener : LocationListener {

//        private val TAG = MyLocationListener::class.java.simpleName

        override fun onLocationChanged(loc: Location) {
            longitude = loc.longitude
            latitude = loc.latitude
            location = loc
            listenerList.forEach {
                it?.locationChanged(loc, longitude, latitude)//設定監聽到的經緯度傳到IGPSActivity類別
            }
        }

        override fun onProviderDisabled(provider: String) {
//            Log.w("--GPS--  onProviderDisabled->$provider")
            location = null
            activity.showToast(string(R.string.Please_turn_on_location_permissions))
//            openGpsSetting()

            listenerList.forEach {
                it?.onGpsDisable()
            }
//            listener?.displayGPSSettingsDialog()//設定沒有GPS失去提供者的接口給IGPSActivity類別裡的displayGPSSettingsDialog()
        }

        override fun onProviderEnabled(provider: String) {
//            Log.i("--GPS--  onProviderEnabled->$provider")
            getGpsLocation()
            listenerList.forEach {
                it?.onGpsEnable()
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

    }

    override fun onCreat(activity: Activity?) {
//        Log.d("--GPS-- onCreat->$activity")
    }

    override fun onResume(activity: Activity?) {
//        Log.w("--GPS-- onResume->$activity")
        this.activity = activity
    }

    override fun onStop(activity: Activity?) {
//        Log.i("--GPS-- onStop->$activity")
    }

    override fun onDestory(activity: Activity?) {
//        Log.d("--GPS-- onCreat->$activity")
    }
}
