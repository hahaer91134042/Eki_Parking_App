package com.eki.parking.Controller.activity.intent

import android.content.Context
import android.content.Intent
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.HtmlActivity
import com.hill.devlibs.impl.IActivityIntent

/**
 * Created by Hill on 2020/10/23
 */
class HtmlIntent(from: Context,private var url:String) : IActivityIntent(from) {
    override val target: Class<*>
        get() = HtmlActivity::class.java
    override fun onIntent(intent: Intent) {
        intent.putExtra(AppFlag.DATA_FLAG,url)
    }
}