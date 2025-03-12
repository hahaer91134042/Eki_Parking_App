package com.eki.parking.Controller.activity.frag.Filter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.AppRequestCode
import com.eki.parking.AppResultCode
import com.eki.parking.Controller.activity.intent.DateSearchIntent
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.DTO.SocketSelect
import com.eki.parking.Model.collection.SocketSelectList
import com.eki.parking.Model.sql.LoadLocationConfig
import com.eki.parking.Model.sql.LoadLocationConfig.SearchVehicle
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.ViewTypeAdaptor
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.FragFilter2Binding
import com.eki.parking.extension.color
import com.eki.parking.extension.string
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.recycleview.model.RecycleViewModel
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2021/12/07
 */
class FilterFrag : SearchFrag(), ISetData<LoadLocationConfig>, IFragViewBinding {

    //地圖傳遞來的
//    private lateinit var rawConfig:LoadLocationConfig

    private lateinit var searchConfig: LoadLocationConfig//暫時副本
    private lateinit var binding: FragFilter2Binding

    private var viewList = ArrayList<RecycleViewModel<LoadLocationConfig>>()
    private var searchEventList = ArrayList<(SearchCondition) -> Unit>()

    var onFinishSearch: (LoadLocationConfig) -> Unit = {}

    override fun initFragView() {

        Log.w("car charge->${searchConfig.CarCharge}")
        Log.i("motor charge->${searchConfig.MotorCharge}")

        viewList.add(RecycleViewModel(conditionSelect))

        when (searchConfig.searchVehicle) {
            SearchVehicle.Motor -> viewList.add(RecycleViewModel(motor))
            SearchVehicle.Car -> viewList.add(RecycleViewModel(car))
            else -> {
                viewList.add(RecycleViewModel(motor))
                viewList.add(RecycleViewModel(car))
            }
        }

        viewList.add(RecycleViewModel(datePicker))
        viewList.add(RecycleViewModel(final))

        binding.recycleView.useVerticalView()
        binding.recycleView.adapter = FilterViewAdaptor()
    }

    private fun setSearch(c: SearchCondition) {
        Log.w("setSearch->$c")
        searchEventList.forEach { it(c) }
    }

    override fun onResumeFragView() {
        toolBarTitle = string(R.string.Filter)
    }

    override fun setData(data: LoadLocationConfig?) {
        data.notNull { searchConfig = it }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.w("$TAG onActivityResult requestCode->$requestCode resultCode->$resultCode data->$data")
        if (requestCode == AppRequestCode.OnDateSearch &&
            resultCode == AppResultCode.OnDateSearchResult &&
            data != null
        ) {
            searchConfig.SearchTime = data.getParcel(AppFlag.DATA_FLAG)
//            searchConfig.printValue()
            onSearchDate?.invoke()
        }
    }

    private var onSearchDate: (() -> Unit)? = null

    private companion object ViewType {
        const val conditionSelect = 0
        const val motor = 1
        const val car = 2
        const val datePicker = 3
        const val range = 4
        const val final = 5
    }

    private inner class FilterViewAdaptor :
        ViewTypeAdaptor<LoadLocationConfig>(context) {

        override val modelList: ModelList<LoadLocationConfig>
            get() = ModelList(viewList)
        override val viewSets: SetList<LoadLocationConfig>
            get() = SetList(object : OptionItem() {
                override val viewType: Int
                    get() = conditionSelect

                override fun itemBack(parent: ViewGroup): ItemLayout<LoadLocationConfig> =
                    ConditionSelectItem(getItemView(R.layout.item_filter_search_condition, parent))
                        .also { it.init() }
            }, object : OptionItem() {
                override val viewType: Int
                    get() = motor

                override fun itemBack(parent: ViewGroup): ItemLayout<LoadLocationConfig> =
                    MotorSocketItem(getItemView(R.layout.item_filter_motor_socket, parent))
                        .also { it.init() }
            }, object : OptionItem() {
                override val viewType: Int
                    get() = car

                override fun itemBack(parent: ViewGroup): ItemLayout<LoadLocationConfig> =
                    CarSocketItem(getItemView(R.layout.item_filter_car_socket, parent))
                        .also { it.init() }
            }, object : OptionItem() {
                override val viewType: Int
                    get() = datePicker

                override fun itemBack(parent: ViewGroup): ItemLayout<LoadLocationConfig> =
                    DatePicketItem(getItemView(R.layout.item_filter_datepicker, parent))
                        .also { it.init() }
            }, object : OptionItem() {
                override val viewType: Int
                    get() = final

                override fun itemBack(parent: ViewGroup): ItemLayout<LoadLocationConfig> =
                    FinalItem(getItemView(R.layout.item_filter_determin, parent))
                        .also { it.init() }
            })


    }

