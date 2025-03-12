package com.eki.parking;


import android.app.Activity;
import com.eki.parking.Controller.activity.abs.BaseActivity;
import com.eki.parking.Controller.manager.EkiHttpManager;
import com.eki.parking.Controller.manager.EkiPreferenceManager;
import com.eki.parking.Controller.manager.GooglePlayManager;
import com.eki.parking.Controller.manager.JobManager;
import com.eki.parking.Controller.manager.MailManager;
import com.eki.parking.Controller.manager.SQLManager;
import com.eki.parking.Controller.manager.SysCameraManager;
import com.eki.parking.Controller.manager.TimeAlarmManager;
import com.eki.parking.Controller.process.LeaveProcess;
import com.eki.parking.Controller.notify.EkiNotifyChannel;
import com.eki.parking.Controller.process.FirstExecuteProcess;
import com.eki.parking.Controller.process.LogoutProcess;
import com.eki.parking.Controller.service.EkiSocketJob;
import com.eki.parking.Controller.tools.ForegroundCallbacks;
import com.eki.parking.Controller.tools.GPS;
import com.eki.parking.Controller.tools.PermissionController;
import com.hill.devlibs.BaseApp;

import java.io.File;
import java.util.ArrayList;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;


public class App extends BaseApp<BaseActivity>
				 implements ForegroundCallbacks.LifeCycleListener{
//					        BaseApp.FontConfig{

	private EkiHttpManager httpManager;
	private SQLManager sqlManager;
    private SysCameraManager cameraManager;
    private EkiPreferenceManager preferenceManager;
    private PermissionController permissionController=new PermissionController();
    private GPS gps;
    private TimeAlarmManager timeAlarmManager;
    private MailManager mailManager;
    private JobManager jobManager;
    private GooglePlayManager playManager;
    public static Activity mainContext;//用來記錄 現在在最上層執行的context(activity)

	public static boolean isOpenTest;
//    private File appPictureDir,cameraDir,cameraTempDir;

	public static App getInstance(){
		if (appContext!=null)return (App)appContext;
		else throw new NullPointerException("App is Empty restart app");
	}


	@Override
	public void onCreate() {
		super.onCreate();
		ForegroundCallbacks.getInstance(this).setForegroundListener(this);//要自動抓目前使用的activity
		isOpenTest=appContext.getResources().getBoolean(R.bool.isOpenTest);
		SQLiteStudioService.instance().start(this);
		httpManager=new EkiHttpManager(this);
		sqlManager=new SQLManager(this);
        cameraManager=new SysCameraManager(this);
        preferenceManager=new EkiPreferenceManager(this);
        timeAlarmManager=new TimeAlarmManager(this);
        mailManager=new MailManager(this);
        playManager=new GooglePlayManager(this);

        EkiNotifyChannel.register(this);
//		creatFolder();
		gps=new GPS(App.this);
		permissionController.addListener(new PermissionController.StateListener() {
			@Override
			public void permissionCheckOK() {
//				if (gps==null)
//					gps=new GPS(App.this);
				gps.start();
			}

			@Override
			public void permissionCheckFail(int code) {
				showToast(getString(R.string.Please_turn_on_permissions_to_enjoy_full_functionality));
			}
		});
		creatDataInSql();


		jobManager = new JobManager(this);
//		jobManager.start();

	}

	private void creatDataInSql() {
		new FirstExecuteProcess(this).run();
	}

	public boolean sendSocketMsg(String msg){
		return EkiSocketJob.getInstance().sendMsg(msg);
	}

//    private void creatFolder() {
//        String inAppImgPath = Environment.getDataDirectory()+"/data/"+getPackageName()+"/picture";
//        appPictureDir=new File(inAppImgPath);
//        if (!appPictureDir.exists()){
//            appPictureDir.mkdir();
//        }
//
//        String cameraPath = Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DCIM + "/"+getString(R.string.cameraDir);
//        cameraDir = new File(cameraPath);
//        if (!cameraDir.exists()) {
//            cameraDir.mkdir();
//        }
//        cameraTempDir=new File(cameraPath+"/temp");
//        if(!cameraTempDir.exists()){
//            cameraTempDir.mkdir();
//        }
//    }


	@Override
	public void addBaseActivity(BaseActivity baseActivity) {
		baseActivity.addResultBack(cameraManager);
		baseActivity.addResultBack(playManager);
		super.addBaseActivity(baseActivity);
	}

	@Override
	public void removeBaseActivity(BaseActivity baseActivity) {
		baseActivity.removeResultBack(cameraManager);
		baseActivity.removeResultBack(playManager);
		super.removeBaseActivity(baseActivity);
	}

	@Override
    public ArrayList<BaseActivity> getBaseActivityList() {
        return super.getBaseActivityList();
    }

    @Override
    public BaseActivity getTopStackActivity() {
        return super.getTopStackActivity();
    }

    @Override
    public void clearBaseActivityStack() {
        super.clearBaseActivityStack();
    }

    public EkiHttpManager getHttpManager(){
		if (httpManager==null)
			httpManager=new EkiHttpManager(this);
		return httpManager;
	}

	public SQLManager getSqlManager() {
		if (sqlManager==null)
			sqlManager=new SQLManager(this);
		return sqlManager;
	}
	public SysCameraManager getSysCamera(){
	    return cameraManager;
    }
    public EkiPreferenceManager getPreferenceManager(){return preferenceManager;}
    public MailManager getMailManager(){return mailManager;}
    public File getAppPictureDir(){
        return cameraManager.appPictureDir;
    }
    public File getAppCameraDir(){
	    return cameraManager.appCameraDir;
    }
    public File getCameraTempDir(){
	    return cameraManager.appCameraTempDir;
    }

    public GPS getGps(){
		return gps;
	}

	public TimeAlarmManager getTimeAlarmManager(){return timeAlarmManager;}

	public GooglePlayManager getPlayManager(){return playManager;}

	public void startLogout() {
		//之後要整理成一個一個的process
//		sqlManager.clean(EkiMember.class);
//		sqlManager.clean(EkiOrder.class);
//		sendBroadcast(new Intent(AppFlag.OnLogout));
		new LogoutProcess().run();
	}


    @Override
	protected void onAppDestory(){
//		Log.e("App onAppDestory");
		gps.stopGPS();
		//gps=null;
		//沒用 不會送
//		sendBroadcast(new Intent(AppFlag.OnAppDestroy));
//		jobManager.stop();

		new LeaveProcess().run();

		SQLiteStudioService.instance().stop();
	}


    @Override
    public void onTrimMemory(int level) {
		//app退到背景會觸發
//	    Log.d("App onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
	public boolean hasMainActivity(){
		return baseActivityList.get(0).isMain();
	}

	@Override
	public void onCreat(Activity activity) {
		cameraManager.onCreat(activity);
		gps.onCreat(activity);
	}

	@Override
	public void onResume(Activity activity) {
		mainContext=activity;
		cameraManager.onResume(activity);
		gps.onResume(activity);
		mailManager.setActivity(activity);
	}

	@Override
	public void onStop(Activity activity) {
		cameraManager.onStop(activity);
		gps.onStop(activity);
	}

	@Override
	public void onDestory(Activity activity) {
		if(activity==mainContext)
			mainContext=null;
		cameraManager.onDestory(activity);
		gps.onDestory(activity);
		mailManager.setActivity(null);
	}

//	@NonNull
//	@Override
//	public FontScale appFontScale() {
////		return new FontScale(1.0f,0.9f);
//		return new DefaultStaticFont();
//	}


//	@Override
//	public void onCreat(Activity activity) {
//		Log.d("App activity onCreat->"+activity);
//		gps.from(activity);
//	}
//
//	@Override
//	public void onResume(Activity activity) {
//
//	}
//
//	@Override
//	public void onStop(Activity activity) {
//
//	}
//
//	@Override
//	public void onDestory(Activity activity) {
//
//	}

//	@Override
//    public void cleanUserData() {
////        sqlManager.deleteUser();
////        sqlManager.cleanShopCar();
//
//    }



}
