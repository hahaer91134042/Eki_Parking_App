package com.hill.devlibs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.hill.devlibs.activity.LibBaseActivity;
import com.hill.devlibs.impl.IMainActivity;
import com.hill.devlibs.time.DateTime;
import com.hill.devlibs.tools.Log;

import java.util.ArrayList;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

/**
 * Created by Hill on 2019/4/16
 */
public abstract class BaseApp<BaseActivity extends LibBaseActivity> extends MultiDexApplication {
    public static BaseApp appContext;
    protected ArrayList<BaseActivity> baseActivityList=new ArrayList<>();

    public static<APP extends BaseApp> APP getInstance(){
        if (appContext!=null)return (APP)appContext;
        else throw new NullPointerException("App is Empty restart app");
    }

    public interface FontConfig{
        @NonNull
        FontScale appFontScale();
    }

    protected static class FontScale{
        public float maxScale;
        public float minScale;
        public FontScale(@FloatRange(to = 1.5f,from = 1f) float max,
                         @FloatRange(to =1f,from = 0.5f) float min){
            maxScale=max;
            minScale=min;
        }
        public void updateConfig(Resources res){
            Configuration config=res.getConfiguration();
//            Log.w("config font Scale->"+config.fontScale);
            if (maxScale<config.fontScale)
                config.fontScale=maxScale;
            if (minScale>config.fontScale)
                config.fontScale=minScale;
//            Log.w("config font new Scale->"+config.fontScale);

            res.updateConfiguration(config,res.getDisplayMetrics());
        }
    }
    protected class DefaultStaticFont extends FontScale{

        public DefaultStaticFont(){
            super(1f,1f);
        }

        @Override
        public void updateConfig(Resources res) {
            Configuration config=res.getConfiguration();
            float offset=config.fontScale-maxScale;
            config.fontScale=maxScale-offset;
            res.updateConfiguration(config,res.getDisplayMetrics());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
        DateTime.initFrom(this);
        if (this instanceof FontConfig){
            FontConfig fontConfig=(FontConfig) this;
            fontConfig.appFontScale().updateConfig(getResources());
        }
    }
    //-------紀錄activity--------------------------------------------------

    public void printBaseActivityList(){
        for (int i = 0; i <baseActivityList.size() ; i++) {
            Log.e("--activity: "+i+" -->Name:"+baseActivityList.get(i).getClass().getSimpleName());
        }
    }
    public void addBaseActivity(BaseActivity baseActivity) {
        baseActivityList.add(baseActivity);
        Log.i("---AddBase---");
        printBaseActivityList();
    }
    public void removeBaseActivity(BaseActivity baseActivity) {
//        if (baseActivity instanceof IMainActivity)
//            onAppDestory();

        baseActivityList.remove(baseActivity);
        Log.i("---RemoveBase---"+baseActivity);
        Log.w("activityList size->"+baseActivityList.size());

        printBaseActivityList();
        if (baseActivityList.size()<1 || baseActivity.isMain())
            onAppDestory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("App Terminate");
    }

    protected void onAppDestory(){

    }
//    public void cleanUserData(){
//    }

    public ArrayList<BaseActivity> getBaseActivityList() {
        return baseActivityList;
    }
    public void clearBaseActivityStack() {
        for (BaseActivity activity : baseActivityList) {
            activity.finish();
        }
    }
    public BaseActivity getTopStackActivity(){
        return baseActivityList.size()>0?baseActivityList.get(baseActivityList.size()-1):null;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = appContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = appContext.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    public abstract boolean hasMainActivity();
}