    private inner class ConditionSelectItem(itemView: View) :
        ItemLayout<LoadLocationConfig>(itemView) {

        var selectGroup = itemView.findViewById<RadioGroup>(R.id.modeSelector)

        override fun init() {
//            Log.w("btn id default->${R.id.rbDefault} custom->${R.id.rbCustom}")

        }

        override fun refresh(data: LoadLocationConfig?) {
            super.refresh(data)
//            Log.d("ConditionSelectItem refresh->${data}")

//            Log.w("FilterFrag searchConfig isDefault->${searchConfig.isDefault()}")


            var checkID = when {
                searchConfig.isDefault() -> R.id.rbDefault
                else -> R.id.rbCustom
            }
//            Log.d("group check id->${checkID}  isDefault->${searchConfig.CarCharge==LoadLocationConfig().CarCharge}")
            selectGroup.check(checkID)
//            selectGroup.checkedRadioButtonId
            selectGroup.setOnCheckedChangeListener { group, checkedId ->

//                Log.i("select group check id->${checkedId}")
                when (checkedId) {
                    R.id.rbDefault -> {
                        searchConfig.copyFrom(LoadLocationConfig())

                        setSearch(SearchCondition.Default)
                    }
                    else -> {
                        //自訂一開始都是取消
                        searchConfig.copyFrom(LoadLocationConfig().apply {

                            CarCharge.clear()
                            MotorCharge.clear()
                        })
                        setSearch(SearchCondition.Custom)
                    }
                }
            }
        }
    }

    private inner class MotorSocketItem(itemView: View) : ItemLayout<LoadLocationConfig>(itemView) {

        private var btnList = listOf(
            object : MotorOptionBtn(itemView.findViewById(R.id.eMovingBtn)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.eMoving
            }, object : MotorOptionBtn(itemView.findViewById(R.id.pbgnBtn)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.PBGN
            }, object : MotorOptionBtn(itemView.findViewById(R.id.ionexBtn)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.ionex
            }, object : MotorOptionBtn(itemView.findViewById(R.id.homeBtn)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.Home
            }, object : MotorOptionBtn(itemView.findViewById(R.id.generalBtn)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.None
            }
        )

        override fun init() {
            searchEventList.add(event)

        }

        override fun refresh(data: LoadLocationConfig?) {
            super.refresh(data)
//            Log.d("MotorSocketItem refresh->${data}")
            event(
                when {
                    searchConfig.isDefault() -> SearchCondition.Default
                    else -> SearchCondition.Custom
                }
            )
        }

        private val event: (SearchCondition) -> Unit = { con ->
//            Log.d("Motor Event ->${con}")

            btnList.forEach { btn -> btn.setSelect(searchConfig.MotorCharge) }

            when (con) {
                SearchCondition.Default -> {
                    btnList.forEach { btn -> btn.view.isEnabled = false }
                }
                else -> {
                    btnList.forEach { btn -> btn.view.isEnabled = true }
                }
            }
        }
    }

    private inner class CarSocketItem(itemView: View) : ItemLayout<LoadLocationConfig>(itemView) {


        private var btnList = listOf(
            object : CarOptionBtn(itemView.findViewById(R.id.tesla_ac)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.Tesla_ac
            }, object : CarOptionBtn(itemView.findViewById(R.id.j1772_ac)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.J1772
            }, object : CarOptionBtn(itemView.findViewById(R.id.tesla_dc)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.Tesla_dc
            }, object : CarOptionBtn(itemView.findViewById(R.id.ccs1_j1772_dc)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.CCS1
            }, object : CarOptionBtn(itemView.findViewById(R.id.ccs2_iec62196_dc)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.CCS2
            }, object : CarOptionBtn(itemView.findViewById(R.id.chademo_dc)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.CHAdeMO
            }, object : CarOptionBtn(itemView.findViewById(R.id.generalCar)) {
                override val btnOption: SocketSelect
                    get() = SocketSelect.None
            })

        override fun init() {
            searchEventList.add(event)
        }

        override fun refresh(data: LoadLocationConfig?) {
            super.refresh(data)
//            Log.d("CarSocketItem refresh->${data}")
            event(
                when {
                    searchConfig.isDefault() -> SearchCondition.Default
                    else -> SearchCondition.Custom
                }
            )
        }

        private val event: (SearchCondition) -> Unit = { con ->
            btnList.forEach { btn -> btn.setSelect(searchConfig.CarCharge) }

            when (con) {
                SearchCondition.Default -> {
                    btnList.forEach { btn -> btn.view.isEnabled = false }
                }
                else -> {
                    btnList.forEach { btn -> btn.view.isEnabled = true }
                }
            }
        }
    }

