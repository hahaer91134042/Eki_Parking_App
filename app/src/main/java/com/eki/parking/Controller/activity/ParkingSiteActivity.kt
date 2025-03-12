package com.eki.parking.Controller.activity

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.ParkingSite.AddParkingSpaceFrag
import com.eki.parking.Controller.activity.frag.ParkingSite.ParkingSiteFrag
import com.eki.parking.R
import com.eki.parking.extension.color
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/02/21
 */

class ParkingSiteActivity:TitleBarActivity(),TitleBarActivity.SetToolbar{

    private var deleteMenuItem:MenuItem?=null
    private var itemVisible:Boolean=false
        set(value) {
//            Log.w("Is Item Visible->$value")
            field=value
            deleteMenuItem?.isVisible=value
        }

    override fun initActivityView() {
        when(intent.action){
            AppFlag.ToList->{
                replaceFragment(FragLevelSet(1)
                    .setFrag(ParkingSiteFrag()
                        .also { itemVisible=it.visible }),
                    FragSwitcher.NON)
            }
            AppFlag.ToSet->{

            }
        }
    }


    override fun setUpResumeComponent() {
        registerReceiver(AppFlag.SteamLocomotive)
        registerReceiver(AppFlag.ParkingSite)
        registerReceiver(AppFlag.IsBackToMain)
    }

    override fun onCatchReceive(action: String?, intent: Intent?) {
        when(action){
            //有空再重作吧
            AppFlag.SteamLocomotive -> {
                replaceSteamLocomotive()
            }
            AppFlag.ParkingSite -> {
                replaceParkingSite()
            }
        }
    }

    private fun replaceSteamLocomotive(){
        replaceFragment(FragLevelSet(2)
            .setFrag(AddParkingSpaceFrag()),
            FragSwitcher.RIGHT_IN)
    }

    private fun replaceParkingSite(){
        replaceFragment(FragLevelSet(1)
            .setFrag(ParkingSiteFrag()),
            FragSwitcher.LEFT_IN)
    }

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_parking_site_set_activity
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        toolbar.setBackgroundColor(color(R.color.Eki_green_2))
    }

    override fun onBackPressed() {
        val sf = supportFragmentManager.findFragmentById(R.id.fragViewLoader)
        val fragment = sf?.fragmentManager?.fragments?.firstOrNull()

        when(fragment) {
            is ParkingSiteFrag -> {toMain()}
            is AddParkingSpaceFrag -> {replaceParkingSite()}
            else -> super.onBackPressed()
        }
    }
}