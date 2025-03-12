package com.eki.parking.Controller.dialog.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.ICameraFileSet
import com.eki.parking.Controller.manager.SysCameraManager
import com.eki.parking.R
import com.eki.parking.databinding.DialogApplyCompensationBinding
import com.eki.parking.extension.screenWidth
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format
import java.io.File

/**
 * Created by Hill on 2020/12/03
 */
class ApplyCompensationDialog:DialogChildFrag<ApplyCompensationDialog>(),
                              SysCameraManager.CameraResultListener,IFragViewBinding{

    private lateinit var binding:DialogApplyCompensationBinding
    private lateinit var sysCameraManager:SysCameraManager

    private var now=DateTime.now()
    private var photo:File?=null
    var onApplyPhoto:((File,DateTime)->Unit)?=null

    override fun initFragView() {

        sysCameraManager=app.sysCamera
        sysCameraManager.onResume(activity)
        sysCameraManager.addCameraListenerFrom(this)
        binding.timeText.text="${getString(R.string.Time)} | $now"

        binding.openCamera.setText("")
        binding.openCamera.onStartCamera={
            sysCameraManager.startCamera(object : ICameraFileSet(){
                override val saveDir: File
                    get() = SysCameraManager.appCameraDir
                override val fileName: String
                    get() = now.format("yyyyMMddHHmmss")
                override val scaleX: Int
                    get() = screenWidth()
                override val scaleY: Int
                    get() = binding.openCamera.height
                override val addDateLabel: Boolean
                    get() = true
                override val labelTime: DateTime
                    get() = now
            })
        }

        binding.determinBtn.setOnClickListener {
            when(photo){
                null->showToast("請先進行拍照")
                else->{
                    onApplyPhoto?.invoke(photo!!,now)
                    dissmissDialog()
                }
            }
        }
    }

    override fun onDestroyView() {
        sysCameraManager.removeCameraListener(this)
        super.onDestroyView()
    }

    override fun onPicture(pic: File) {
        photo=pic
        binding.openCamera.showPicture(pic.toBitMap())
    }

    override fun onPictureError() {
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogApplyCompensationBinding.inflate(inflater,container,false)
        return binding
    }
}