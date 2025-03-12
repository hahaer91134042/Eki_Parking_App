package com.eki.parking.Controller.activity.frag.SiteDetail.child

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Model.DTO.LocationSocket
import com.eki.parking.Model.DTO.SocketSelect
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.EnumClass.SiteSize
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.widget.SiteSocketSelectView
import com.eki.parking.databinding.FragSiteBaseInfoBinding
import com.eki.parking.extension.string
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/07/10
 */
class SiteBaseInfoFrag : BaseFragment<SiteBaseInfoFrag>(),
    ISetData<ManagerLocation>, IFragViewBinding {

    private lateinit var binding: FragSiteBaseInfoBinding
    private lateinit var loc: ManagerLocation
    private var selectSocketList = ArrayList<SocketSelect>()

    var onEdit: (ManagerLocation) -> Unit = {
        it.Info?.Content = binding.itemParkingSpaceName.siteName.text.toString().cleanTex

        var price = binding.itemChargePrice.locPrice.text.toString().cleanTex
        it.Config?.Price = when {
            price.isNullOrEmpty() -> 0.0
            else -> price.toDouble()
        }

        it.Socket.clear()
        it.Socket.addAll(selectSocketList.map {
            LocationSocket().apply {
                currentEnum = it.current
                chargeSocket = it.socket
            }
        })
    }

    override fun initFragView() {
        selectSocketList.addAll(loc.Socket.map { SocketSelect(it.currentEnum, it.chargeSocket) })

        binding.itemParkingSpaceName.siteName.setText(loc.Info?.Content ?: "")
        binding.itemChargePrice.locPrice.setText(loc.Config?.Price.toString())

        SiteTypeCtrl().setLocType()

        binding.radioGroup.check(
            when (loc.current) {
                CurrentEnum.NONE -> R.id.noneBtn
                else -> R.id.yesBtn
            }
        )
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.socketSelect.cleanSelect()
            selectSocketList.clear()
            binding.socketSelect.isEnabled = checkedId == R.id.yesBtn
        }

        binding.chargeSocketTitle.text = when (loc.Info?.siteSize) {
            SiteSize.Motor -> string(R.string.Locomotive_Charging)
            else -> string(R.string.Car_Charging)
        }

        binding.socketSelect.creatFram(loc)
        binding.socketSelect.socketSelectEvent = object : SiteSocketSelectView.OnSocketSelectEvent {
            override fun onSocketSelect(sockets: List<SocketSelect>) {
                selectSocketList.clear()
                selectSocketList.addAll(sockets)
            }
        }
    }

    override fun setData(data: ManagerLocation?) {
        data.notNull { loc = it }
    }

    private inner class SiteTypeCtrl {
        var btnList = listOf(
            Pair(listOf(SiteSize.Motor), binding.itemParkingSpaceType.locomotiveBtn),
            Pair(listOf(SiteSize.Small, SiteSize.Standar), binding.itemParkingSpaceType.carBTn)
        )

        fun setLocType() {
            btnList.firstOrNull { it.first.any { s -> s == loc.Info?.siteSize } }?.second?.visibility =
                View.VISIBLE
            btnList.filterNot { it.first.any { s -> s == loc.Info?.siteSize } }
                .forEach {
                    it.second.visibility = View.GONE
                }
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragSiteBaseInfoBinding.inflate(inflater, container, false)
        return binding
    }

}