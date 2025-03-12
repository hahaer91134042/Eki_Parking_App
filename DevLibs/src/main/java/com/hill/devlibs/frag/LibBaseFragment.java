package com.hill.devlibs.frag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.hill.devlibs.BaseApp;
import com.hill.devlibs.EnumClass.ProgressMode;
import com.hill.devlibs.IntentFlag;
import com.hill.devlibs.R;
import com.hill.devlibs.dialog.LibProgressDialog;
import com.hill.devlibs.frag.FragController.FragSwitcher;
import com.hill.devlibs.impl.IActivityIntent;
import com.hill.devlibs.impl.IFragViewBinding;
import com.hill.devlibs.impl.IFragViewRes;
import com.hill.devlibs.model.ValueObjContainer;
import com.hill.devlibs.tools.Log;

/**
 * Created by Hill on 2017/5/24.
 */

public abstract class LibBaseFragment<Frag extends LibBaseFragment,
                                   APP extends BaseApp> extends Fragment {
    //protected OnFragItemClickListener onFragItemClickListener;
    public String TAG=getClass().getSimpleName();
    protected FragController fragController;
    private LibProgressDialog progressDialog;
    Bundle savedState;
    private String bundleKey="internalSavedViewState8954201239547";
    protected @IdRes int childFragLoaderRes=getChildFragLoader();
    //在asyncTask裡面不能建立handler會跳錯
//    protected Handler handler=new Handler();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG,"onSaveInstanceState");
        saveStateToArguments();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragController=new FragController(getChildFragmentManager());
//        Log.e("restoreState->"+restoreStateFromArguments());
        if (!restoreStateFromArguments()){
            initFragView();
        }else {
            onRestartViewState(savedState);
        }
//        Log.e(TAG,"Frag->onActivityCreated");
    }

    protected void initFragView() {
//        Log.w("initFragView");
    }
    protected void onRestartViewState(Bundle savedState) {
    }

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"Frag->onCreateView");
        //這樣會使用舊有的view
        if (rootView==null || isReCreatedView()){
            if (isReCreatedView()) {
                rootView=null;
            }
            if(this instanceof IFragViewRes) {
                rootView=inflater.inflate(((IFragViewRes)this).setFragViewRes(),container,false);
            } else if(this instanceof IFragViewBinding) {
                rootView=((IFragViewBinding)this).fragViewBinding(inflater,container).getRoot();
            } else {
                rootView=fragView(inflater,container);
            }
            if (viewLayoutParams()!=null && rootView != null){
                rootView.setLayoutParams(viewLayoutParams());
            }
        }
        return  rootView;
    }

    protected View fragView(LayoutInflater inflater,@Nullable ViewGroup container){return null;}
    protected @Nullable ViewGroup.LayoutParams viewLayoutParams(){
        return null;
    }
    protected boolean isReCreatedView(){
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        onResumeFragView();
    }
    protected void onResumeFragView(){

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroyView() {
//        Log.e(TAG,"Frag->onDestroyView");
        saveStateToArguments();
        unRegisterReceiver();
        super.onDestroyView();

        //https://www.jianshu.com/p/808ea22eb5dc
        //避免一些fragment轉場動畫的問題
        if (rootView!=null){
            ViewGroup parentView = (ViewGroup) rootView.getParent();
            if (parentView!=null)
                parentView.removeView(rootView);
        }
    }

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle argument=getArguments();
            if (argument==null)
                argument=new Bundle();

            argument.putBundle(bundleKey, savedState);
            try {
                /**
                 * When activity destory that fragment attached.
                 * This method will be throw Exception.
                 * So,it will be ignore.
                 */
                setArguments(argument);
            }catch (IllegalStateException e){
            }
            savedState=null;
        }
    }
    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
//        Log.e("restoreArguments->"+b);
        if (b!=null && b.containsKey(bundleKey))
            savedState = b.getBundle(bundleKey);

        if (savedState != null) {
            return true;
        }
        return false;
    }
    private Bundle saveState() {
        Bundle state = new Bundle();
        return onSaveState(state);
    }

    protected Bundle onSaveState(@NonNull Bundle outState) {

        return outState;
    }

//    protected abstract int setFragViewRes();

