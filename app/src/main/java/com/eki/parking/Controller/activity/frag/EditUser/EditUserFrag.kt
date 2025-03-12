package com.eki.parking.Controller.activity.frag.EditUser

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.MemberEditPrecess
import com.eki.parking.Controller.tools.IconPhotoEvent
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.MemberEditBody
import com.eki.parking.Model.request.body.PostImgBody
import com.eki.parking.Model.response.PostImgResponse
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.databinding.FragEditUserBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.isNotEmpty
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.tools.ValidCheck
import java.io.File

/**
 * Created by Hill on 2020/05/29
 */
class EditUserFrag : SearchFrag(), IFragViewBinding {

    var onEditPwd = {}
    var onEditPhone = {}

    private lateinit var binding: FragEditUserBinding
    private lateinit var photoEvent: IconPhotoEvent

    override fun initFragView() {
        sqlData<EkiMember>().notNull { member ->
            setUpIconView(member)
        }
    }

    override fun onResumeFragView() {
        super.onResumeFragView()
        toolBarTitle = getString(R.string.Edit_profile)
        sqlData<EkiMember>().notNull { member ->
            setUpNickView(member)
            setSmsView(member)
            setUpPwdView(member)
            setUpMailView(member)
        }
    }

    private fun setUpMailView(member: EkiMember) {
        binding.mailInputView.input = member.mail
        binding.mailInputView.tailTextControl = object : TailEditCtrl() {
            override fun viewAfterClick(clickView: TextView) {
                binding.mailInputView.input.isNotEmpty { m ->
                    val check = ValidCheck.mail(m)
                    if (check.valid) {
                        val body = member.toData().apply { mail = m }
                        MemberEdit(body).also {
                            it.onSuccess = {
                                member.apply { mail = m }
                                member.sqlSaveOrUpdate()
                                sendBroadcast(AppFlag.OnMemberCheck)
                            }
                        }.run()
                    } else {
                        showToast(getString(R.string.Mail_format_error))
                    }
                }
            }
        }
    }

    private fun setSmsView(member: EkiMember) {
        binding.smsTexView.input = member.phoneNum
        member.info.notNull {
            binding.smsTexView.contryCode = it.CountryCode
        }
        binding.smsTexView.sendTextView
        binding.smsTexView.sendTextView.text = getString(R.string.Edit)
        binding.smsTexView.sendTextView.setTextColor(getColor(R.color.Eki_orange_4))
        binding.smsTexView.inputEnable(false)
        binding.smsTexView.onSendClick = {
//            Log.w("Edit sms")
            onEditPhone()
        }
    }

    private fun setUpPwdView(member: EkiMember) {
        binding.pwdInputView.input = "123456789"
        binding.pwdInputView.tailTextControl = object : TailEditCtrl() {
            override fun viewAfterClick(clickView: TextView) {
                onEditPwd()
            }
        }
    }

    private fun setUpNickView(member: EkiMember) {
        member.info.notNull {
            binding.nickNameInputView.input = it.NickName
        }
        binding.nickNameInputView.tailTextControl = object : TailEditCtrl() {
            override fun viewAfterClick(clickView: TextView) {
                binding.nickNameInputView.input.isNotEmpty { n ->
                    val body = member.toData().apply { info.notNull { it.nickName = n } }
                    MemberEdit(body).also {
                        it.onSuccess = {
                            member.apply { info.notNull { info -> info.NickName = n } }
                            member.sqlSaveOrUpdate()
                            //用來更新側邊欄的暱稱使用
                            sendBroadcast(AppFlag.OnMemberCheck)
                        }
                    }.run()
                }
            }
        }
    }


    private fun setUpIconView(member: EkiMember) {
        member.info.notNull { info ->
            binding.userIconCameraView.loadUrl(info.IconImg)
        }

        photoEvent = object : IconPhotoEvent(context!!) {
            override val width: Int
                get() = binding.userIconCameraView.width
            override val height: Int
                get() = binding.userIconCameraView.height
            override val namePrefix: String
                get() = "user"

            override fun onPicture(pic: File) {
                binding.userIconCameraView.icon.setImageBitmap(pic.toBitMap())

                EkiRequest<PostImgBody>().also {
                    it.body = PostImgBody().setIcon(pic)
                }.sendRequest(context, true, object : OnResponseListener<PostImgResponse> {
                    override fun onReTry() {

                    }

                    override fun onFail(errorMsg: String, code: String) {

                    }

                    override fun onTaskPostExecute(result: PostImgResponse) {
                        result.info.notNull { info ->
                            member.info?.IconImg = info.imgUrl
                        }
                        sqlUpdate(member)
                    }
                })
            }

            override fun onPictureError() {

            }
        }

        binding.userIconCameraView.listener = photoEvent
    }

    private inner class MemberEdit(var b: MemberEditBody) : MemberEditPrecess(context) {
        override val body: MemberEditBody
            get() = b
    }

    override fun onDestroyView() {
        app.sysCamera.removeCameraListener(photoEvent)
        super.onDestroyView()
    }

    private abstract class TailEditCtrl : IViewControl<TextView>() {
        override fun clickViewSet(clickView: TextView) {
            clickView.text = string(R.string.Edit)
            clickView.setTextColor(color(R.color.Eki_orange_4))
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragEditUserBinding.inflate(inflater, container, false)
        return binding
    }
}