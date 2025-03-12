package com.eki.parking.Controller.activity

import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.IActivityBackEvent
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.SiteDetail.SiteDetailInfoFrag
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.dialog.EkiProgressDialog
import com.eki.parking.Controller.impl.ICameraFileSet
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.manager.SysCameraManager
import com.eki.parking.Controller.process.EditLocationProcess
import com.eki.parking.Controller.process.SetLocationImgProcess
import com.eki.parking.Model.DTO.LocationImg
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.request.body.EditLocationBody
import com.eki.parking.Model.request.body.SetLocationImgBody
import com.eki.parking.Model.response.EditLocationResponse
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.libs.StateButton
import com.eki.parking.View.widget.LocationImgSelectPager
import com.eki.parking.extension.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format
import com.hill.devlibs.tools.Log
import java.io.File

/**
 * Created by Hill on 2020/03/10
 */
class SiteDetailActivity: TitleBarActivity(),
                          TitleBarActivity.ToolbarIconSet,
                          IActivityBackEvent,
                          SysCameraManager.CameraResultListener{

    private lateinit var location:ManagerLocation

    private val infoFrag=SiteDetailInfoFrag()
    private lateinit var sysCamera:SysCameraManager
    private lateinit var selectImgData:LocationImg

    private lateinit var imgSelectPager:LocationImgSelectPager
    private lateinit var cameraBtn: FloatingActionButton
    private lateinit var editBtn: StateButton

    override fun initActivityView() {
        imgSelectPager = findViewById<LocationImgSelectPager>(R.id.imgSelectPager)
        cameraBtn = findViewById<FloatingActionButton>(R.id.cameraBtn)
        editBtn = findViewById<StateButton>(R.id.editBtn)

        sysCamera=app.sysCamera
        sysCamera.addCameraListenerFrom(this)
//        Log.w("Img height->${locationImg.height}")
        location=intent.getParcel(AppFlag.DATA_FLAG)

        selectImgData= location.Img.minByOrNull { it.Sort } ?: LocationImg()

        imgSelectPager.initLocImg(location.Img)

        imgSelectPager.onImgSelect={ data->
            selectImgData=data
        }

        cameraBtn.setOnClickListener {
            sysCamera.startCamera(object : ICameraFileSet(){
                override val saveDir: File
                    get() = SysCameraManager.appCameraDir
                override val fileName: String
                    get() = DateTime().format("yyyyMMddHHmmss")
                override val scaleX: Int
                    get() = screenWidth()
                override val scaleY: Int
                    get() = dpToPx(200f)
            })
        }

        editBtn.setOnClickListener {
            var loc=infoFrag.getEditBackLoc()
            Log.d("$classTag LocName->${loc.Info?.Content} price->${loc.Config?.Price} socket->${loc.sockets} current->${loc.current}")
            loc.Socket.forEach {
                Log.i("select current->${it.currentEnum} socket->${it.chargeSocket}")
            }
            Log.w(" Loc position->${loc.Info?.sitePosition} type->${loc.Info?.siteType}  size->${loc.Info?.siteSize} ")
            Log.i("Loc weight->${loc.Info?.Weight} height->${loc.Info?.Height} remark->${loc.Config?.Text}")

            EkiMsgDialog().also {
                it.msg="確定要修改車位資訊?"
                it.determinClick={
//                    Log.i("determin click")
                    EditProcess(loc.toData()).apply {
                        onSuccess = {
                            location.copyFrom(loc)
                            location.sqlSaveOrUpdate()
                        }
                    }.run()
                }
            }.show(supportFragmentManager)
        }

        replaceFragment(FragLevelSet(1)
                .setFrag(infoFrag.also { it.setData(location) }),
                FragSwitcher.NON)
    }

    override fun setUpResumeComponent() {

    }

    override fun onDestroy() {
        sysCamera.removeCameraListener(this)

        super.onDestroy()
    }

    private var locEditBack:(ManagerLocation)->Unit={loc->
        EditProcess(loc.toData()).apply {
            onSuccess = {
                location.copyFrom(loc)
                location.sqlSaveOrUpdate()
            }
        }.run()
    }

    override fun setUpActivityView(): Int = R.layout.activity_site_detail
    override fun toolbarIcon(): Int =R.drawable.icon_back_green

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun onPicture(pic: File) {

        var progress=showProgress(ProgressMode.PROCESSING_MODE)

        var info=selectImgData.let {data->
            RequestBody.SetLocImg().also { body->
                body.serNum=location.Info?.SerialNumber?:""
                //先找有無一樣的sort值 有的話表示有圖片 沒有就加入新的
                body.sort=when{
                    location.Img.any { it.Sort==data.Sort }->data.Sort
                    else->{
                        var max= location.Img.maxByOrNull { it.Sort }?.Sort?:0

//                        Log.w("sort max->$max")
                        max + 1
                    }
                }
            }
        }

        EditImgProcess(SetLocationImgBody().apply {
            setImg(pic)
            setInfo(info)
        }).apply {
            onFail={
                progress.dismiss()
                showToast("上傳失敗")
            }
            onSuccess={list->
                showToast("圖片已上傳")
                progress.dismiss()
                location.apply {
                    Img.clear()
                    Img.addAll(list)
                }
                //更新
                imgSelectPager.initLocImg(location.Img)
                location.sqlSaveOrUpdate()

                imgSelectPager.getSelectImg().view.setImageBitmap(pic.toBitMap())
            }
        }.run()

    }

    override fun onPictureError() {

    }

    private inner class EditProcess(private var data:EditLocationBody) : EditLocationProcess(this) {
        var onSuccess={}
        var onFail={}

        override val body: EditLocationBody
            get() = data

        override val onResponse: OnResponseListener<EditLocationResponse>?
            get() = object : OnResponseListener<EditLocationResponse> {
                override fun onReTry() {
                    progress?.dismiss()
                }

                override fun onFail(errorMsg: String, code: String) {
                    progress?.dismiss()
                    onFail()
                }

                override fun onTaskPostExecute(result: EditLocationResponse) {
                    progress?.dismiss()
                    onSuccess()
                }
            }

        var progress: EkiProgressDialog?=null
        override fun run() {
            progress=from?.showProgress(ProgressMode.PROCESSING_MODE)
            super.run()
        }
    }

    private inner class EditImgProcess(imgBody:SetLocationImgBody) : SetLocationImgProcess(imgBody,this) {
        override fun run() {
            super.run()
        }
    }

    override fun onBackPress(): Boolean {
        sendBroadcast(AppFlag.UpdateView)
        return true
    }
}