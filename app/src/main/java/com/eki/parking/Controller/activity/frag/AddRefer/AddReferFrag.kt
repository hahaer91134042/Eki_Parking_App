package com.eki.parking.Controller.activity.frag.AddRefer

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.AddReferrerBody
import com.eki.parking.Model.response.ReferrerResponse
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.databinding.FragAddReferBinding
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.sqlData
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/06/23
 */
class AddReferFrag:SearchFrag(),IFragViewBinding{

    private lateinit var binding:FragAddReferBinding
    private var referrerBody=AddReferrerBody()

    override fun initFragView() {
        toolBarTitle = getString(R.string.Customer_Service_Code)
        binding.referrerText.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input=s?.toString()?.cleanTex?:""
//                Log.d("referre input->$input")
                referrerBody.code=input
                checkReferrerOK()
            }
        })

        binding.determinBtn.setOnClickListener {
            sendAddRequest()
        }
    }

    private fun sendAddRequest() {
        EkiRequest<AddReferrerBody>().also {
            it.body=referrerBody
        }.sendRequest(context,true,object :OnResponseListener<ReferrerResponse>{
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {
                showToast(getString(R.string.Join_failed))
                binding.referrerText.setText("")
            }

            override fun onTaskPostExecute(result: ReferrerResponse) {
                showToast(getString(R.string.Join_successfully))
                sqlData<EkiMember>().notNull { member->
                    member.referrer=referrerBody.code
                    member.sqlSaveOrUpdate()
                }
                sendBroadcast(AppFlag.OnReferrerAdd)
                backFrag()
            }
        },showErrorDialog = false)
    }

    private fun checkReferrerOK() {
        binding.determinBtn.isEnabled = referrerBody.code.isNotEmpty()
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragAddReferBinding.inflate(inflater,container,false)
        return binding
    }
}