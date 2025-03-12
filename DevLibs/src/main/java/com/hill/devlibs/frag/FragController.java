package com.hill.devlibs.frag;


import com.hill.devlibs.R;
import com.hill.devlibs.tools.Log;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Hill on 2017/7/18.
 */

public class FragController {
    private String TAG=getClass().getSimpleName();
    public FragmentManager fragManager;
    private FragmentActivity activity;
    public FragController(FragmentActivity activity){
        this.activity=activity;
        fragManager=activity.getSupportFragmentManager();
    }

    public FragController(FragmentManager manager) {
        fragManager=manager;
    }


    public Fragment getScreenFrag(int loaderRes){
        return fragManager.findFragmentById(loaderRes);
    }
    public String getScreenFragTag(int loaderRes){
        return fragManager.findFragmentById(loaderRes).getTag();
    }
    public void remove(LibBaseFragment frag){
        //TODO:Edit by Hill 2018/3/13 Because sometime commit in unknow condition will be crash
        try {
            FragmentTransaction ft = fragManager.beginTransaction();
            ft.remove(frag).commitAllowingStateLoss();
        }catch (IllegalStateException e){
            Log.e(TAG,e.toString());
        }
    }



//    public void goBackFrag(String tag) {
//        FragmentTransaction ft = fragManager.beginTransaction();
//        ft.add(fragManager.findFragmentByTag(tag),tag).commit();
//
//    }

    public enum FragSwitcher{
        NON,
        RIGHT_IN,
        LEFT_IN,
        SWITCH_FADE
    }
    public void replaceFragment(int fragLoader, Fragment fragment, String targetTag, String backTag, FragSwitcher switchType) {
        //TODO:Edit by Hill 2018/3/13 Because sometime commit in unknow condition will be crash
        try {
            FragmentTransaction ft = fragManager.beginTransaction();
            switch (switchType) {
                case NON:
                    ft.replace(fragLoader, fragment, targetTag).addToBackStack(backTag)
                            .commitAllowingStateLoss();
                    break;
                case RIGHT_IN:
                    ft.setCustomAnimations(R.anim.slide_right_in,
                            R.anim.slide_left_out)
                            .replace(fragLoader, fragment, targetTag).addToBackStack(backTag)
                            .commitAllowingStateLoss();
                    break;
                case LEFT_IN:
                    ft.setCustomAnimations(R.anim.slide_left_in,
                            R.anim.slide_right_out)
                            .replace(fragLoader, fragment, targetTag).addToBackStack(backTag)
                            .commitAllowingStateLoss();
                    break;
                case SWITCH_FADE:
                    ft.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                            .replace(fragLoader, fragment, targetTag).addToBackStack(backTag)
                            .commitAllowingStateLoss();
                    break;
            }
        }catch (IllegalStateException e){
            Log.e(TAG,e.toString());
            Log.e("error frag->"+fragment.getClass());
        }
    }
}
