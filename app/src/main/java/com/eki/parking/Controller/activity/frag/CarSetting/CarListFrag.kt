package com.eki.parking.Controller.activity.frag.CarSetting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppConfig
import com.eki.parking.AppFlag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IVisibleSet
import com.eki.parking.Model.DTO.VehicleInfo
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.FragCarListBinding
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.sqlData
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.widget.libs.CircleImageView
import com.squareup.picasso.Picasso

/**
 * Created by Hill on 2019/12/25
 */

class CarListFrag:SearchFrag(),IVisibleSet,IFragViewBinding{

    private lateinit var binding:FragCarListBinding
//    override fun setFragViewRes(): Int = R.layout.item_recycleview

    override val visible: Boolean get() = true
    private var dataList=ArrayList<CarItemData>()

    var onAddCarClick:(()->Unit)?=null
    var onCarDetail:((VehicleInfo)->Unit)?=null
    var isNoData = true

    companion object{
        private const val Add=0
        private const val Car=1
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragCarListBinding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {

        binding.carSettingRecyclerView.recycleView.useSimpleDivider()
        binding.carSettingRecyclerView.recycleView.setBackgroundColor(getColor(R.color.color_white))

        binding.addImage.setOnClickListener { onAddCarClick?.invoke() }
        binding.addCarFrame.setOnClickListener { onAddCarClick?.invoke() }

        setNoData()
    }

    override fun onResumeFragView() {
        toolBarTitle=getString(R.string.Car_Setting)

        sqlData<EkiMember>()?.vehicle.notNull { vehicleList ->
            dataList.clear()

            isNoData = vehicleList.isEmpty()
            vehicleList.forEach { info ->
                dataList.add(CarItemData(Car, info))
            }

            if (vehicleList.size < AppConfig.maxCarSetNumber)
                dataList.add(CarItemData(Add).apply {
                    remain = AppConfig.maxCarSetNumber - vehicleList.size
                })

            binding.carSettingRecyclerView.recycleView.adapter = CarAdaptor(context).apply {
                setItemListClickListener {
                    when (dataList[it].viewType) {
                        Car -> {
                            dataList[it].data.notNull { data -> onCarDetail?.invoke(data) }
                        }
                        Add -> {
                            onAddCarClick?.invoke()
                        }
                    }
                }
            }
        }

        setNoData()
    }

    private fun setNoData() {
        if (isNoData) {
            binding.addCarFrame.visibility = View.VISIBLE
            binding.addImage.visibility = View.VISIBLE
            binding.carSettingRecyclerView.recycleView.visibility = View.GONE
        } else {
            binding.addCarFrame.visibility = View.GONE
            binding.addImage.visibility = View.GONE
            binding.carSettingRecyclerView.recycleView.visibility = View.VISIBLE
        }
    }

    private inner class CarAdaptor(context: Context?) : BaseAdapter<ItemLayout<CarItemData>>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemLayout<CarItemData> =
                when(viewType){
                    Add->AddItem(getItemView(R.layout.item_add_btn,parent)).also { it.init(dpToPx(65f)) }
                    else->CarItem(getItemView(R.layout.item_vehicle_info,parent)).also { it.init(dpToPx(65f)) }
                }

        override fun onBindViewHolder(item: ItemLayout<CarItemData>, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(dataList[position])
        }
        override fun getItemCount(): Int =dataList.size

        override fun getItemViewType(position: Int): Int {
            return dataList[position].viewType
        }

    }

    private class CarItem(itemView: View) : ItemLayout<CarItemData>(itemView) {
        private var carImg:CircleImageView=itemView.findViewById(R.id.carImgView)
        private var name:TextView=itemView.findViewById(R.id.carNameText)
        private var number:TextView=itemView.findViewById(R.id.carNumberText)
        private var defaultView:TextView=itemView.findViewById(R.id.isDefaultText)
        private var parentView:View=itemView.findViewById(R.id.parentView)

        override fun init(lenght: Int) {
            super.init(lenght)
            parentView.setOnClickListener {
                itemClick()
            }
            parentView.setOnLongClickListener {
                context.sendBroadcast(Intent(AppFlag.CarDelete))
                return@setOnLongClickListener true
            }
        }

        override fun refresh(data: CarItemData?) {
            super.refresh(data)

            data.notNull {
                if (it.data?.Img?.isEmpty()==true){
                    Picasso.with(context)
                            .load(R.drawable.none_img)
                            .into(carImg)
                }else
                    Picasso.with(context)
                            .load(it.data?.Img)
                            .placeholder(R.drawable.none_img)
                            .resize(dpToPx(150f), dpToPx(150f))
                            .centerCrop()
                            .into(carImg)

//                Log.d("carImg width->${dpToPx(50f)} height->${dpToPx(50f)}")

                name.text=it.data?.Name
                number.text=it.data?.Number

                if (it.data?.IsDefault == true)
                    defaultView.text=getString(R.string.Default)
                else
                    defaultView.visibility=View.GONE
            }
        }
    }

    private class AddItem(itemView: View) : ItemLayout<CarItemData>(itemView) {
        private var parentView:View=itemView.findViewById(R.id.parentView)
        private var addBtn:TextView=itemView.findViewById(R.id.addBtn)
        override fun init(lenght: Int) {
            super.init(lenght)
            parentView.setOnClickListener { itemClick() }
        }

        override fun refresh(data: CarItemData?) {
            super.refresh(data)
            data.notNull {
                addBtn.text=getString(R.string.Can_still_add_car).messageFormat(it.remain)
            }
        }
    }

    private data class CarItemData(val viewType:Int,var data:VehicleInfo?=null){
        var remain=0
    }
}