package com.eki.parking.Controller.activity.frag.CarSetting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IVisibleSet
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.VehicleInfo
import com.eki.parking.Model.request.DeleteVehicleRequest
import com.eki.parking.Model.response.VehicleListResponse
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.FragCarSetDeleteBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.tools.Log
import com.hill.devlibs.widget.libs.CircleImageView
import com.squareup.picasso.Picasso

/**
 * Created by Hill on 2019/12/25
 */
class CarDeleteFrag:SearchFrag(),IVisibleSet,IFragViewBinding{

    override val visible: Boolean
        get() = false
    private lateinit var binding:FragCarSetDeleteBinding
    private var dataList=ArrayList<VehicleInfo>()
    private var itemList=ArrayList<CarItem>()

    private val onItemSelect={
        binding.determinBtn.isEnabled = itemList.any { it.tailControl.isSelect }
    }

    override fun initFragView() {
        binding.recycleView.useSimpleDivider()

        binding.cancelBtn.setOnClickListener {
            itemList.filter { it.tailControl.isSelect }
                    .forEach { it.tailControl.unSelect() }
        }
        binding.determinBtn.setOnClickListener {
            val list=itemList.filter { it.tailControl.isSelect }
                    .map { it.itemData }.toArrayList()

            EkiMsgDialog().also {
                it.msg=string(R.string.Are_you_sure_you_want_to_delete_cart_information)
                        .messageFormat(list.size)
                it.determinClick={ sendDeleteVehicle(list) }
            }.show(childFragmentManager)
        }
    }

    private fun sendDeleteVehicle(list: ArrayList<VehicleInfo>) {
        Log.w("Delete list size->${list.size}")

        DeleteVehicleRequest().apply {
            body.id.addAll(list.map { it.Id })
        }.sendRequest(context,true,object:OnResponseListener<VehicleListResponse>{
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String,code:String) {

            }

            override fun onTaskPostExecute(result: VehicleListResponse) {
                sqlData<EkiMember>().notNull { member->

//                    member.vehicle.removeAll { info->
//                        result.info?..any { it.Id==info.Id&&it.Success}
//                    }
                    member.vehicle.clear()
                    member.vehicle.addAll(result.info)

                    sqlUpdate(member)
                    showToast(getString(R.string.Parking_space_deleted))
                    //要退回去
                    backFrag()
                }
            }
        })
    }

    override fun onResumeFragView() {
        toolBarTitle=getString(R.string.Car_Setting)

        sqlData<EkiMember>()?.vehicle.notNull { vehicleList->
            dataList.clear()
            dataList.addAll(vehicleList)

            binding.recycleView.adapter=CarAdaptor(context)
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragCarSetDeleteBinding.inflate(inflater,container,false)
        return binding
    }

    private inner class CarAdaptor(context: Context?) : BaseAdapter<CarItem>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItem {
            val item=CarItem(getItemView(R.layout.item_vehicle_info,parent))
                    .also { it.init(dpToPx(65f)) }

            itemList.add(item)
            return item
        }

        override fun onBindViewHolder(item: CarItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(dataList[position])
        }
        override fun getItemCount(): Int =dataList.size

//        override fun getItemViewType(position: Int): Int {
//            return dataList[position].viewType
//        }

    }

    private inner class CarItem(itemView: View) : ItemLayout<VehicleInfo>(itemView) {
        private var carImg:CircleImageView=itemView.findViewById(R.id.carImgView)
        private var name:TextView=itemView.findViewById(R.id.carNameText)
        private var number:TextView=itemView.findViewById(R.id.carNumberText)
        private var defaultView:TextView=itemView.findViewById(R.id.isDefaultText)
        private var parentView:View=itemView.findViewById(R.id.parentView)
        private var tailIcon:ImageView=itemView.findViewById(R.id.tailIcon)
        var tailControl=DeleteControl()

        override fun init(lenght: Int) {
            super.init(lenght)
            parentView.setOnClickListener {
                tailControl.viewAfterClick(tailIcon)
                itemClick()
            }
            defaultView.visibility=View.GONE
            tailControl.clickViewSet(tailIcon)
            tailIcon.setOnClickListener { tailControl.viewAfterClick(tailIcon) }
        }

        override fun refresh(data: VehicleInfo?) {
            super.refresh(data)

            data.notNull {
                if (it.Img.isEmpty()){
                    Picasso.with(context)
                            .load(R.drawable.icon_set_no_car)
                            .into(carImg)
                }else
                    Picasso.with(context)
                            .load(it.Img)
                            .placeholder(R.drawable.icon_set_no_car)
                            .resize(dpToPx(150f), dpToPx(150f))
                            .centerCrop()
                            .into(carImg)

//                Log.d("carImg width->${dpToPx(50f)} height->${dpToPx(50f)}")

                name.text=it.Name
                number.text=it.Number
            }
        }

    }

    private inner class DeleteControl:IViewControl<ImageView>(){
        var isSelect=false
        private lateinit var view:ImageView
        override fun clickViewSet(clickView: ImageView) {
            view=clickView
            clickView.setImageResource(R.drawable.icon_checkbox_false_orange)
        }

        override fun viewAfterClick(clickView: ImageView) {
            isSelect=!isSelect
            onItemSelect.invoke()
            if (isSelect){
                clickView.setImageResource(R.drawable.icon_checkbox_true_orange)
            }else{
                clickView.setImageResource(R.drawable.icon_checkbox_false_orange)
            }
        }
        fun unSelect(){
            isSelect=false
            view.setImageResource(R.drawable.icon_checkbox_false_orange)
        }
    }




}