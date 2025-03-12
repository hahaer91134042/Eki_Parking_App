package com.eki.parking.Controller.activity.frag.Main

import android.content.Intent
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.AppRequestCode
import com.eki.parking.AppResultCode
import com.eki.parking.Controller.activity.intent.FilterIntent
import com.eki.parking.Controller.activity.intent.ReservaIntent
import com.eki.parking.Controller.asynctask.task.CreatMapMarkerTask
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IMapSearchSet
import com.eki.parking.Controller.listener.GpsListener
import com.eki.parking.Controller.listener.OnMultiPageResponseBack
import com.eki.parking.Controller.listener.OnSelectListener
import com.eki.parking.Controller.map.EkiMap
import com.eki.parking.Controller.map.MapMarker
import com.eki.parking.Controller.tools.GPS
import com.eki.parking.Model.DTO.MultiPageResponse
import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.response.LoadLocationResonse
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.LoadLocationConfig
import com.eki.parking.Model.sql.LoadLocationConfig.SearchVehicle
import com.eki.parking.Model.sql.SearchInfo
import com.eki.parking.R
import com.eki.parking.View.popup.LocationDetailPopup_2
import com.eki.parking.View.widget.LocationSearchBar
import com.eki.parking.databinding.FragMapBinding
import com.eki.parking.extension.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.listener.OnPostExecuteListener
import com.hill.devlibs.model.ValueObjContainer
import com.hill.devlibs.tools.Log


