package com.eki.parking.Controller.activity.frag.Main

import android.content.Intent
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.frag.Main.child.SettingPanelFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.extension.sqlHasData
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IFragViewRes
import com.hill.devlibs.tools.Log

class SettingFrag:SearchFrag(),IFragViewRes{

    override fun onResumeFragView() {
        toolBarTitle=getString(R.string.Setting_option)
        registerReceiver(AppFlag.OnLogout)


        if (sqlHasData<EkiMember>()){

            replaceFragWithCacheChildFrag(ChildCacheLevel(1)
                    .setFrag(SettingPanelFrag()),FragSwitcher.SWITCH_FADE)

        }else{
            replaceFragWithCacheChildFrag(ChildCacheLevel(1)
                    .setFrag(UserNoLoginFrag()),FragSwitcher.SWITCH_FADE)
        }
    }

    override fun onCatchReceive(action: String?, intent: Intent?) {
        Log.d("setFrag get action->$action")
        replaceFragWithCacheChildFrag(ChildCacheLevel(1)
                .setFrag(UserNoLoginFrag()),FragSwitcher.SWITCH_FADE)
    }

    override fun onPause() {
        super.onPause()
        unRegisterReceiver()
    }

    override fun setFragViewRes(): Int = R.layout.frag_setting

}