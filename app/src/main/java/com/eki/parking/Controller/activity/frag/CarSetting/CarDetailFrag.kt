package com.eki.parking.Controller.activity.frag.CarSetting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.CarSetting.child.CurrentSelectFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IVisibleSet
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.tools.IconPhotoEvent
import com.eki.parking.Model.DTO.VehicleInfo
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.VehicleBody
import com.eki.parking.Model.response.VehicleResponse
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.databinding.FragCarDetailBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.getDrawable
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.tools.InputCheck
import java.io.File

/**
 * Created by Hill on 2020/01/08
 */
class CarDetailFrag : SearchFrag(),
    ISetData<VehicleInfo>,
    IVisibleSet, IFragViewBinding {

    override val visible: Boolean
        get() = false

    private lateinit var binding: FragCarDetailBinding
    private var vehicleBody = VehicleBody(EkiApi.UpdateVehicle)
    private lateinit var info: VehicleInfo
    private var check = VehicleCheck()
    private lateinit var photoEvent: PhotoEvent

    override fun initFragView() {
        DefaultCarSet(view?.findViewById(R.id.carDefaultSetText)!!)
        photoEvent = PhotoEvent()

        setUpCarIconView()
        setUpCarNameInput()
        setUpCarNumberInput()
        setUpChargeType()
        setUpDeterminBtn()
    }


    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.Car_Setting)
    }

    override fun onDestroyView() {
        app.sysCamera.removeCameraListener(photoEvent)
        super.onDestroyView()
    }

    private fun setUpDeterminBtn() {
        binding.determinBtn.stateButton.isEnabled = true
        binding.determinBtn.stateButton.setOnClickListener {
            vehicleBody.setInfo(info.toData())
            EkiRequest<VehicleBody>().apply {
                body = vehicleBody
            }.sendRequest(context, true, UpdateVehicleBack())
        }
    }

    private fun setUpChargeType() {
        binding.carTypeInput.input = when (info.current) {
            CurrentEnum.AC -> getString(R.string.Electric_Car_AC)
            CurrentEnum.DC -> getString(R.string.Electric_Car_DC)
            else -> getString(R.string.General)
        }
        binding.carTypeInput.inputView.closeEdit()
        binding.carTypeInput.inputView.tailControl = object : SelectClick() {
            override fun viewAfterClick(clickView: ImageView) {
                openChargeSelectFrag()
            }
        }
        binding.carTypeInput.inputView.whenInputClick {
            openChargeSelectFrag()
        }
    }

    private fun openChargeSelectFrag() {
        showFragDialog(CurrentSelectFrag().also { frag ->
            frag.onCurrentSelect = { current, res ->
                info.Current = current.value
                binding.carTypeInput.input = getString(res)
            }
        })
    }

    private fun setUpCarNumberInput() {
        binding.carNumberInput.input = info.Number
        check.add(object : InputCheck.CallBack() {
            override fun check(): Boolean = info.Number.isNotEmpty()
        })
        binding.carNumberInput.inputView.whenInputOnFocuse { binding.carNumberInput.showNormal() }
        binding.carNumberInput.inputView.whenInputChange {
            info.Number = it
            check.start()
        }
        binding.carNumberInput.inputView.whenInputNoFocuse {
            check.start()
            if (info.Number.isNotEmpty()) {
                binding.carNumberInput.showNormal()
            } else {
                binding.carNumberInput.showError(getString(R.string.This_field_is_required))
            }
        }
    }

    private fun setUpCarNameInput() {
        binding.carNameInput.input = info.Name
        check.add(object : InputCheck.CallBack() {
            override fun check(): Boolean = info.Number.isNotEmpty()
        })
        binding.carNameInput.inputView.whenInputOnFocuse { binding.carNameInput.showNormal() }
        binding.carNameInput.inputView.whenInputChange {
            info.Name = it
            check.start()
        }
        binding.carNameInput.inputView.whenInputNoFocuse {
            check.start()
            if (info.Name.isNotEmpty()) {
                binding.carNameInput.showNormal()
            } else {
                binding.carNameInput.showError(getString(R.string.This_field_is_required))
            }
        }
    }

    private inner class PhotoEvent : IconPhotoEvent(context!!) {
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

    private fun setUpCarIconView() {

        binding.carIconView.run {
            loadUrl(info.Img)
            icon.setImageDrawable(context?.getDrawable(R.drawable.icon_steamlocomotive_white))
            icon.contentDescription = string(R.string.todo)
            icon.scaleType = ImageView.ScaleType.CENTER_CROP
            listener = photoEvent
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragCarDetailBinding.inflate(inflater, container, false)
        return binding
    }

    override fun setData(data: VehicleInfo?) {
        data.notNull {
            info = it
        }
    }

    private inner class UpdateVehicleBack : OnResponseListener<VehicleResponse> {
        override fun onReTry() {

        }

        override fun onFail(errorMsg: String, code: String) {

        }

        override fun onTaskPostExecute(result: VehicleResponse) {
            val member = sqlData<EkiMember>()
            member?.vehicle?.first { it.Id == result.info.Id }
                ?.update(result.info)
            if (result.info.IsDefault) {
                member?.vehicle?.filter { (it.Id != result.info.Id) && it.IsDefault }
                    ?.forEach {
                        it.IsDefault = false
                    }
            }
            sqlUpdate(member)
            backFrag()
        }
    }

    private abstract class SelectClick : IViewControl<ImageView>() {
        override fun clickViewSet(clickView: ImageView) {
            val img = drawable(R.drawable.icon_arrow_down_orange)
            clickView.scaleType = ImageView.ScaleType.CENTER
            clickView.contentDescription = string(R.string.todo)
            clickView.setImageDrawable(img)
        }
    }

    private inner class DefaultCarSet(val view: TextView) : IViewControl<TextView>() {
        private var isDefault
            get() = info.IsDefault
            set(value) {
                info.IsDefault = value
            }

        private var isClick = false

        init {
            view.textSize = 14f
            view.text = getString(R.string.Set_as_default_car)
            view.setPadding(dpToPx(10f))
            clickViewSet(view)
        }

        override fun clickViewSet(clickView: TextView) {
            if (isDefault) {
                clickView.visibility = View.GONE
            } else {
                clickView.setOnClickListener { viewAfterClick(view) }
                unDefaultStyle()
            }
        }

        override fun viewAfterClick(clickView: TextView) {
            isClick = !isClick
            isDefault = isClick
//            Log.d("Car IsDefault->$isDefault")

            if (isClick) {
                defaultStyle()
            } else {
                unDefaultStyle()
            }
        }

        private fun defaultStyle() {
            view.setTextColor(getColor(R.color.Eki_orange_4))
            view.background = getDrawable(R.drawable.shape_round_stroke_orange4_2dp)
        }

        private fun unDefaultStyle() {
            view.setTextColor(getColor(R.color.dark_gray1))
            view.setBackgroundColor(getColor(R.color.light_gray6))
        }
    }

    private inner class VehicleCheck : InputCheck() {

        override fun whenAllTrue() {
            binding.determinBtn.stateButton.isEnabled = true
        }

        override fun whenCheckFalse() {
            binding.determinBtn.stateButton.isEnabled = false
        }
    }
}