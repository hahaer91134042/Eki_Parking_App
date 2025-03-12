package com.eki.parking.Controller.activity;

import android.animation.Animator;
import android.location.Location;
import android.view.View;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.eki.parking.Controller.activity.abs.BaseActivity;
import com.eki.parking.Controller.dialog.MsgDialog;
import com.eki.parking.Controller.listener.GpsListener;
import com.eki.parking.Controller.listener.OnMultiPageResponseBack;
import com.eki.parking.Controller.manager.GooglePlayManager;
import com.eki.parking.Controller.manager.SQLManager;
import com.eki.parking.Controller.manager.TimeAlarmManager;
import com.eki.parking.Controller.process.LoadLocationProcess;
import com.eki.parking.Controller.process.LoadOrderProcess;
import com.eki.parking.Controller.process.ManagerLocationProcess;
import com.eki.parking.Controller.process.ManagerLvProcess;
import com.eki.parking.Controller.tools.CheckRule;
import com.eki.parking.Controller.tools.GPS;
import com.eki.parking.Controller.tools.PermissionController;
import com.eki.parking.Controller.tools.PermissionController.StateListener;
import com.eki.parking.Controller.tools.ProcessExecutor;
import com.eki.parking.Model.DTO.MultiPageResponse;
import com.eki.parking.Model.EnumClass.EkiPermission;
import com.eki.parking.Model.request.body.LoadLocationBody;
import com.eki.parking.Model.response.LoadLocationResonse;
import com.eki.parking.Model.sql.EkiMember;
import com.eki.parking.Model.sql.EkiOrder;
import com.eki.parking.R;
import com.hill.devlibs.time.DateTime;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hill on 2017/11/14.
 */
