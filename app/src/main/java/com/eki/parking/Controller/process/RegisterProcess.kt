package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Controller.dialog.EkiProgressDialog
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.PostImgBody
import com.eki.parking.Model.request.body.RegisterBody
import com.eki.parking.Model.response.PostImgResponse
import com.eki.parking.Model.response.RegisterResponse
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.sqlData
import com.eki.parking.extension.sqlSave
import com.eki.parking.extension.sqlUpdate
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.extension.*
import java.io.File

/**
 * Created by Hill on 2019/12/23
 */
abstract class RegisterProcess(from: Context?,private val pic: File?) :
        BaseProcess(from),
        BaseProcess.ISetRequestBody<RegisterBody>,
        BaseProcess.ISetProcessBack{

    override val request: EkiRequest<RegisterBody>
        get() = EkiRequest<RegisterBody>().also { it.body=body }

    private var progress=EkiProgressDialog(from,ProgressMode.PROCESSING_MODE)

    override fun run() {
//        body.printValue()
//        pic.isNull {
//            Log.d("icon picture is Null")
//        }.onNotNull {
//            Log.e("icon picture is NotNull")
//        }

//        onProcessOver()
//        onProcessFail()

        from.notNull { context ->
            progress.show()
            request.sendRequest(context,false,object:OnResponseListener<RegisterResponse>{
                override fun onReTry() {
                    progress.dismiss()
                }

                override fun onFail(errorMsg: String,code:String) {
                    progress.dismiss()
                    onProcessFail()
                }

                override fun onTaskPostExecute(result: RegisterResponse) {
                    sqlSave(EkiMember(body).apply {
                        token = result?.info?.token ?: ""
                    })

                    pic.isNull {
                        progress.dismiss()
//                        isProcessOver=true
                        onOver()
                    }.onNotNull { icon->
                        sendUserIcon(icon)
                    }
                }
            })
        }
    }

    private fun sendUserIcon(icon: File) {
        from.notNull { context ->
            EkiRequest<PostImgBody>().also {
                it.body= PostImgBody().setIcon(icon)
            }.sendRequest(context,false,object :OnResponseListener<PostImgResponse>{
                override fun onReTry() {
                    progress.dismiss()
                }

                override fun onFail(errorMsg: String,code:String) {
                    progress.dismiss()
                }

                override fun onTaskPostExecute(result: PostImgResponse) {
//                        SQLManager sqlManager=getSqlManager();
//                        EkiMember member=sqlManager.getObjData(EkiMember.class);
//                        member.getInfo().setIconImg(result.getInfo().getImgUrl());
//                        sqlManager.updateById(member);
                    var member= sqlData<EkiMember>()
                    result.info.notNull { info->
                        member?.info?.IconImg=info.imgUrl
                    }
                    sqlUpdate(member)

                    progress.dismiss()
//                    isProcessOver=true
                    onProcessOver()
                }
            })
        }
    }
}