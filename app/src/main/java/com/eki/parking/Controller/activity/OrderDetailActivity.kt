package com.eki.parking.Controller.activity

import android.content.Intent
import androidx.appcompat.widget.Toolbar
import com.eki.parking.AppFlag
import com.eki.parking.AppResultCode
import com.eki.parking.Controller.activity.abs.BaseActivity
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.OrderDetail.OrderArgueFrag
import com.eki.parking.Controller.activity.frag.OrderDetail.OrderDetailPanelFrag
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.extension.color
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/05/19
 */
class OrderDetailActivity:TitleBarActivity(),
                          TitleBarActivity.SetToolbar,
                          BaseActivity.ActivityBackResult{


    private var detailPanel=OrderDetailPanelFrag()

//    private var argueFrag=OrderArgueFrag()
    private lateinit var order:EkiOrder
    private var backCode=AppResultCode.OrderStateNotChange

    override fun initActivityView() {
        order=intent.getParcel(AppFlag.DATA_FLAG)

        replaceFragment(FragLevelSet(1)
                .setFrag(detailPanel
                        .also {
                            it.setData(order)
                            it.onArgue=onArgueFrag
                            it.onRatingFinish={
                                backCode=AppResultCode.OrderStateChange
                            }
                        }),FragSwitcher.NON)

    }

    private var onArgueFrag:(EkiOrder)->Unit={
        replaceFragment(FragLevelSet(2)
                .setFrag(OrderArgueFrag().also {
                    it.setData(order)
//                    it.closeArgueBtn=detailPanel.closeArgueBtn
                    it.onArgueFinish={
                        backCode=AppResultCode.OrderStateChange
                        toMain()
                    }
                }),FragSwitcher.RIGHT_IN)
    }

    override fun setUpResumeComponent() {

    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        Log.d("back press")
//    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        toolbar.setBackgroundColor(color(R.color.Eki_green_2))
    }

    override fun activityBackCode(): Int =backCode
    override fun backData(): Intent? =null
}