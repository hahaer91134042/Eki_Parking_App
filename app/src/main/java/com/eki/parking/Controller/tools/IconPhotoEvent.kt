package com.eki.parking.Controller.tools

import android.content.Context
import com.eki.parking.App
import com.eki.parking.Controller.impl.ICameraFileSet
import com.eki.parking.Controller.listener.OnPhotoEventListener
import com.eki.parking.Controller.manager.SysCameraManager
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/01/08
 */
abstract class IconPhotoEvent(var context: Context):OnPhotoEventListener,
                                                    SysCameraManager.CameraResultListener{
    private var sysCamera= App.getInstance().sysCamera
    init {
        sysCamera.addCameraListenerFrom(this)
    }

    abstract val width:Int
    abstract val height:Int
    open val namePrefix="car"

    override fun onCameraEvent() {
//        Log.d("CarIconPhotoEvent w->$width h->$height")
        sysCamera.startCamera(object : ICameraFileSet(){
            override val fileName: String
                get() = "${namePrefix}_${DateTime().toStamp()}"
            override val scaleX: Int
                get() = width
            override val scaleY: Int
                get() = height
        })
    }

    override fun onPhotoEvent() {
        sysCamera.startPhoto("${namePrefix}_${DateTime().toStamp()}")
    }
}