class MapFrag : SearchFrag(),
    OnMapReadyCallback,
    GpsListener, IFragViewBinding {

    lateinit var toChargePage: () -> Unit
    private var nowLocation: Location? = null

    private lateinit var binding:FragMapBinding
    private lateinit var map: EkiMap<GoogleMap?>
    private lateinit var nowSearchLatLng: LatLng

    private var locList = ArrayList<EkiLocation>()//這個 有搜尋過的話 會記錄在裡面 但是不會記錄在sql裡面

    private var isMapReady = false
    private lateinit var searchConfig: LoadLocationConfig
    private var searchText = SearchText()
    private lateinit var cameraStopEvent: MapCameraStopEvent

    @Deprecated("目前沒用")
    fun onCleanSearch() {
        searchConfig = LoadLocationConfig()
        searchText.clean()
        sqlSave(searchConfig)
        setToolBarActionViewText("")

        //下載成功的時候重新記錄到sql
        startSearchServer(object : IMapSearchSet() {
            override val searchLatLng: LatLng
                get() = nowLocation.getLatLng()
            override val config: LoadLocationConfig
                get() = searchConfig
        }) {

        }
    }

    //外部溝通用
    fun onStartSearch(config: LoadLocationConfig? = null) {
        if (config != null) {
            searchConfig = config
        }

        startSearchServer(SimpleMapSearchSet()) {//callback
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //Log.w("Google map is OK->${googleMap}")
        try {
            Log.w("OnMapReady lat->${gps?.latitude} lng->${gps?.longitude}")
            map = EkiMap.load(googleMap, object : EkiMap.IMapSet {
                override val zoom: Float
                    get() = 6.5f
                override val mapType: Int
                    get() = GoogleMap.MAP_TYPE_NORMAL
            })
            isMapReady = true
            map.markerControl.startCycleMarker()
            nowSearchLatLng = nowLocation.getLatLng()
            map.goMyLocation(nowLocation)

            cameraStopEvent = MapCameraStopEvent()
            map.addOnCameraStopListener(cameraStopEvent)
            loadData()
        } catch (e: Exception) {
            showToast(getString(R.string.Please_turn_on_location_permissions))
        }
    }


    override fun locationChanged(loc: Location?, longitude: Double, latitude: Double) {
        nowLocation = loc
    }

    override fun onGpsDisable() {
        gps.checkNowLocation()
    }

    override fun onGpsEnable() {}

    override fun initFragView() {
        val mapFrag = childFragmentManager.findFragmentById(R.id.mapFrag) as SupportMapFragment
        mapFrag.getMapAsync(this)

        sqlDataListAsync<EkiOrder> { list->
            val orderList = list.find{
                (it.isBeSettle()|| it.isBeCheckOut()) && (it.Cp != null ||it.Cp?.Serial != "") }

            if (orderList?.isEmpty() == false) {
                binding.toChargeButton.visibility = View.VISIBLE
            } else {
                binding.toChargeButton.visibility = View.GONE
            }
        }
    }

    override fun onResumeFragView() {
        if (isMapReady) {
            map.markerControl.startCycleMarker()
        }

        binding.locationSearchBar.setBarListener(LocSearchBarEvent())

        binding.myLocationBtn.setOnClickListener {
//            Log.d("map->$map   go my location->$nowLocation")
            //這樣回到原始點位才會開始搜尋
            cameraStopEvent.initLatLng = map.centerLoc()
            map.goMyLocation(nowLocation, 14f)
        }
        binding.refreshLocBtn.setOnClickListener {
            //測試
//            map.removeAllMarker()

            //正式
            startSearchServer(object : IMapSearchSet() {
                override val searchLatLng: LatLng
                    get() = nowLocation.getLatLng()
                override val config: LoadLocationConfig
                    get() = searchConfig
                override val isMove: Boolean
                    get() = false
            }) {//call back

            }
        }

        binding.filterBtn.setOnClickListener {
            startActivitySwitchAnim(FilterIntent(requireContext(), searchConfig), true)
        }

        binding.toChargeButton.setOnClickListener {
            toChargePage.invoke()
        }
    }

    private fun loadData() {
        if (locList.size < 1) {
            sqlManager?.getObjListAsync(EkiLocation::class.java) {
                locList.addAll(it)
                addMarker()
            }
        }
    }

    private fun addMarker(list: ArrayList<EkiLocation> = locList) {
        map.removeAllMarker()
        CreatMapMarkerTask(context, list.filter { it?.Config?.beEnable ?: false }.toArrayList())
            .setExecuteListener(object : OnPostExecuteListener<ArrayList<MapMarker>> {
                override fun onTaskPostExecute(result: ArrayList<MapMarker>) {
                    result.forEach {
                        map.addMarker(it)
                    }
                    map.checkRepeatMarker()
                }
            }).start()

        map.setOnMarkerSelectListener { loc ->
            loc.notNull {
                locationSelect.onSelect(list.select(it.Lat, it.Lng))
            }
        }
    }

    private var locationSelect = object : OnSelectListener<ArrayList<EkiLocation>> {
        override fun onSelect(data: ArrayList<EkiLocation>) {
            when {
                data.size > 1 -> {
                    LocationDetailPopup_2(context).apply {
                        setData(data)
                        onReservaLocation = { loc ->
                            Log.w("Select Loc SerNum->${loc.Info?.SerialNumber}")
                            startActivitySwitchAnim(ReservaIntent(context!!, loc), false)
                        }
                    }.showPopupWindow()
                }
                else -> {
//                    Log.d("Only one Location")
                    startActivitySwitchAnim(ReservaIntent(context!!, data.first()), false)
                }
            }

        }
    }

    //應該之後 不用使用了 目前 地址收尋還在使用
    override fun onCatchReceive(action: String?, intent: Intent?) {
        Log.d(
            "Receive action->$action Extra->${
                intent?.getParcelableExtra<ValueObjContainer<SearchInfo>>(
                    AppFlag.DATA_FLAG
                )
            }"
        )

        when (action) {
            AppFlag.OnDateSearch -> {
            }
        }
        //地址搜尋送過來的
        intent?.getParcelableExtra<ValueObjContainer<SearchInfo>>(AppFlag.DATA_FLAG).notNull {
            searchConfig?.Address = it.valueObj.Content

            sqlUpdate(searchConfig)
        }


        searchConfig.notNull {
            searchText.appendAddress(it.Address)
            setToolBarActionViewText(searchText.toString())
        }

        onStartSearch(searchConfig)
    }

    //這邊指搜尋距離跟插頭
    private fun drawLocation(set: IMapSearchSet) {

        //過濾開放日期 是向伺服器查詢
        //這邊之後要同時檢查電流 跟插頭類型
        var list = locList.filter {
//            Log.e("filter range->${set.checkInRange(it.getPosition())}")
            //檢查距離
            set.checkInRange(it.getPosition())
        }.filter {
            //檢查載具插頭
            set.config.checkSocket(it)
        }.filter {
            //檢查載具類型(汽機車)
            set.config.checkVehicle(it)
        }
        addMarker(list.toArrayList())
    }

    //這邊應該只有重新刷新的時候才要使用
    private fun startSearchServer(
        set: IMapSearchSet,
        back: (() -> Unit)? = null
    ) {
        nowSearchLatLng = set.searchLatLng
        set.searchFromServer(context, object : OnMultiPageResponseBack<LoadLocationResonse> {
            override fun onReTry() {
            }

            override fun onTaskPostExecute(result: ArrayList<MultiPageResponse<LoadLocationResonse>>) {

                result.isNotEmpty().isTrue {
                    locList.clear()

                    result?.forEach { res ->
                        res.response?.info?.List.notNull { data -> locList.addAll(data) }
                    }
                    sqlSaveAsync(locList)
//                    Log.d("Search result->$locList")

                    result[0]?.response?.info.notNull { info ->
//                        Log.w("Search result info->$info")

                        nowSearchLatLng = info.getLatLng()
                        if (set.isMove) {
                            map.moveCamera(
                                info.Lat,
                                info.Lng,
                                true
                            )
                        }
                        drawLocation(set.changeLoc(nowSearchLatLng))

                    }
                    back?.invoke()
                }
            }

            override fun onFail(errorMsg: String, code: String) {
                showToast(getString(R.string.Search_Fail))
            }
        })
    }


    override fun onResume() {
        super.onResume()
//        Log.i("${TAG} onResume")
        registerReceiver(AppFlag.GoSearch, AppFlag.OnDateSearch)
        sqlManager.getObjData(LoadLocationConfig::class.java).notNull {
            searchConfig = it

        }
        //這邊 因為是使用在fragment裡面 所有要重複設定
        //怪的是map不用
        GPS.addListener(this)
        gps?.checkNowLocation()

        Log.i("map frag onResum isMapReady->$isMapReady")
    }


    //這邊要生出新的view不然map無法用
    override fun isReCreatedView(): Boolean {
        return true
    }

    override fun onPause() {
        GPS.removeListener(this)
        super.onPause()
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragMapBinding.inflate(inflater,container,false)
        return binding
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.w("Map frag onActivityResult request->$requestCode result->$resultCode data->${data}")
        //這裡只有filter activity 回傳
        if (requestCode == AppRequestCode.OnFilter &&
            resultCode == AppResultCode.OnFilterResult &&
            data != null
        ) {
            runCatching {

                var newConfig = data.getParcel<LoadLocationConfig>(AppFlag.DATA_FLAG)
                var isSearchFromServer = searchConfig.let { oldConfig ->
                    oldConfig.SearchTime != newConfig.SearchTime
                }

                searchConfig = newConfig
                sqlSave(searchConfig)

                //假如沒有設定搜尋時間
                if (isSearchFromServer) {
                    startSearchServer(SimpleMapSearchSet())
                } else {
                    drawLocation(SimpleMapSearchSet())
                }

            }.onFailure {

            }
        }
    }

    private inner class LocSearchBarEvent : LocationSearchBar.OnSearchBarClickListener {
        override fun onSlmSelect() {

            searchConfig.searchVehicle = SearchVehicle.CarAndMotor

            drawLocation(SimpleMapSearchSet())
        }

        override fun onCarSelect() {
            searchConfig.searchVehicle = SearchVehicle.Car

            drawLocation(SimpleMapSearchSet())
        }

        override fun onLmSelect() {
            searchConfig.searchVehicle = SearchVehicle.Motor

            drawLocation(SimpleMapSearchSet())
        }

        override fun onCancelClick() {
            //這邊是取消所有的尋條件 只留下汽機車
        }

    }

    private inner class SimpleMapSearchSet : IMapSearchSet() {
        override val searchLatLng: LatLng
            get() = nowLocation.getLatLng()
        override val config: LoadLocationConfig
            get() = searchConfig
    }

    private inner class MapCameraStopEvent : EkiMap.OnCameraStopListener {

        //這是紀錄上一個舊的地點
        var initLatLng = nowLocation.getLatLng()

        override fun onCameraMoveStop() {
            var center = map.centerLoc()
            var set = object : IMapSearchSet() {
                override val searchLatLng: LatLng
                    get() = center
                override val config: LoadLocationConfig
                    get() = searchConfig
                override val isMove: Boolean
                    get() = false
            }


            var inDistance = set.checkInRange(initLatLng) { dis ->

                Log.w("init loc->$initLatLng center loc->$center distance->$dis ")
            }

            Log.d("camera stop nowLoc->${nowLocation.getLatLng()} search range->${searchConfig.Range} ${searchConfig.unitEnum} is in distance->$inDistance")
            //假如跑出了收尋距離 下載新的
            if (!inDistance) {
                startSearchServer(set) {//back
                    //搜尋成功之後 更新
                    initLatLng = center
                }
            }
        }
    }


    @Deprecated("目前暫時不用了")
    private class SearchText {
        private var addresStr = ""
        private var rangeStr = ""
        private var dateStr = ArrayList<String>()
        private var socketStr = ArrayList<ChargeSocket>()
        private var timeStr = ""

        fun appendAddress(addr: String): SearchText {
            addresStr = addr
            return this
        }

        fun appendRange(range: Double, unit: String): SearchText {
            rangeStr = "$range $unit"
            return this
        }

        fun appendDate(date: ArrayList<String>): SearchText {
            dateStr.addAll(date)
            return this
        }

        fun appendTime(start: String, end: String): SearchText {
            timeStr = "$start-$end"
            return this
        }

        override fun toString(): String {
            var str = StringBuilder()
            addresStr.isNotEmpty {
                str.append("$it|")
            }
            rangeStr.isNotEmpty {
                str.append("$it|")
            }

            var date = StringBuilder()
            dateStr.forEach {
                date.append("$it,")
            }
            if (date.isNotEmpty())
                date.replace(date.length - 1, date.length, "")
            date.toString().isNotEmpty {
                str.append("$it $timeStr|")
            }

            if (str.isNotEmpty())
                str.replace(str.length - 1, str.length, "")

            return str.toString()
        }

        fun clean() {
            addresStr = ""
            rangeStr = ""
            dateStr.clear()
            socketStr.clear()
            timeStr = ""
        }

    }
}