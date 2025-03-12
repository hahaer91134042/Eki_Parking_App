package com.eki.parking.Controller.tools

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.core.app.ActivityCompat


import com.eki.parking.Controller.activity.abs.BaseActivity
import com.hill.devlibs.tools.Log

import com.hill.devlibs.annotation.parse.PermissionFieldSetParser
import com.hill.devlibs.model.bean.PermissionSet
import com.hill.devlibs.model.bean.toCheckArray

import kotlin.collections.ArrayList

/**
 * Created by Hill on 2017/6/30.
 */
@TargetApi(Build.VERSION_CODES.M)
class PermissionController {

    private var listeners = ArrayList<StateListener?>()
    private var handler = Handler()
    private var fromList = ArrayList<BaseActivity<*>>()

    interface StateListener {
        fun permissionCheckOK()
        fun permissionCheckFail(code: Int)
    }

    init {
        Instance = this
    }

    companion object {
        const val FREE = 0
        const val ALL_PERMISSION_CODE = 1
        const val RECORD_AUDIO_CODE = 2
        const val CAMERA_CODE = 3
        const val WRITE_EXTERNAL_STORAGE_CODE = 4
        const val READ_EXTERNAL_STORAGE_CODE = 5
        const val CALL_PHONE_CODE = 6
        const val FINE_LOCATION_CODE = 7
        const val COARSE_LOCATION_CODE = 8
        @JvmStatic
        var Instance: PermissionController? = null
            get() = field ?: PermissionController()

    }

    fun from(activity: BaseActivity<*>): PermissionController {
        fromList.add(activity)
        return this
    }

    fun remove(activity: BaseActivity<*>): PermissionController {
        fromList.remove(activity)
        return this
    }


    fun addListener(l: StateListener): PermissionController {
        if (!listeners.contains(l))
            listeners.add(l)
        return this
    }
    fun removeListener(l:StateListener): PermissionController {
        if (listeners.contains(l))
            listeners.remove(l)
        return this
    }

    fun checkAllPermissionIsGranted(clazz: Class<*>) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //延遲確認避免crash
            handler.postDelayed({
                var permissions = PermissionFieldSetParser.getAllPermission(clazz)
                var checkArray = permissions.let { it ->
                    var list = ArrayList<PermissionSet>()
                    if (fromList.isNotEmpty())
                        it.forEach {
                            var result = ActivityCompat.checkSelfPermission(fromList.first(), it.name)
                            if (result != PackageManager.PERMISSION_GRANTED)
                                list.add(it)
                        }
                    list.toCheckArray()
                }
                if (checkArray.isEmpty()) {
                    listeners.forEach {
                        it?.permissionCheckOK()
                    }
                } else {
                    if (fromList.isNotEmpty())
                        ActivityCompat.requestPermissions(fromList.first(), checkArray, ALL_PERMISSION_CODE)
                }
            }, 1000)
        }else{
            listeners.forEach {
                it?.permissionCheckOK()
            }
        }
    }

    fun checkPermission(vararg checks: Enum<*>) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            handler.postDelayed({
                var permissions = PermissionFieldSetParser.getPermission(checks)
                var checkArray = permissions.let { it ->
                    var list = ArrayList<PermissionSet>()
                    it.forEach {
                        if (fromList.isNotEmpty()) {
                            var result = ActivityCompat.checkSelfPermission(fromList.first(), it.name)
                            if (result != PackageManager.PERMISSION_GRANTED)
                                list.add(it)
                        }
                    }
                    list
                }
                Log.d("Permission checkArray size->${checkArray.size}")
                if (checkArray.isEmpty()) {
                    listeners.forEach {
                        it?.permissionCheckOK()
                    }
                } else {
                    if (fromList.isNotEmpty())
                        checkArray.forEach {
                            ActivityCompat.requestPermissions(fromList.first(), arrayOf(it.name), it.code)
                        }
                }
            }, 1000)
        }else{
            listeners.forEach {
                it?.permissionCheckOK()
            }
        }
    }


    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.e("-----onRequestPermissionsResult-----")
        Log.i("requsetCode->$requestCode")
        var isOK = when (requestCode) {
            ALL_PERMISSION_CODE -> {
                var b = true
                for (i in permissions.indices) {
                    Log.i("-permission->" + permissions[i] + " grantResult->" + grantResults[i])
                    if (grantResults[i] == -1)
                        b = false
                }
                b
            }
            else -> false
        }

        if (isOK)
            listeners.forEach {
                it?.permissionCheckOK()
            }
        else
            listeners.forEach {
                it?.permissionCheckFail(requestCode)
            }

    }


}
