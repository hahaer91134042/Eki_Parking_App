package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import com.eki.parking.R
import com.hill.devlibs.widget.libs.CircleImageView

/**
 * Created by Hill on 2019/12/03
 */
class UserModeIcon(context: Context?, attrs: AttributeSet?) : CircleImageView(context, attrs) {

    private var guestIconRes= R.drawable.icon_guest
    private var houseMasterRes=R.drawable.icon_house_master
    private var carMasterRes=R.drawable.icon_car_master


    init {
        setImageResource(guestIconRes)

    }

    fun useGuest(){
        setImageResource(guestIconRes)
    }
    fun useHouseMaster(){
        setImageResource(houseMasterRes)
    }
    fun useCarMaster(){
        setImageResource(carMasterRes)
    }
}