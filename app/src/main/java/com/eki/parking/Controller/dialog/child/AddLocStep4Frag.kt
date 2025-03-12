package com.eki.parking.Controller.dialog.child

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.dialog.SelectPhotoTypeDialog
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.*
import com.eki.parking.Controller.manager.SysCameraManager
import com.eki.parking.Controller.process.AddLocationProcess
import com.eki.parking.Controller.process.SetLocationImgProcess
import com.eki.parking.Model.DTO.LocationImg
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.PPYPTheme
import com.eki.parking.Model.request.body.AddLocationBody
import com.eki.parking.Model.request.body.SetLocationImgBody
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.databinding.DialogAddlocStep4Binding
import com.eki.parking.extension.screenWidth
import com.eki.parking.extension.sqlSaveOrUpdate
import com.eki.parking.extension.string
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.showToast
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import java.io.File

/**
 * Created by Hill on 2020/04/16
 */

class AddLocStep4Frag : DialogChildFrag<AddLocStep4Frag>(),
    ISerialDialog,
    ISerialFinish,
    ISetData<AddLocationBody>,
    SysCameraManager.CameraResultListener, IAppTheme, IFragViewBinding {

    private lateinit var binding: DialogAddlocStep4Binding
    private enum class ImgClick {
        Img1,
        Img2,
        Img3
    }

    private data class TempSelectImg(var sort: Int) {
        var pic: File? = null
    }

    private lateinit var info: AddLocationBody
    private lateinit var serialEvent: ISerialEvent
    private var sysCamera: SysCameraManager? = null
    private var array = arrayOf<ImageView>()

    private var imgClick = ImgClick.Img1

    private var bodyMap = LinkedHashMap<ImgClick, TempSelectImg>().apply {
        put(ImgClick.Img1, TempSelectImg(1))
        put(ImgClick.Img2, TempSelectImg(2))
        put(ImgClick.Img3, TempSelectImg(3))
    }

    override fun initFragView() {
        setProgress()
        showToast(getResString(R.string.Please_upload_three_photos_with_different_angles_thank_you))

        array = arrayOf(binding.locImg1, binding.locImg2, binding.locImg3)

        sysCamera = app.sysCamera
        sysCamera?.addCameraListenerFrom(this)

        for (i in array.indices) {
            array[i].setOnClickListener {
                startPhoto(ImgClick.values()[i])
            }
        }
        binding.itemAddlocStopBtn.toPreviewBtn.setOnClickListener { serialEvent.onPrevious() }

        binding.itemAddlocStopBtn.toNextBtn.setOnClickListener {

            val addLocProcess = AddLocProcess()
            addLocProcess.run()
        }
    }

    private fun startPhoto(click: ImgClick) {
        imgClick = if (bodyMap.any { it.value.pic == null }) {
            //圖片未選滿的情況
            bodyMap.filter { it.value.pic == null }.toList().first().first
        } else {
            //圖片已經選滿
            click
        }
        showPhotoDialog()
    }

    private fun showPhotoDialog() {
        SelectPhotoTypeDialog().also { dialog ->
            dialog.onSelectAlbum = {
                sysCamera?.startPhoto(object : SysCameraManager.DefaultPhotoSet() {
                    override val fileName: String
                        get() = "user_${DateTime().toStamp()}"
                    override val scaleX: Int
                        get() = screenWidth()
                    override val scaleY: Int
                        get() = binding.clickView.height
                })
            }
            dialog.onSelectCamera = {
//                Log.w("select camera")
                sysCamera?.startCamera(object : ICameraFileSet() {
                    override val fileName: String
                        get() = "user_${DateTime().toStamp()}"
                    override val scaleX: Int
                        get() = screenWidth()
                    override val scaleY: Int
                        get() = binding.clickView.height
                })
            }
        }.show(childFragmentManager, "SelectPhotoType")
    }

    override fun onDestroyView() {
        sysCamera?.removeCameraListener(this)
        sysCamera = null
        super.onDestroyView()
    }

    override val frag: DialogChildFrag<*>
        get() = this

    override val title: String
        get() = string(R.string.Parking_Site_Setting)

    override fun next(): ISerialDialog? = null

    override fun setEvent(event: ISerialEvent) {
        serialEvent = event
    }

    override fun setData(data: AddLocationBody?) {
        data.notNull { info = it }
    }

    override fun onPicture(pic: File) {
        bodyMap[imgClick]?.pic = pic

        when (imgClick) {
            ImgClick.Img1 -> {
                binding.locImg1.setImageBitmap(pic.toBitMap())
            }
            ImgClick.Img2 -> {
                binding.locImg2.setImageBitmap(pic.toBitMap())
            }
            ImgClick.Img3 -> {
                binding.locImg3.setImageBitmap(pic.toBitMap())
                checkToFinish()
            }
        }

        //checkToFinish()
    }

    private fun checkToFinish() {
        binding.itemAddlocStopBtn.toNextBtn.isEnabled = bodyMap.any { it.value.pic != null }
    }

    override fun onPictureError() {}

    override var onSerialFinish: (() -> Unit)? = null

    private lateinit var mLoc: ManagerLocation

    private inner class AddLocProcess : AddLocationProcess(context) {

        override val body: AddLocationBody
            get() = info

        override fun run() {

            context.showToast(getResString(R.string.Start_joining_location))

            onAddSuccess = {
                mLoc = it
                startUploadImg(ImgClick.Img1, onImg1Add)
            }
            onFail = {
                showToast(string(R.string.Failed_to_join_location))
            }
            context?.sendBroadcast(Intent(AppFlag.ParkingSite))
            super.run()
        }

    }

    private fun startUploadImg(imgNum: ImgClick, back: ((List<LocationImg>) -> Unit)? = null) {

        bodyMap[imgNum].notNull { temp ->
            temp.pic.notNull { pic ->
                AddLocImgProcess(SetLocationImgBody().apply {
                    setImg(pic)
                    setInfo(RequestBody.SetLocImg().apply {
                        serNum = mLoc.Info?.SerialNumber ?: ""
                        sort = temp.sort
                    })
                }).apply {
                    back.notNull {
                        onSuccess = it
                    }
                }.run()
            }
        }

    }

    private val onImg1Add: (List<LocationImg>) -> Unit = { list ->
        if (bodyMap[ImgClick.Img2]?.pic != null) {
            startUploadImg(ImgClick.Img2, onImg2Add)
        } else {
            uploadImgFinish(list)
        }
    }

    private val onImg2Add: (List<LocationImg>) -> Unit = { list ->
        if (bodyMap[ImgClick.Img3]?.pic != null) {
            startUploadImg(ImgClick.Img3, onImg3Add)
        } else {
            uploadImgFinish(list)
        }
    }
    private val onImg3Add: (List<LocationImg>) -> Unit = { list ->
        uploadImgFinish(list)
    }

    private fun uploadImgFinish(list: List<LocationImg>) {
        mLoc.also {
            it.Img.clear()
            it.Img.addAll(list)
        }.sqlSaveOrUpdate()

        closeProgress()

        EkiMsgDialog(object : EkiMsgDialog.Style {
            override val btnSet: EkiMsgDialog.BtnSet
                get() = EkiMsgDialog.BtnSet.Single
            override val contentSet: EkiMsgDialog.ContentSet
                get() = EkiMsgDialog.ContentSet.Set1
        }).also {
            it.msg = string(R.string.Successfully_joined)
            it.determinClick = {
                onSerialFinish?.invoke()
                dissmissDialog()
            }
        }//.show(childFragmentManager) 改頁面排序
    }

    private fun setProgress() {
        view?.let {
            val displayMetrics = it.resources.displayMetrics
            val height = (displayMetrics.heightPixels * 0.009).toInt()
            val width = displayMetrics.widthPixels
            val progress: View? = it.findViewById(R.id.progress_bar)
            progress?.setBackgroundResource(R.color.Eki_orange_4)
            val params = LinearLayout.LayoutParams((width * 1.0).toInt(), height)
            progress?.layoutParams = params
        }
    }

    private inner class AddLocImgProcess(body: SetLocationImgBody) :
        SetLocationImgProcess(body, context) {
        override fun run() {
            super.run()
        }
    }

    override var theme: PPYPTheme = PPYPTheme.Manager
    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogAddlocStep4Binding.inflate(inflater,container,false)
        return binding
    }

}