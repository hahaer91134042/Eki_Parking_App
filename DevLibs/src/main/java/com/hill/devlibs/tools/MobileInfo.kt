package com.hill.devlibs.tools

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.util.*

/** * Mobile Info Utils * create by heliquan at 2017年3月23日  */
/**
 * Copy from http://www.voidcn.com/article/p-dpiicqfm-zw.html
 * Edit by Hill on 2017/11/13.
 */
object MobileInfo {
    var locale = Locale.getDefault()

    @JvmStatic
    fun appVersionFrom(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Could not get package name: $e")
        }
    }

    @JvmStatic
    fun versionCode(context: Context): Int {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Could not get package name: $e")
        }
    }

    val country:String
        get() = locale.country
    val language: String
        get() = locale.language

    val releaseVer: String
        get() = Build.VERSION.RELEASE

    val mobileModel: String
        get() = Build.MODEL

    /** * Get Mobile Type * * @return  */
    val mobileType: String
        get() = Build.MANUFACTURER
}