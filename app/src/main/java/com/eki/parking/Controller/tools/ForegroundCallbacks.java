package com.eki.parking.Controller.tools;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import com.hill.devlibs.tools.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

/**
 * Created by Hill on 2017/8/1.
 * copy and edit from http://www.jianshu.com/p/e7f64e6bc2cc
 * fix some fail problem
 */

public class ForegroundCallbacks implements Application.ActivityLifecycleCallbacks,
                                            LifecycleObserver {
    public static final long CHECK_DELAY = 0;
    public static final String TAG = ForegroundCallbacks.class.getName();
    private LifeCycleListener mForegroundListener;
    public interface Listener {
        void onBecameForeground();
        void onBecameBackground();
    }

    public interface LifeCycleListener{
        void onCreat(Activity activity);
        void onResume(Activity activity);
        void onStop(Activity activity);
        void onDestory(Activity activity);
    }

    private static ForegroundCallbacks instance;
    private boolean foreground = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<>();

    public static ForegroundCallbacks init(Application application){
        if (instance == null) {
            instance = new ForegroundCallbacks();
            application.registerActivityLifecycleCallbacks(instance);
            ProcessLifecycleOwner
                    .get()
                    .getLifecycle()
                    .addObserver(instance);
        }
        return instance;
    }
    public static ForegroundCallbacks getInstance(Application application){
        if (instance == null) {
            init(application);
        }
        return instance;
    }
//    public static ForegroundCallbacks getInstance(Context ctx){
//        if (`is` == null) {
//            Context appCtx = ctx.getApplicationContext();
//            if (appCtx instanceof Application) {
//                init((Application)appCtx);
//            }
////            throw new IllegalStateException(
////                    "Foreground is not initialised and " +
////                            "cannot obtain the Application object");
//        }
//        return `is`;
//    }
    public static ForegroundCallbacks getInstance(){
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }
    public boolean isForeground(){
        return foreground;
    }
    public boolean isBackground(){
        return !foreground;
    }
    public void addListener(Listener listener){
        listeners.add(listener);
    }
    public void removeListener(Listener listener){
        listeners.remove(listener);
    }

    public void setForegroundListener(LifeCycleListener listener){
        mForegroundListener=listener;
    }
    public void removeForegroundListener(){
        mForegroundListener=null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onBackground() {
//        Log.d("--app-- onBackground");
        foreground = false;
        handler.post(()->{
            for (Listener l : listeners) {
                try {
                    l.onBecameBackground();
                } catch (Exception exc) {
                    Log.d ("Listener threw exception!:"+exc.toString());
                }
            }
        });
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onForeground() {
        foreground = true;
//        Log.d("--app-- onForeground");
        handler.post(()->{
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    Log.d ("Listener threw exception!:"+exc.toString());
                }
            }
        });
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (mForegroundListener!=null){
            mForegroundListener.onResume(activity);
        }
        Log.e(TAG+"---("+activity.getClass().getSimpleName()+")--->  onResumed");

    }
    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG+"---("+activity.getClass().getSimpleName()+")--->  onPaused");
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mForegroundListener!=null){
            mForegroundListener.onCreat(activity);
        }
        Log.e(TAG+"---("+activity.getClass().getSimpleName()+")--->  onCreat");
    }
    @Override
    public void onActivityStarted(Activity activity) {}
    @Override
    public void onActivityStopped(Activity activity) {
        if (mForegroundListener!=null){
            mForegroundListener.onStop(activity);
        }
        Log.e(TAG+"---("+activity.getClass().getSimpleName()+")--->  onStop");
    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mForegroundListener!=null){
            mForegroundListener.onDestory(activity);
        }
        Log.e(TAG+"---("+activity.getClass().getSimpleName()+")--->  onDestroyed");
    }
}

