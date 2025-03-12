package com.eki.parking.Controller.activity.frag.DiscountSelect.child

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.frag.ChildFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.ActionResponse
import com.eki.parking.databinding.FragCodeInputBinding
import com.eki.parking.extension.sendRequest
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/08/25
 */
class CodeInputFrag : ChildFrag(), IFragViewBinding {

    private lateinit var binding: FragCodeInputBinding
    override fun initFragView() {
        binding.determinBtn.setOnClickListener {
            var code = binding.codeInput.text.toString().trim().cleanTex
            startCheckCode(code)
        }
    }

    private fun startCheckCode(code: String) {
        EkiRequest<SendIdBody>().also {
            it.body = SendIdBody(EkiApi.LoadAction).apply {
                serNum.add(code)
            }
        }.sendRequest(
            context,
            showProgress = true,
            listener = object : OnResponseListener<ActionResponse> {
                override fun onReTry() {

                }

                override fun onFail(errorMsg: String, code: String) {

                    toMainActivity()
                }

                override fun onTaskPostExecute(result: ActionResponse) {

                    if (result.info.any { it.Serial == code }) {
                        var rAction = result.info.first { it.Serial == code }
                        sendBroadcast(Intent(AppFlag.ActionSelect).apply {
                            putExtra(
                                AppFlag.DATA_FLAG,
                                rAction
                            )
                        })
                    } else {
                        showToast("活動碼錯誤!")
                    }

                    toMainActivity()
                }
            })
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragCodeInputBinding.inflate(inflater, container, false)
        return binding
    }
}