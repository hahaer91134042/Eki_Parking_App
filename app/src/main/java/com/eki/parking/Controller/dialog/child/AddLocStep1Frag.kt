package com.eki.parking.Controller.dialog.child

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.builder.AddLocSerialBuilder
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IAppTheme
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialEvent
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.PPYPTheme
import com.eki.parking.Model.request.body.AddLocationBody
import com.eki.parking.R
import com.eki.parking.databinding.DialogAddlocStep1Binding
import com.eki.parking.extension.string
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.showToast
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/04/15
 */

class AddLocStep1Frag(var body: AddLocationBody,val type:Int) : DialogChildFrag<AddLocStep1Frag>(),
    ISetData<AddLocationBody>,
    ISerialDialog, IAppTheme, IFragViewBinding {

    private lateinit var serialEvent: ISerialEvent
    private lateinit var binding: DialogAddlocStep1Binding

    private lateinit var rbNo: RadioButton
    private lateinit var rbYes: RadioButton

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogAddlocStep1Binding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {
        initView()
        setProgress()
        isCarOrScooter(type)
        setNamePrice()
        setRadioGroup()
        setBtn()
    }

    private fun initView() {
        rbNo = binding.addLoc1ChargingInterface.rbNo
        rbYes = binding.addLoc1ChargingInterface.rbYes
    }

    private fun isCarOrScooter(type: Int) {
        if(type == AddLocSerialBuilder.car){
            binding.chargingTitleText.setText(R.string.Car_Charging)
            binding.chargingChipScooter.scooterChipLayout.visibility = View.GONE
        }else if(type == AddLocSerialBuilder.scooter) {
            binding.chargingTitleText.setText(R.string.Locomotive_Charging)
            binding.chargingChipCar.carChipLayout.visibility = View.GONE
        }
    }

    private fun setProgress() {
        //上方橘色進度條的顯示設定
        //暫時先將function拉出來 因為params設定的layout和原先不同 沿用會閃退
//        val siteSetProgress = ParkingSiteSet()
        view?.let { view ->
//            siteSetProgress.initProgress(
//                it,
//                0.25,
//                R.drawable.shape_round_progress_orange4_20dp
//            )
            val displayMetrics = view.resources.displayMetrics
            val height = (displayMetrics.heightPixels * 0.009).toInt()
            val width = displayMetrics.widthPixels
            val progress: View = binding.addLoc1ProgressBar
            progress.setBackgroundResource(R.drawable.shape_round_progress_orange4_20dp)
            val params = ConstraintLayout.LayoutParams((width * 0.25).toInt(), height)
            progress.layoutParams = params
        }
    }

    private fun setNamePrice() {
        var str = ""

        binding.itemParkingSpaceName.siteName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                str = s.toString().trim()
                runCatching {
                    body.info.content = str
                    toNext()
                }.onFailure {
                    body.info.content = ""
                }

            }

        })

        binding.addLoc1ChargePrice.locPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                str = s.toString().trim()
                runCatching {
                    body.config.price = str.toDouble()
                    toNext()
                }.onFailure {
                    body.config.price=0.0
                }

            }
        })
    }

    private fun setTextChip(isCharging:Boolean) {
        //依類型設定click事件

        if(type == AddLocSerialBuilder.car){
            if (isCharging) {
                //允許view的點擊
                binding.chargingChipCar.chipTesla.isClickable = true
                binding.chargingChipCar.chipJ1772.isClickable = true
                binding.chargingChipCar.chipDcTesla.isClickable = true
                binding.chargingChipCar.chipDcJ1772.isClickable = true
                binding.chargingChipCar.chipDcChademo.isClickable = true

                binding.chargingChipCar.chipTesla.setOnClickListener {
                    binding.chargingChipCar.chipTesla.isSelected =
                        !binding.chargingChipCar.chipTesla.isSelected

                    storeBody(4,1,binding.chargingChipCar.chipTesla.isSelected)
                }
                binding.chargingChipCar.chipJ1772.setOnClickListener {
                    binding.chargingChipCar.chipJ1772.isSelected =
                        !binding.chargingChipCar.chipJ1772.isSelected
                    storeBody(7,1,binding.chargingChipCar.chipJ1772.isSelected)
                }
                binding.chargingChipCar.chipDcTesla.setOnClickListener {
                    binding.chargingChipCar.chipDcTesla.isSelected =
                        !binding.chargingChipCar.chipDcTesla.isSelected
                    storeBody(4,2,binding.chargingChipCar.chipDcTesla.isSelected)
                }
                binding.chargingChipCar.chipDcJ1772.setOnClickListener {
                    binding.chargingChipCar.chipDcJ1772.isSelected =
                        !binding.chargingChipCar.chipDcJ1772.isSelected
                    storeBody(5,2,binding.chargingChipCar.chipDcJ1772.isSelected)
                }
                binding.chargingChipCar.chipDcChademo.setOnClickListener {
                    binding.chargingChipCar.chipDcChademo.isSelected =
                        !binding.chargingChipCar.chipDcChademo.isSelected
                    storeBody(3,2,binding.chargingChipCar.chipDcChademo.isSelected)
                }
            } else {
                //取消view的點擊
                binding.chargingChipCar.chipTesla.isClickable = false
                binding.chargingChipCar.chipJ1772.isClickable = false
                binding.chargingChipCar.chipDcTesla.isClickable = false
                binding.chargingChipCar.chipDcJ1772.isClickable = false
                binding.chargingChipCar.chipDcChademo.isClickable = false
                //將view狀態回復
                binding.chargingChipCar.chipTesla.isSelected = false
                binding.chargingChipCar.chipJ1772.isSelected = false
                binding.chargingChipCar.chipDcTesla.isSelected = false
                binding.chargingChipCar.chipDcJ1772.isSelected = false
                binding.chargingChipCar.chipDcChademo.isSelected = false
            }
        } else if (type == AddLocSerialBuilder.scooter) {
            if (isCharging) {
                binding.chargingChipScooter.chipEmoving.isClickable = true
                binding.chargingChipScooter.chipPbgn.isClickable = true
                binding.chargingChipScooter.chipIonex.isClickable = true
                binding.chargingChipScooter.chipHouse.isClickable = true

                binding.chargingChipScooter.chipEmoving.setOnClickListener {
                    binding.chargingChipScooter.chipEmoving.isSelected =
                        !binding.chargingChipScooter.chipEmoving.isSelected
                    storeBody(10,1,binding.chargingChipScooter.chipEmoving.isSelected)
                }
                binding.chargingChipScooter.chipPbgn.setOnClickListener {
                    binding.chargingChipScooter.chipPbgn.isSelected =
                        !binding.chargingChipScooter.chipPbgn.isSelected
                    storeBody(11,1,binding.chargingChipScooter.chipPbgn.isSelected)
                }
                binding.chargingChipScooter.chipIonex.setOnClickListener {
                    binding.chargingChipScooter.chipIonex.isSelected =
                        !binding.chargingChipScooter.chipIonex.isSelected
                    storeBody(12,1,binding.chargingChipScooter.chipIonex.isSelected)
                }
                binding.chargingChipScooter.chipHouse.setOnClickListener {
                    binding.chargingChipScooter.chipHouse.isSelected =
                        !binding.chargingChipScooter.chipHouse.isSelected
                    storeBody(13,1,binding.chargingChipScooter.chipHouse.isSelected)
                }
            } else {
                binding.chargingChipScooter.chipEmoving.isClickable = false
                binding.chargingChipScooter.chipPbgn.isClickable = false
                binding.chargingChipScooter.chipIonex.isClickable = false
                binding.chargingChipScooter.chipHouse.isClickable = false

                binding.chargingChipScooter.chipEmoving.isSelected = false
                binding.chargingChipScooter.chipPbgn.isSelected = false
                binding.chargingChipScooter.chipIonex.isSelected = false
                binding.chargingChipScooter.chipHouse.isSelected = false
            }
        }
    }

    private fun storeBody(clickCharge:Int,clickCurrent:Int,isSelected:Boolean) {
        var socket = RequestBody.LocationSocket()

        if (isSelected) {
            socket.current = clickCurrent
            socket.charge = clickCharge

            body.socket.add(socket)
        } else {
            val iterator = body.socket.iterator()

            while (iterator.hasNext()) {
                socket = iterator.next()
                if (socket.current == clickCurrent && socket.charge == clickCharge) {
                    iterator.remove()
                }
            }
        }
        toNext()
    }

    private fun setRadioGroup() {
        binding.addLoc1ChargingInterface.rbNo.isChecked = true
        setTextChip(false)

        val rg = binding.addLoc1ChargingInterface.radioGroup
        rg.setOnCheckedChangeListener { _: RadioGroup?, checkedId: Int ->
            when(checkedId) {
                R.id.rbYes -> {
                    setTextChip(true)
                }
                R.id.rbNo -> {
                    setTextChip(false)
                    body.socket.clear()
                }
            }
            toNext()
        }
    }

    private fun toNext() {
        when {
            body.info.content.isNotEmpty() &&
                    body.config.price > 0.0 &&
                    rbYes.isChecked &&
                    body.socket.isNotEmpty() -> {
                binding.previousNextLayout.toNextBtn.isEnabled = true
            }
            body.info.content.isNotEmpty() &&
                    body.config.price > 0.0 &&
                    rbNo.isChecked &&
                    body.socket.isEmpty() -> {
                binding.previousNextLayout.toNextBtn.isEnabled = true
            }
            else -> binding.previousNextLayout.toNextBtn.isEnabled = false
        }
    }

    private fun setBtn() {
        binding.previousNextLayout.toNextBtn.setOnClickListener {
            if ( body.config.price<=0.0 ) {
                binding.previousNextLayout.toNextBtn.isEnabled = false
                frag.context.showToast(string(R.string.Please_enter_the_price_thank_you))
            } else {
                serialEvent.onNext()
            }
        }
        binding.previousNextLayout.toPreviewBtn.setOnClickListener { dissmissDialog() }
    }

    override fun setData(data: AddLocationBody?) {
        data.notNull { body = it }
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
            else -> {
                string(R.string.Parking_Site_Setting)
            }
        }

    override fun setEvent(event: ISerialEvent) {
        serialEvent = event
    }

    override fun next(): ISerialDialog = AddLocStep2Frag().also { it.setData(body) }
    override var theme: PPYPTheme = PPYPTheme.Manager
}