    private inner class DatePicketItem(itemView: View) : ItemLayout<LoadLocationConfig>(itemView) {

        var datePickerBtn = itemView.findViewById<View>(R.id.datePickerBtn)
        var iconDatePicker = itemView.findViewById<ImageView>(R.id.datePicker)
        var selectText = itemView.findViewById<TextView>(R.id.selectText)

        override fun init() {
            datePickerBtn.setOnClickListener {
                Log.d("date picker click")
                startActivitySwitchAnim(DateSearchIntent(context!!, searchConfig.SearchTime), true)
            }
            onSearchDate = {
                setViewSelectable()
            }
        }

        override fun refresh(data: LoadLocationConfig?) {
            super.refresh(data)
            setViewSelectable()
        }

        fun setViewSelectable() {
            if (searchConfig.SearchTime.isDefault()) {
                selectText.text = string(R.string.Please_choose)
                selectText.setTextColor(color(R.color.text_color_1))
                iconDatePicker.isSelected = false
            } else {
                selectText.text = string(R.string.Chosen)
                selectText.setTextColor(color(R.color.Eki_orange_4))
                iconDatePicker.isSelected = true
            }
        }
    }

    private inner class FinalItem(itemView: View) : ItemLayout<LoadLocationConfig>(itemView) {
        var determinBtn = itemView.findViewById<Button>(R.id.determinBtn)

        override fun init() {
            determinBtn.setOnClickListener {
                onFinishSearch(searchConfig)
            }
        }
    }

    private abstract class OptionItem : ViewTypeAdaptor.ItemTypeSet<LoadLocationConfig>

    private abstract inner class MotorOptionBtn(view: TextView) : OptionBtn(view) {
        override fun whenOptionSelect(s: SocketSelect) {
            addOption(searchConfig.MotorCharge)
        }
    }

    private abstract inner class CarOptionBtn(view: TextView) : OptionBtn(view) {
        override fun whenOptionSelect(s: SocketSelect) {
            addOption(searchConfig.CarCharge)
        }
    }

    private abstract inner class OptionBtn(override val view: TextView) : IViewControl<TextView>(),
        IViewControl.IClickViewSet<TextView> {
        init {
            useDefaultSet(view)
//            view.setOnClickListener {
//                setSelect(!view.isSelected)
//            }
        }

        abstract val btnOption: SocketSelect

        protected abstract fun whenOptionSelect(s: SocketSelect)

        fun setSelect(list: SocketSelectList) {
            setSelect(list.any { it == btnOption })
        }

        private fun setSelect(s: Boolean) {
            view.isSelected = s
            view.setTextColor(
                when (s) {
                    true -> Color.orange
                    else -> Color.default
                }
            )
        }

        override fun viewAfterClick(clickView: TextView) {
            setSelect(!view.isSelected)
            Log.w("option car before->${searchConfig.CarCharge}")
            Log.w("option motor before->${searchConfig.MotorCharge}")

            whenOptionSelect(btnOption)

            Log.i("option car after->${searchConfig.CarCharge}")
            Log.i("option motor after->${searchConfig.MotorCharge}")
        }

        protected fun addOption(charges: SocketSelectList) {
            if (view.isSelected && !charges.any { it == btnOption }) {
                charges.add(btnOption)
            } else {
                charges.remove(btnOption)
            }
        }

        override fun clickViewSet(clickView: TextView) {

        }
    }

    //這樣避免重複讀取
    object Color {
        val orange = color(R.color.Eki_orange_4)
        val default = color(R.color.text_color_1)
    }

    private enum class SearchCondition {
        Default,
        Custom
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragFilter2Binding.inflate(inflater, container, false)
        return binding
    }
}