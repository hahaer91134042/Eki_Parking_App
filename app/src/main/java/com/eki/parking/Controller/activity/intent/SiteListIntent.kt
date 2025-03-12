package com.eki.parking.Controller.activity.intent

import android.content.Context
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.ParkingSiteActivity
import com.hill.devlibs.impl.IActivityIntent

/**
 * Created by Hill on 2020/02/24
 */
class SiteListIntent(from: Context) : IActivityIntent(from) {
    override val target: Class<*>
        get() = ParkingSiteActivity::class.java
    override val action: String
        get() = AppFlag.ToList
}