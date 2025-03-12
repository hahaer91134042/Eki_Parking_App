package com.eki.parking.Controller.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.eki.parking.AppRequestCode
import com.eki.parking.R
import com.eki.parking.extension.color
import com.eki.parking.extension.string
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.hill.devlibs.activity.LibBaseActivity.OnResultBack
import com.hill.devlibs.extension.isNull
import com.hill.devlibs.extension.onNotNull
import com.hill.devlibs.manager.LibBaseManager
import com.hill.devlibs.tools.Log
import com.hill.devlibs.tools.MobileInfo


/**
 * Created by Hill on 2021/02/08
 */
class GooglePlayManager(context: Context) : LibBaseManager(context),
                                            OnResultBack{

    interface FinishListener{
        fun showMsgFrom():View
        fun onCheckFinish()
    }
    private val appUpdateManager = AppUpdateManagerFactory.create(context)
//    private val appUpdateManager=TestUpdateManager(context)
    private val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    init {
//        Log.i("$TAG DOWNLOADING->${InstallStatus.DOWNLOADING} DOWNLOADED->${InstallStatus.DOWNLOADED} INSTALLED->${InstallStatus.INSTALLED}")

    }

    var onFinishListener:FinishListener?=null

    fun checkInAppUpdate(activity: Activity){
        appUpdateManager.registerListener(installListener)

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            var updateAvailability=appUpdateInfo.updateAvailability()
            var isAllow=appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

            Log.w("$TAG updateAvailability->$updateAvailability isAllow->$isAllow")
//            appUpdateManager.startUpdateFlowForResult(
//                    appUpdateInfo,
//                    AppUpdateType.FLEXIBLE,
//                    activity,
//                    AppRequestCode.APP_UPDATE
//            )
//            appUpdateManager.completeUpdate()


            if (updateAvailability == UpdateAvailability.UPDATE_AVAILABLE
                    && isAllow) {
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        activity,
                        AppRequestCode.APP_UPDATE
                )
            } else {
                onFinishListener?.onCheckFinish()
            }
        }

//        appUpdateInfoTask.addOnFailureListener {
//
//        }
    }

    fun onStop(){
        appUpdateManager.unregisterListener(installListener)
    }

    private val installListener= InstallStateUpdatedListener { state ->

        Log.w("$TAG  InstallStatus->${state.installStatus()}")
        when(state.installStatus()) {
             InstallStatus.DOWNLOADED -> {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate()

            }
            InstallStatus.INSTALLED -> {
//            if (mAppUpdateManager != null){
//                mAppUpdateManager.unregisterListener(installStateUpdatedListener);
//            }
                onFinishListener?.onCheckFinish()
            }
            else -> {
                Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus())
                onFinishListener?.onCheckFinish()
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {

        onFinishListener?.showMsgFrom().isNull {
            appUpdateManager.completeUpdate()
        }.onNotNull { view ->
            Snackbar.make(view, string(R.string.Ready_to_install_the_update),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(string(R.string.Installation)) { view: View? ->
//            if (mAppUpdateManager != null) {
//                mAppUpdateManager.completeUpdate()
//            }
                        appUpdateManager.completeUpdate()
                    }.setActionTextColor(color(R.color.Eki_red_1))
                    .show()
        }

    }

    override fun requestCodes(): MutableList<Int> = arrayListOf(AppRequestCode.APP_UPDATE)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("$TAG activityResult  requestCode->$requestCode result->$resultCode date->$data")
    }


    private inner class TestUpdateManager(context: Context?) : FakeAppUpdateManager(context) {
        //目前這沒什麼用

        init {
            // Setup immediate update.
//            this.partiallyAllowedUpdateType = AppUpdateType.IMMEDIATE

            var vCode=MobileInfo.versionCode(context!!)
            Log.d("$TAG vCode->$vCode")

//            setUpdateAvailable(1)



            // Validate that immediate update is prompted to the user.
//            assertTrue(fakeAppUpdateManager.isImmediateFlowVisible)

            // Simulate user's and download behavior.
//            userAcceptsUpdate()

//            downloadStarts()
//
//            downloadCompletes()

            // Validate that update is completed and app is restarted.
//            assertTrue(fakeAppUpdateManager.isInstallSplashScreenVisible)
        }
    }
}