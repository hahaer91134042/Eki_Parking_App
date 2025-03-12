package com.eki.parking.Controller.activity.frag.CarSetting

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.CarSetting.child.CurrentSelectFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IVisibleSet
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.tools.IconPhotoEvent
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.VehicleBody
import com.eki.parking.Model.response.VehicleResponse
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.databinding.FragAddCarBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.tools.InputCheck
import java.io.File

/**
 * Created by Hill on 2019/12/25
 */
class AddCarFrag: SearchFrag(),IVisibleSet,IFragViewBinding{

    override val visible: Boolean
        get() = false

    private lateinit var binding:FragAddCarBinding
    private var vehicleBody= VehicleBody(EkiApi.AddVehicle)
    private var info= RequestBody.VehicleInfo().apply {
        sqlData<EkiMember>().notNull { member ->
            isDefault = when (member.vehicle) {
                null -> true
                else -> when {
                    member.vehicle.size < 1 -> true
                    else -> false
                }
            }
        }
    }
    private var check=VehicleCheck()
    private lateinit var photoEvent:PhotoEvent

    override fun initFragView() {
        photoEvent=PhotoEvent()
        setUpCarIconView()
        setUpCarNameInput()
        setUpCarNumberInput()
        //setUpCarBrandInput()
        //setUpBrandSeries()
        setUpChargeType()
        setUpDeterminBtn()
    }

    override fun onResumeFragView() {
        toolBarTitle=getString(R.string.Car_Setting)
    }

    private fun setUpDeterminBtn() {
        binding.determinBtn.stateButton.setOnClickListener {
            vehicleBody.setInfo(info)
            EkiRequest<VehicleBody>().apply {
                body=vehicleBody
            }.sendRequest(context,true,OnAddVehicleBack())
        }
    }

    private fun setUpChargeType() {
        binding.carTypeInput.inputView.closeEdit()
        binding.carTypeInput.inputView.tailControl=object :SelectClick(){
            override fun viewAfterClick(clickView: ImageView) {
                openChargeSelectFrag()
            }
        }
        binding.carTypeInput.inputView.whenInputClick {
            openChargeSelectFrag()
        }
    }

    private fun openChargeSelectFrag() {
        showFragDialog(CurrentSelectFrag().also { frag->
            frag.onCurrentSelect={current,res->
                info.current=current.value
                binding.carTypeInput.input=getString(res)
            }
        })
    }

    private fun setUpCarNumberInput() {
        check.add(object :InputCheck.CallBack(){
            override fun check(): Boolean =info.number.isNotEmpty()
        })
        binding.carNumberInput.inputView.whenInputOnFocuse { binding.carNumberInput.showNormal() }
        binding.carNumberInput.inputView.whenInputChange {
            info.number=it
            check.start()
        }
        binding.carNumberInput.inputView.whenInputNoFocuse {
            check.start()
            if (info.number.isNotEmpty()){
                binding.carNumberInput.showNormal()
            }else{
                binding.carNumberInput.showError(getString(R.string.This_field_is_required))
            }
        }
    }

    private fun setUpCarNameInput() {
        check.add(object :InputCheck.CallBack(){
            override fun check(): Boolean =info.name.isNotEmpty()
        })
        binding.carNameInput.inputView.whenInputOnFocuse { binding.carNameInput.showNormal() }
        binding.carNameInput.inputView.whenInputChange {
            info.name=it
            check.start()
        }
        binding.carNameInput.inputView.whenInputNoFocuse {
            check.start()
            if (info.name.isNotEmpty()){
                binding.carNameInput.showNormal()
            }else{
                binding.carNameInput.showError(getString(R.string.This_field_is_required))
            }
        }
    }

    private fun setUpCarIconView() {
        binding.carIconView.icon.apply {
            setImageDrawable(context?.getDrawable(R.drawable.icon_steamlocomotive_white))
            contentDescription = string(R.string.todo)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        binding.carIconView.listener=photoEvent
    }

    private inner class PhotoEvent: IconPhotoEvent(context!!){
        override val width: Int
            get() = binding.carIconView.width
        override val height: Int
            get() = binding.carIconView.height

        override fun onPicture(pic: File) {
            vehicleBody.setImg(pic)
            binding.carIconView.icon.setImageBitmap(pic.toBitMap())
        }

        override fun onPictureError() {

        }
    }


    override fun onDestroyView() {
        app.sysCamera.removeCameraListener(photoEvent)
        super.onDestroyView()
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragAddCarBinding.inflate(inflater,container,false)
        return binding
    }

    private abstract class SelectClick:IViewControl<ImageView>(){
        override fun clickViewSet(clickView: ImageView) {
            val img=drawable(R.drawable.icon_arrow_down_orange)
            clickView.scaleType = ImageView.ScaleType.CENTER
            clickView.contentDescription = string(R.string.todo)
            clickView.setImageDrawable(img)
        }
    }

    private inner class OnAddVehicleBack:OnResponseListener<VehicleResponse>{
        override fun onReTry() {

        }

        override fun onFail(errorMsg: String,code:String) {
//            showToast(errorMsg)
        }

        override fun onTaskPostExecute(result: VehicleResponse) {
            val member= sqlData<EkiMember>()
            member.notNull {
                it.vehicle.add(result.info)
                sqlUpdate(it)
//                onCarList?.invoke()
                backFrag()
            }
        }

    }

    private inner class VehicleCheck: InputCheck() {

        override fun whenAllTrue() {
            binding.determinBtn.stateButton.isEnabled=true
        }

        override fun whenCheckFalse() {
            binding.determinBtn.stateButton.isEnabled=false
        }

    }


}