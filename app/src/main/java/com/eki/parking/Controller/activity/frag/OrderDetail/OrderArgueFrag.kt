package com.eki.parking.Controller.activity.frag.OrderDetail

import android.location.Location
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.OrderDetail.child.ArgueTypeSelectFrag
import com.eki.parking.Controller.dialog.SelectPhotoTypeDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.ICameraFileSet
import com.eki.parking.Controller.listener.GpsListener
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.manager.SysCameraManager
import com.eki.parking.Controller.tools.GPS
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.ArgueSource
import com.eki.parking.Model.EnumClass.ArgueType
import com.eki.parking.Model.EnumClass.PPYPTheme
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.ArgueBody
import com.eki.parking.Model.response.ArgueResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.View.viewControl.GreenArrowDownClick
import com.eki.parking.View.viewControl.OrangeArrowDownClick
import com.eki.parking.databinding.FragOrderArgueBinding
import com.eki.parking.extension.screenWidth
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.show
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import java.io.File

/**
 * Created by Hill on 2020/05/21
 */
class OrderArgueFrag : SearchFrag(),
    ISetData<EkiOrder>,
    GpsListener,
    SysCameraManager.CameraResultListener, IFragViewBinding {

    private lateinit var binding: FragOrderArgueBinding
    private var sysCamera: SysCameraManager? = app.sysCamera
    private var order: EkiOrder? = null
    private var nowLoc: Location? = null

    private var selectPic: File? = null
    private var selectArgue: ArgueType? = null
    private var argueQuestion: String? = null

    var argueSource = ArgueSource.Manager

    var onArgueFinish = {}

    override fun initFragView() {

        sysCamera?.addCameraListenerFrom(this)
        toolBarTitle = getString(R.string.Appeal)

        setItemStyle()

        binding.argueText.setOnClickListener {
            selectArgueItem()
        }

        binding.imgFrame.setOnClickListener {
            SelectPhotoTypeDialog(
                when (argueSource) {
                    ArgueSource.Manager -> PPYPTheme.Manager
                    else -> PPYPTheme.CarUser
                }
            ).also { dialog ->
                dialog.onSelectAlbum = {

                    sysCamera?.startPhoto(object : SysCameraManager.DefaultPhotoSet() {
                        override val fileName: String
                            get() = "user_${DateTime().toStamp()}"
                        override val scaleX: Int
                            get() = screenWidth()
                        override val scaleY: Int
                            get() = screenWidth() * 2 / 3
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
                            get() = screenWidth() * 2 / 3
                    })
                }
            }.show(childFragmentManager, "SelectPhotoType")
        }

        binding.detailText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                argueQuestion = s?.toString()?.cleanTex

                checkInput()
            }
        })

        binding.determinBtn.setOnClickListener {
            order.notNull { o ->
                EkiRequest<ArgueBody>().also {
                    it.body = ArgueBody().apply {
                        selectPic.notNull { pic -> setImg(pic) }
                        setInfo(RequestBody.ArgueInfo().apply {
                            serial = o.SerialNumber
                            type = selectArgue?.value ?: 0
                            source = argueSource.value
                            text = argueQuestion ?: ""
                            lat = nowLoc?.latitude ?: 0.0
                            lng = nowLoc?.longitude ?: 0.0
                        })
                    }
                }.sendRequest(
                    context,
                    showProgress = true,
                    listener = object : OnResponseListener<ArgueResponse> {
                        override fun onReTry() {

                        }

                        override fun onFail(errorMsg: String, code: String) {

                        }

                        override fun onTaskPostExecute(result: ArgueResponse) {
                            onArgueFinish()
//                        toMainActivity()
                        }
                    },
                    showErrorDialog = true
                )
            }
        }
    }

    private fun setItemStyle() {
        binding.argueText.tailControl = when (argueSource) {
            ArgueSource.Manager -> object : GreenArrowDownClick() {
                override fun viewAfterClick(clickView: ImageView) {
                    selectArgueItem()
                }
            }
            else -> object : OrangeArrowDownClick() {
                override fun viewAfterClick(clickView: ImageView) {
                    selectArgueItem()
                }
            }
        }

        binding.uploadText.setTextColor(
            when (argueSource) {
                ArgueSource.Manager -> getColor(R.color.Eki_green_2)
                else -> getColor(R.color.Eki_orange_4)
            }
        )

        binding.arrowImg.setImageResource(
            when (argueSource) {
                ArgueSource.Manager -> R.drawable.icon_arrow_up_green
                else -> R.drawable.icon_arrow_up_orange
            }
        )
    }

    override fun onResumeFragView() {
        GPS.addListener(this)
        gps.checkNowLocation()
    }

    private fun selectArgueItem() {
        ArgueTypeSelectFrag(argueSource).also {
            it.onSelectArgueType = { type ->
                selectArgue = type
                binding.argueText.input = getString(type.str)
                checkInput()
            }
        }.show(childFragmentManager)
    }

    override fun onPause() {
        GPS.removeListener(this)
        super.onPause()
    }

    private fun checkInput() {
        binding.determinBtn.isEnabled = selectArgue != null && argueQuestion?.isNotEmpty() == true
    }

    override fun onDestroyView() {
        sysCamera?.removeCameraListener(this)
        sysCamera = null
        super.onDestroyView()
    }

    override fun onPicture(pic: File) {
        selectPic = pic
        binding.imgUpload.visibility = View.GONE
        binding.imgView.visibility = View.VISIBLE
        binding.imgView.setImageBitmap(pic.toBitMap())
    }

    override fun onPictureError() {

    }

    override fun setData(data: EkiOrder?) {
        order = data
    }

    override fun onGpsDisable() {
        gps.checkNowLocation()
    }

    override fun onGpsEnable() {

    }

    override fun locationChanged(loc: Location?, longitude: Double, latitude: Double) {
        nowLoc = loc
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragOrderArgueBinding.inflate(inflater, container, false)
        return binding
    }
}