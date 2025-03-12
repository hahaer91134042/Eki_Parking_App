package com.eki.parking.Controller.manager

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.hill.devlibs.preference.LibPerference
import com.hill.devlibs.tools.Log


/**
 * Created by Hill on 2019/6/20
 */
class EkiPreferenceManager(context: Context?) : BaseManager(context) {

    var settingPreference=SettingPreference(context).creat("SettingPreference")

    init{
        //settingPreference?.fcmToken= FirebaseInstanceId.getInstance().token
        FirebaseMessaging.getInstance().token.addOnSuccessListener { instanceIdResult ->
            val deviceToken = instanceIdResult
            Log.w("Fcm get token->$deviceToken")
        }

    }


    class SettingPreference(context: Context?) : LibPerference<SettingPreference>(context) {
        var fcmToken:String?
        get() {
            return setting?.getString("FcmToken","")!!
        }
        set(value) {
            editor?.putString("FcmToken",value)
            editor?.commit()
        }


    }



}