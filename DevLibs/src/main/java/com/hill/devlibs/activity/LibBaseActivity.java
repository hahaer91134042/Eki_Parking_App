package com.hill.devlibs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.hill.devlibs.BaseApp;
import com.hill.devlibs.R;
import com.hill.devlibs.frag.LibBaseFragment;
import com.hill.devlibs.frag.FragController;
import com.hill.devlibs.frag.FragController.FragSwitcher;
import com.hill.devlibs.impl.IMainActivity;
import com.hill.devlibs.tools.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public abstract class LibBaseActivity<Frag extends LibBaseFragment,APP extends BaseApp> extends AppCompatActivity {

    protected String classTag = getClass().getSimpleName();
    protected FragController fragController;
    protected Handler handler = new Handler();
    protected ArrayList<OnResultBack> resultBackList=new ArrayList();
    protected Boolean firstExecute=true;

    public interface OnResultBack{
        List<Integer> requestCodes();
        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    }

    public void addResultBack(OnResultBack back){
        if(!resultBackList.contains(back))
            resultBackList.add(back);
    }
    public void removeResultBack(OnResultBack back){
        if (resultBackList.contains(back))
            resultBackList.remove(back);
    }

    //--------Life cycle-------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("onCreat->"+classTag);
        super.onCreate(savedInstanceState);
        //隱藏標題列
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //設定隱藏狀態
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        firstExecute=false;
        setContentView(setUpActivityView());
        fragController = new FragController(this);
        initActivityView();
    }


    @Override
    protected void onResumeFragments() {
        // TODO Auto-generated method stub
        Log.e("onResumeFragments ->" + classTag);
        super.onResumeFragments();
        setUpResumeComponent();
    }

    @Override
    protected void onPause() {
        Log.e("onPause ->" + classTag);
        super.onPause();
        unRegisterReceiver();
    }

    @Override
    protected void onStop() {
        Log.e("onStop ->" + classTag);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("onDestroy ->" + classTag);
        super.onDestroy();
    }


    //----------cycle end-------------
    @NonNull
    protected abstract int setUpActivityView();
    protected abstract void initActivityView();
    protected abstract void setUpResumeComponent();
    protected abstract int setFragLoaderRes();

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            onCatchReceive(action, intent);
        }
    };

    protected void onCatchReceive(String action, Intent intent) {

    }

    protected void sendBroadcast(Intent... intents){
        for (Intent intent:
                intents) {
            sendBroadcast(intent);
        }
    }
    protected void sendBroadcast(String... actions){
        for (String act:
                actions) {
            sendBroadcast(new Intent(act));
        }
    }

    protected void registerReceiver(String... actions) {
        IntentFilter filter = new IntentFilter();
        for (String action :
                actions) {
            filter.addAction(action);
        }
        registerReceiver(receiver, filter);
    }

    private void unRegisterReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
    }
//	public void initActivityTransferAnim(){
//		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
//			getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//		}
//	}

    protected void startActivitySwitchAnim(Class<?> activity) {
        Intent it = new Intent();
        it.setClass(this, activity);
        startActivity(it);
        overridePendingTransition(R.anim.from_black_to_white, R.anim.from_white_to_black);
    }

    protected void startActivitySwitchAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.from_black_to_white, R.anim.from_white_to_black);
    }

    protected void replaceFragment(Frag fragment, String targetTag, String backTag, FragSwitcher switchType) {
        fragController.replaceFragment(setFragLoaderRes(), fragment, targetTag, backTag, switchType);
    }

    protected String getScreenFragTag() {
        return fragController.getScreenFragTag(setFragLoaderRes());
    }


    protected Frag getScreenFrag() {
        return (Frag) fragController.getScreenFrag(setFragLoaderRes());
    }

    protected <F extends Fragment> F getScreenFrag(@IdRes int res) {
        return (F) fragController.getScreenFrag(res);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("OnActivityResult  request code->"+requestCode);
        Log.i("LibBaseActivity get result code->"+resultCode+" data->"+data);
        getScreenFrag().onActivityResult(requestCode,resultCode,data);
        for (OnResultBack back : resultBackList) {
            List<Integer> requestCodes=back.requestCodes();
            if (requestCodes.contains(requestCode))
                back.onActivityResult(requestCode,resultCode,data);
        }
    }

    protected abstract int getHeight();

    protected abstract int getWidth();

    protected abstract APP getApp();

    protected abstract void toMain();
    public boolean isMain(){
        if (this instanceof IMainActivity)
            return true;
        return false;
    }

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		Log.i("dispatch keyEvent->"+event.toString());
//		return super.dispatchKeyEvent(event);
//	}

}
