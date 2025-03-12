package com.eki.parking.Controller.dialog.child

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IAppTheme
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialEvent
import com.eki.parking.Model.EnumClass.PPYPTheme
import com.eki.parking.Model.EnumClass.SitePosition
import com.eki.parking.Model.request.body.AddLocationBody
import com.eki.parking.R
import com.eki.parking.databinding.DialogAddlocStep2Binding
import com.eki.parking.extension.string
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/04/10
 */

class AddLocStep2Frag : DialogChildFrag<AddLocStep2Frag>(),
    ISetData<AddLocationBody>,
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback,
    ISerialDialog, IAppTheme, IFragViewBinding {

    private lateinit var binding:DialogAddlocStep2Binding
    private lateinit var body:AddLocationBody
    private lateinit var serialEvent:ISerialEvent

    private lateinit var mGoogleMap: GoogleMap
    private var mLocationRequest: LocationRequest? = null

    private lateinit var geoCoder:Geocoder
    private lateinit var searchLocation:List<Address>

    companion object {
        private const val LOCATION_PERMISSION = 1001
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogAddlocStep2Binding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {
        initMap()
        setProgress()
        setAddress()
        setPosition()
        setBtn()
    }

    private fun initMap() {
        var mapFrag = childFragmentManager.findFragmentById(R.id.add_loc_2_mapFrag) as SupportMapFragment
        mapFrag.getMapAsync(this)

        geoCoder = Geocoder(requireContext())
    }

    private fun setAddress(){
        var location = ""
        val locDetail = binding.addLoc2AddressLayout.locDetail

        locDetail.hint = getString(R.string.Example_address)
        locDetail.textSize = 14F

        locDetail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                location = s.toString().cleanTex

                runCatching {
                    body.address.detail= location
                    toNext2(body)
                }.onFailure {
                    body.address.city = ""
                    body.address.detail = ""
                }

                if (location.isNotEmpty()) {
                    binding.addLoc2AddressLayout.sendingText
                        .setTextColor(getColor(R.color.Eki_green_2))
                } else {
                    binding.addLoc2AddressLayout.sendingText
                        .setTextColor(getColor(R.color.text_color_2))
                }
            }
        })

        locDetail.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                inputTextUpdateMap(location)
                hideKeyboard(requireContext())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.addLoc2AddressLayout.sendingText.setOnClickListener {
            inputTextUpdateMap(location)
            hideKeyboard(requireContext())
        }
    }

    private fun inputTextUpdateMap(location: String) {
        var latLng = LatLng(0.0,0.0)

        searchLocation = geoCoder.getFromLocationName(location,1)
        latLng = LatLng(searchLocation[0].latitude, searchLocation[0].longitude)
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16F))
    }

    private fun hideKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow( activity?.currentFocus?.windowToken,0)
    }

    private fun setPosition() {
        binding.addLoc2PlaceLayout.positionSelector.setOnCheckedChangeListener { _, checkedId ->
            body.info.position = if(checkedId ==  R.id.indoor){
                SitePosition.InSide.value
            } else {
                SitePosition.OutSide.value
            }

            toNext2(body)
        }
    }

    private fun setProgress(){

//        val siteSetProgress = ParkingSiteSet()
        view?.let { view ->
//            siteSetProgress.initProgress(it, 0.5, R.drawable.shape_round_progress_orange4_20dp)
            val displayMetrics = view.resources.displayMetrics
            val height = (displayMetrics.heightPixels * 0.009).toInt()
            val width = displayMetrics.widthPixels
            val progress = binding.addLoc2ProgressBar
            progress.setBackgroundResource(R.drawable.shape_round_progress_orange4_20dp)
            val params = ConstraintLayout.LayoutParams((width * 0.5).toInt(), height)
            progress.layoutParams = params
        }
    }

    private fun setBtn(){
        binding.addLoc2NextLayout.toPreviewBtn.setOnClickListener {
            serialEvent.onPrevious()
        }
        binding.addLoc2NextLayout.toNextBtn.setOnClickListener {
            serialEvent.onNext()
        }
    }

    private fun toNext2(body: AddLocationBody) {
        binding.addLoc2NextLayout.toNextBtn.isEnabled = body.address.detail.isNotEmpty()
    }

    override val frag: DialogChildFrag<*>
        get() = this
    override val title: String
        get() = when {
            body.info.size <= 1 -> {
                string(R.string.Add_car_parking_space)
            }
            body.info.size == 2 -> {
                string(R.string.New_locomotive_parking_space)
            }
            else->{ string(R.string.Parking_Site_Setting) }
        }

    override fun setEvent(event: ISerialEvent) {
        serialEvent=event
    }

    override fun next(): ISerialDialog = AddLocStep3Frag().also { it.setData(body) }
    override fun setData(data: AddLocationBody?) { data.notNull { body=it } }
    override var theme: PPYPTheme = PPYPTheme.Manager

    //處理 GoogleMap 物件的事件與使用者互動 -Linda
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        //取得camera移動後的中心點之經緯度 -Linda
        mGoogleMap.setOnCameraIdleListener {
            val center = mGoogleMap.cameraPosition.target
            body.lat = center.latitude
            body.lng = center.longitude
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (checkPermission()) {
            //開啟右上角定位icon -Linda
            mGoogleMap.isMyLocationEnabled = true

            // initialize location request object -Linda
            mLocationRequest = LocationRequest.create()
            mLocationRequest?.run {
                //提供較精確的位置
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                // initialize location setting request builder object -Linda
                val builder = LocationSettingsRequest.Builder()
                builder.addLocationRequest(this)
                val locationSettingsRequest = builder.build()

                // initialize location service object -Linda
                val settingsClient = LocationServices.getSettingsClient(requireActivity())
                settingsClient?.checkLocationSettings(locationSettingsRequest)
            }
            // call register location listener -Linda
            registerLocationListener()
        }
    }

    private fun registerLocationListener() {
        // initialize location callback object -Linda
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { onLocationChanged(it) }
            }
        }
        // 取得當前位置
        if(checkPermission()) {
            mLocationRequest?.let {
                LocationServices.getFusedLocationProviderClient(requireActivity())
                    .requestLocationUpdates(it, locationCallback, Looper.myLooper())
            }
        }
    }

    private fun onLocationChanged(location: Location) {
        // show toast message with updated location -Linda
        val mLocation = LatLng(location.latitude, location.longitude)
        mGoogleMap.clear()

        //相機縮放(16.0F)和移動至當前位置(location) -Linda
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation,16.0F))
    }

    private fun checkPermission():Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage(
                        "This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION
                                ,Manifest.permission.ACCESS_COARSE_LOCATION),
                            LOCATION_PERMISSION)
                    }
                    .create()
                    .show()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION
                        ,Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION)
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults.size == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                startLocationUpdates()
            } else {
                // permission denied
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts(
                                "package", requireActivity().packageName, null),
                        ),
                    )
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}