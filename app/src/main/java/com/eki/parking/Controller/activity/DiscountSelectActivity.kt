package com.eki.parking.Controller.activity

import android.content.Intent
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.DiscountSelect.DiscountInputFrag
import com.eki.parking.Controller.activity.frag.DiscountSelect.DiscountSelectListFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.DiscountBody
import com.eki.parking.Model.response.DiscountResponse
import com.eki.parking.R
import com.eki.parking.extension.sendRequest
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/08/19
 */
class DiscountSelectActivity:TitleBarActivity(){


    private var listFrag=DiscountSelectListFrag()

    private lateinit var discountResponse:DiscountResponse
//    private var isBackToMain=true

    override fun initActivityView() {

//        isBackToMain=intent.getBooleanExtra(AppFlag.IsBackToMain,true)

        EkiRequest<DiscountBody>().also {
            it.body= DiscountBody()
        }.sendRequest(this,showProgress = true,listener = object : OnResponseListener<DiscountResponse> {
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {

            }

            override fun onTaskPostExecute(result: DiscountResponse) {
                discountResponse=result

                replaceFragment(FragLevelSet(1)
                        .setFrag(listFrag.also { frag->
                            frag.setData(discountResponse.info)
                            frag.onScanClick=onStartScan
                            frag.onDiscountSelect=onSelectDiscount
                        }),
                        FragSwitcher.SWITCH_FADE)
            }
        })
    }

    private var onSelectDiscount:(ResponseInfo.Coupon)->Unit={
        Log.w("select discount->$it")
        sendBroadcast(Intent(AppFlag.DiscountSelect).apply { putExtra(AppFlag.DATA_FLAG,it) })

//        when(isBackToMain){
//            true->toMain()
//            false->finish()
//        }

        toMain()

    }

    private var onStartScan={
        Log.d("Scan click")

        replaceFragment(FragLevelSet(2)
                .setFrag(DiscountInputFrag()),
                FragSwitcher.SWITCH_FADE)

    }

    override fun setUpResumeComponent() {

    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

//    override fun onBackPress(): Boolean {
//        Log.d("IsBackToMain->$isBackToMain")
//        if (!isBackToMain){
//            finish()
//        }
//        return isBackToMain
//    }
}