public class LoadingActivity extends BaseActivity
                             implements StateListener,
                                        GpsListener {

    private MsgDialog errorDialog;
    private Location nowLocation;
    private boolean isProcessOver=false;
    private GooglePlayManager mPlayManager;

    @Override
    protected int setUpActivityView() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initActivityView() {
        mPlayManager=getApp().getPlayManager();
        mPlayManager.checkInAppUpdate(this);
        mPlayManager.setOnFinishListener(new GooglePlayManager.FinishListener() {
            @NotNull
            @Override
            public View showMsgFrom() {
                return findViewById(R.id.bgLoader);
            }

            @Override
            public void onCheckFinish() {
                startLoadingAnim();
            }
        });

        GPS.addListener(this);

        PermissionController.getInstance()
                .from(this)
                .addListener(this);
        startCheckLoadingFinish();
    }

    private void startLoadingAnim() {
        LottieAnimationView animView=findViewById(R.id.logoIconImg);

        animView.playAnimation();
        animView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                Log.w("--Animate End--");
                startCheckLoadingFinish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void startCheckLoadingFinish(){
        if (isProcessOver){
            toMain();
        }else {
            Timer timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isProcessOver){
//                                Log.i("--toMain--");
                        ArrayList<EkiOrder> orderList= getSqlManager().getObjList(EkiOrder.class);
                        ArrayList<EkiOrder> filterList = new ArrayList<>() ;
                        for (EkiOrder order : orderList) {
                            if ((order.isBeSettle() || order.isBeCheckOut())
                                    && (!order.getCp().getSerial().equals(""))) {
                                filterList.add(order);
                            }
                        }
                        if (orderList.size()<1){
                            toMain();
                        }else {
                            if (CheckRule.getOrderAllCheckoutRule().isInRule(orderList)){

                               // Log.d("取得Loading " , "checkout " + CheckRule.getOrderAllCheckoutRule().isInRule(orderList));

                                runOnUiThread(()->{
                                    showToast(getString(R.string.You_still_have_unpaid_orders));

                                    startActivitySwitchAnim(OnlyCheckoutActivity.class);
                                    finish();
                                });
                            } else if (!filterList.isEmpty()) { //Linda
                                runOnUiThread(() -> {
                                    startActivitySwitchAnim(ChargeParkingSpaceActivity.class);
                                    finish();
                                });
                            }else {
                                toMain();
                            }
                        }

                        cancel();
                        timer.cancel();
                        timer.purge();
                    }
                }
            },1000,1000);
        }
    }

    @Override
    protected void setUpResumeComponent() {
        initLoad();
    }


    private void initLoad() {
        if (errorDialog != null)
            errorDialog.dismiss();

        PermissionController.getInstance()
                .checkAllPermissionIsGranted(EkiPermission.class);
    }

    private final ProcessExecutor pExecutor=new ProcessExecutor();

    @Override
    public void permissionCheckOK() {
        handler.post(loadProcess);
    }

    private final Runnable loadProcess= () -> {
        GPS gps=getApp().getGps();

        if (gps.haveGps()){
            gps.checkNowLocation();
            //Log.d("Location Loading activity->"+nowLocation);

            if(getSqlManager().hasData(EkiMember.class)){
                //有登入會員 下載會員訂單
                pExecutor.getProcess().add(new LoadOrderProcess(LoadingActivity.this) {});

                if (getSqlManager().getObjData(EkiMember.class).getBeManager()){

                    //下載地主車位
                    pExecutor.getProcess().add(new ManagerLocationProcess(LoadingActivity.this,DateTime.now(),true){});
                    pExecutor.getProcess().add(new ManagerLvProcess(LoadingActivity.this));

                }
                pExecutor.setOnAllProcessOver(() -> {
                    addOrderAlarm();
                    startLoading();
                });
                pExecutor.run();

            }else {
                startLoading();
            }
        }else {
            gps.openGpsSetting();
        }
    };

    private void addOrderAlarm() {
        SQLManager sql=getSqlManager();
        if (sql.hasData(EkiOrder.class)){
            TimeAlarmManager timeAlarmManager=getApp().getTimeAlarmManager();

            List<EkiOrder> list=sql.getObjList(EkiOrder.class);
            for (EkiOrder order : list) {
                timeAlarmManager.addOrder(order);
            }
        }
    }

    //最耗時
    private void startLoading() {
        LoadLocationBody body=new LoadLocationBody();
        body.setLat(nowLocation.getLatitude());
        body.setLng(nowLocation.getLongitude());
        body.setNowTime(DateTime.now().toString());

        new LoadLocationProcess(this) {
            @NotNull
            @Override
            public LoadLocationBody getBody() {
                return body;
            }
            @NotNull
            @Override
            public OnMultiPageResponseBack<LoadLocationResonse> getOnResponse() {
                return new OnMultiPageResponseBack<LoadLocationResonse>() {
                    @Override
                    public void onReTry() {
                        showToast(getString(R.string.Reconnecting));
                        startLoading();
                    }
                    @Override
                    public void onFail(@NotNull String errorMsg, @NonNull String code) {
                    }
                    @Override
                    public void onTaskPostExecute(
                            @Nullable ArrayList<MultiPageResponse<LoadLocationResonse>> result) {
                        getSqlManager().save(body.getConfig().toSql());
                        isProcessOver=true;
                    }
                };
            }
        }.run();
    }

    @Override
    public void permissionCheckFail(int code) {
//                showToast(getString(R.string.Please_turn_on_permissions_to_enjoy_full_functionality));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayManager.onStop();
    }

    @Override
    protected void onDestroy() {
        GPS.removeListener(this);
        PermissionController.getInstance()
                .removeListener(this)
                .remove(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionController.getInstance().onRequestPermissionsResult(requestCode,permissions,grantResults);
//        startActivitySwitchAnim(MainActivity.class);
    }

    @Override
    public void locationChanged(Location loc, double longitude, double latitude) {
        nowLocation=loc;
    }

    @Override
    public void onGpsDisable() {
        getApp().getGps().checkNowLocation();
    }

    @Override
    public void onGpsEnable() {
        handler.post(loadProcess);
    }

}
