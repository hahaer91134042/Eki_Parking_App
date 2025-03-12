package com.hill.devlibs.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.hill.devlibs.listener.BroadcastListener
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2018/5/21.
 */
abstract class BaseReceiver(private val context: Context, vararg actions: String?) : BroadcastReceiver(), BroadcastListener {
    var actions: Array<out String?>? = actions

    constructor(context: Context) : this(context, null)

//    init {
//        this.actions = actions
//    }

    fun setAction(vararg actions: String): BaseReceiver {
        this.actions = (actions as? Array<String>)!!
        return this
    }

    fun register() {
        if (actions == null) return

        val filter = IntentFilter()
        for (action in actions!!) {
            filter.addAction(action)
        }
        context.registerReceiver(this, filter)
    }

    fun unRegister() {
        try {
            context.unregisterReceiver(this)
        } catch (e: Exception) {

        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        onCatchReceiver(action, intent)
    }
}
