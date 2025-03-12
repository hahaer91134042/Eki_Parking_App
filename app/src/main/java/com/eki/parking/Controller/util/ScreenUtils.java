package com.eki.parking.Controller.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.DisplayMetrics;

import com.eki.parking.App;

import java.util.List;


/**
 * ScreenUtils
 * <ul>
 * <strong>Convert between dp and sp</strong>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-14
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new AssertionError();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        Context context = App.appContext;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        Context context = App.appContext;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int dpToPx(float dp) {
        return (int) (dp * ((float) App.appContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float pxToDp(int px) {
        return px / ((float) App.appContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean isScreenOn(Context context){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP)
            return pm.isInteractive();
        else
            return pm.isScreenOn();
    }

    public static String onScreenName(Context context){
        return getCurTopTaskName(context);
    }


    private static String getCurTopTaskName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);

        return runningTasks.isEmpty() ? "" : runningTasks.get(0).topActivity.getClassName();
    }

    private static String getCurBaseTaskName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        //獲得當前正在運行的activity
        List<ActivityManager.RunningTaskInfo> appList = mActivityManager.getRunningTasks(1000);
        for (ActivityManager.RunningTaskInfo running : appList) {
            return running.baseActivity.getClassName();
        }
        return "";
    }
}