//    public Frag setOnFragItemClickListener(OnFragItemClickListener listener){
//        onFragItemClickListener=listener;
//        return (Frag)this;
//    }

//    protected void onItemClick(View v){
//        if (onFragItemClickListener!=null){
//            onFragItemClickListener.onFragItemClick(v);
//        }
//    }


    protected void setChildFragLoader(@IdRes int res){
        childFragLoaderRes=res;
    }
    protected abstract int getChildFragLoader();

    protected String getScreenFragTag(int loaderRes){
        return fragController.getScreenFragTag(loaderRes);
    }
    public <ChildFrag extends LibBaseFragment> ChildFrag getScreenChildFrag(@IdRes int loaderRes){
        try {
            Fragment frag=fragController.getScreenFrag(loaderRes);
            if (frag !=null)
                return (ChildFrag)frag;
        }catch (Exception e){
        }
        return null;
    }

    protected void replaceFragment(Frag fragment, String targetTag, String backTag, FragSwitcher switchType) {
        fragController.replaceFragment(childFragLoaderRes,fragment,targetTag,backTag,switchType);
    }

    protected void sendBroadcast(String action, ValueObjContainer container) {
        Intent intent=new Intent(action);
        intent.putExtra(IntentFlag.DATA_FLAG,container);
        sendBroadcast(intent);
    }

    protected void sendBroadcast(Intent... intents){
        for (Intent intent:
                intents) {
            getContext().sendBroadcast(intent);
        }
    }
    protected void sendBroadcast(String... actions){
        for (String act:
             actions) {
            getContext().sendBroadcast(new Intent(act));
        }
    }
    protected void registerReceiver(String... actions){
        IntentFilter filter=new IntentFilter();
        for (String action:
             actions) {
            filter.addAction(action);
        }
        getContext().registerReceiver(receiver,filter);
    }
    protected void unRegisterReceiver(){
        try{
            getContext().unregisterReceiver(receiver);
        }catch (Exception e){

        }
    }
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onCatchReceive(intent.getAction(),intent);
        }
    };

    protected void onCatchReceive(String action, Intent intent) {
    }

    protected <V extends EditText> void openKeyBoard(V view){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,0);
    }

    protected void closeKeyBoard(){

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    protected void showProgress(ProgressMode mode){
        progressDialog=new LibProgressDialog(getContext(),mode);
        progressDialog.show();
    }
    protected void closeProgress(){
        if (progressDialog!=null){
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    protected abstract void showErrorMsgDialog(String message);

    public void onKeyDown(){}
    public void onBackPress(){}

//    protected BaseActivity getTopActivity(){
//        return (BaseActivity) getApp().getTopStackActivity();
//    }

    protected abstract APP getApp();
    protected abstract int getWidth();
    protected abstract int getHeight();

    protected void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }
    protected void startActivitySwitchAnim(Class<?> activity){
        startActivitySwitchAnim(activity,true);
    }
    protected void startActivitySwitchAnim(Class<?> activity,boolean isAnim){
        Intent it=new Intent();
        it.setClass(getContext(),activity);
        startActivity(it);
        if (isAnim)
            getActivity().overridePendingTransition(R.anim.from_black_to_white, R.anim.from_white_to_black);
    }
    public void startActivitySwitchAnim(Intent intent){
        startActivitySwitchAnim(intent,true);
    }
    protected void startActivitySwitchAnim(Intent intent,boolean isAnim){
        startActivity(intent);
        if (isAnim)
            getActivity().overridePendingTransition(R.anim.from_black_to_white, R.anim.from_white_to_black);
    }
    public void startActivitySwitchAnim(IActivityIntent impl, boolean isAnim){
        if(impl instanceof IActivityIntent.ForResultBack){
            startActivityForResult(impl.getIntent(),((IActivityIntent.ForResultBack) impl).getRequestCode());
        }else {
            startActivitySwitchAnim(impl.getIntent(),isAnim);
        }
    }

    protected  @ColorInt int getColor(@ColorRes int res){return getApp().getResources().getColor(res);}
    protected String getResString(@StringRes int res){
        return getApp().getString(res);
    }
    protected String[] getStringArr(@ArrayRes int res){
        return getApp().getResources().getStringArray(res);
    }

}
