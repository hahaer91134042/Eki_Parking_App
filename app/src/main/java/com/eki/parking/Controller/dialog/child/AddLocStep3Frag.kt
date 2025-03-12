package com.eki.parking.Controller.dialog.child

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.builder.AddLocSerialBuilder
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IAppTheme
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialEvent
import com.eki.parking.Model.EnumClass.PPYPTheme
import com.eki.parking.Model.EnumClass.SiteSize
import com.eki.parking.Model.EnumClass.SiteType
import com.eki.parking.Model.request.body.AddLocationBody
import com.eki.parking.R
import com.eki.parking.databinding.DialogAddlocStep3Binding
import com.eki.parking.extension.string
import com.eki.parking.utils.Logger
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/05/07
 */

class AddLocStep3Frag:DialogChildFrag<AddLocStep3Frag>(),
                      ISetData<AddLocationBody>,
                      ISerialDialog, IAppTheme,IFragViewBinding {

    private lateinit var binding: DialogAddlocStep3Binding
    private lateinit var body:AddLocationBody
    private lateinit var serialEvent:ISerialEvent

    override fun initFragView() {
        initView()
        setProgress()
        setNotice()
        setBtn()
    }

    private fun initView(){
        if(body.info.size == AddLocSerialBuilder.scooter){

            binding.itemParkingSpace.sizeSelector.visibility = View.GONE
            binding.itemParkingSpace.suggest.visibility = View.VISIBLE

            binding.itemParkingType.llParkingType.visibility = View.GONE
            binding.itemHighParkingSpace.llHighParkingSpace.visibility = View.GONE
            binding.itemWeightLimit.llWeightLimit.visibility = View.GONE

            binding.itemParkingSpace.tvParkingSpace3.text = context?.getString(R.string.Locomotive_parking_size)

            body.info.size = SiteSize.Motor.value

            toNext3()

        }else if(body.info.size == AddLocSerialBuilder.car){

            binding.itemParkingSpace.sizeSelector.visibility = View.VISIBLE
            binding.itemParkingSpace.suggest.visibility = View.GONE

            binding.itemParkingSpace.tvParkingSpace3.text = context?.getString(R.string.Car_parking_space_size)

            binding.itemAddlocStopBtn.toNextBtn.isEnabled = true

            setRadioGroup()
        }

    }

    private fun toNext3() {
        Logger.w("toNext3 body = ${body.info.size}")

        // car = 0 ; locomotive = 2
        val spaceType = body.info.size
        if (spaceType == 0) {
            binding.itemAddlocStopBtn.toNextBtn.isEnabled = (body.info.height > 0.0 && body.info.weight > 0.0) ||
                    (body.info.height > 0.0 && body.info.weight == -1.0) ||
                    (body.info.height == -1.0 && body.info.weight > 0.0) ||
                    (body.info.height == -1.0 && body.info.weight == -1.0)
        } else {
            binding.itemAddlocStopBtn.toNextBtn.isEnabled = true
        }
    }

    private fun setProgress(){
        view?.let {
            val displayMetrics = it.resources.displayMetrics
            val height = (displayMetrics.heightPixels * 0.009).toInt()
            val width = displayMetrics.widthPixels
            val progress: View? = it.findViewById(R.id.progress_bar)
            progress?.setBackgroundResource(R.drawable.shape_round_progress_orange4_20dp)
            val params = LinearLayout.LayoutParams((width * 0.75).toInt(), height)
            progress?.layoutParams = params
        }
    }

    fun setRadioGroup(){
        binding.itemParkingType.typeSelector.setOnCheckedChangeListener { _, _ ->
            body.info.type = when(binding.itemParkingType.typeSelector.checkedRadioButtonId){
                R.id.flatSite->{ SiteType.Flat.value }
                else-> { SiteType.Mechanical.value }
            }
        }
        binding.itemParkingSpace.sizeSelector.setOnCheckedChangeListener { _, _ ->
            body.info.size = when(binding.itemParkingSpace.sizeSelector.checkedRadioButtonId){
                R.id.small->{ SiteSize.Small.value }
                else-> { SiteSize.Standar.value }
            }
        }

        setHeightSelector()
        setWeightSelector()
    }

    private fun setHeightSelector() {
        binding.itemHighParkingSpace.heightSelector.check(R.id.nonHeight)

        binding.itemHighParkingSpace.heightSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.limitHeight -> {
                    var newValue = 0.0

                    binding.itemHighParkingSpace.heightInput.isEnabled = true
                    binding.itemHighParkingSpace.heightInput.isFocusable = true
                    binding.itemHighParkingSpace.heightInput
                        .addTextChangedListener(object : TextChangeWatch() {

                        override fun inputValueChange(value: Double) {
                            body.info.height = value
                            newValue = value
                            toNext3()
                        }
                    })
                    body.info.height = newValue
                }
                R.id.nonHeight -> {
                    binding.itemHighParkingSpace.heightInput.isEnabled = false
                    binding.itemHighParkingSpace.heightInput.setText(R.string.Number_zero)
                    body.info.height = -1.0
                }
            }
            toNext3()
        }
    }
    private fun setWeightSelector() {
        binding.itemWeightLimit.weightSelector.check(R.id.nonWeight)
        binding.itemWeightLimit.weightSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.limitWeight -> {
                    var newValue = 0.0

                    binding.itemWeightLimit.weightInput.isEnabled = true
                    binding.itemWeightLimit.weightInput.isFocusable = true
                    binding.itemWeightLimit.weightInput.addTextChangedListener(object : TextChangeWatch() {

                        override fun inputValueChange(value: Double) {
                            body.info.weight = value
                            newValue = value
                            toNext3()
                        }
                    })
                    body.info.weight = newValue

                }
                R.id.nonWeight -> {
                    binding.itemWeightLimit.weightInput.isEnabled = false
                    binding.itemWeightLimit.weightInput.setText(R.string.Number_zero)
                    body.info.weight = -1.0
                }
            }

            toNext3()

        }
    }

    private abstract class TextChangeWatch : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            inputValueChange(runCatching { s.toString().cleanTex.toDouble() }.getOrDefault(0.0))
        }

        abstract fun inputValueChange(value: Double)
    }

    private fun setNotice(){
        binding.itemOtherNotice.remarkText.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                body.config.text=s?.toString()?:""
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setBtn(){
        binding.itemAddlocStopBtn.toPreviewBtn.setOnClickListener {
            serialEvent.onPrevious()
        }

        binding.itemAddlocStopBtn.toNextBtn.setOnClickListener {
            serialEvent.onNext()
        }
    }

    override fun setData(data: AddLocationBody?) { data.notNull { body=it } }

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

    override fun next(): ISerialDialog =AddLocStep4Frag().also { it.setData(body) }
    override var theme: PPYPTheme = PPYPTheme.Manager
    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogAddlocStep3Binding.inflate(inflater,container,false)
        return binding
    }

}