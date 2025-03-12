package com.eki.parking.Controller.activity.frag.ParkingSite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.builder.AddLocSerialBuilder
import com.eki.parking.Controller.dialog.SimulatePageDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.R
import com.eki.parking.databinding.FragParkingSteamlocomotiveBinding
import com.hill.devlibs.impl.IFragViewBinding

class AddParkingSpaceFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding:FragParkingSteamlocomotiveBinding
    var size = 0

    companion object {
        const val car = AddLocSerialBuilder.car
        const val locomotive = AddLocSerialBuilder.scooter
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragParkingSteamlocomotiveBinding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {
        super.initFragView()
        toolBarTitle = getString(R.string.Add_parking_spaces)
        initItem()
    }
    private fun initItem() {

        binding.llCar.setOnClickListener {
            changeDialog(car)
        }
        binding.llLocomotive.setOnClickListener{
            changeDialog(locomotive)
        }
    }
    private fun changeDialog(Transportation:Int) {

        SimulatePageDialog(AddLocSerialBuilder().apply {
            body.info.size = Transportation
            type = Transportation
            onFinish = {
                //context.sendBroadcast(Intent(AppFlag.UpdateView))
            }
        }).show(this.childFragmentManager, "SimulateDialog")

    }
}