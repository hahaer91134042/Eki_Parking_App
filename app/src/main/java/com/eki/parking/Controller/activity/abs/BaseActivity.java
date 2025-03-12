package com.eki.parking.Controller.activity.abs;

//import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;

import com.eki.parking.App;
import com.eki.parking.Controller.activity.MainActivity;
import com.eki.parking.Controller.frag.BaseFragment;
import com.eki.parking.Controller.manager.SQLManager;
import com.eki.parking.R;
import com.hill.devlibs.activity.LibBaseActivity;
import com.hill.devlibs.frag.FragController.FragSwitcher;
import com.hill.devlibs.tools.Log;


public abstract class BaseActivity<Frag extends BaseFragment> extends LibBaseActivity<Frag,App> {

	public interface ActivityBackResult{
		int activityBackCode();
		@Nullable
		Intent backData();
	}

	private final int totalCacheLevel=3;
	protected FragLevelCache fragLevelCache=new FragLevelCache(totalCacheLevel);

	/**
	 * Cache有以下規則
	 * 1.一定要照順序依序存放 不能跳層級
	 * 2.從第1層開始
	 * 3.同一個層級只存最後顯示的fragment
	 */
	protected class FragLevelCache{
		private SparseArrayCompat<FragLevelSet> fragCacheList = new SparseArrayCompat<>();
		public int nowCacheNum=0;
		public FragLevelCache(int totalLevelNum){
			for (int i = 0; i <totalLevelNum ; i++) {
				fragCacheList.append(i,new FragLevelSet(i+1));
			}
		}
		public void setCache(FragLevelSet levelSet){
			if (levelSet.fragLevel<2){
				fragCacheList.get(levelSet.fragLevel-1).setFrag(getScreenFrag());
				nowCurrentCacheNum();
				for (int i = 0; i <fragCacheList.size() ; i++) {
					Log.d("cache frag->"+fragCacheList.get(i).frag);
				}
			}else {
				nowCurrentCacheNum();
				// TODO:Edit by Hill 2018/8/23 先檢查有沒有第一層
				if (nowCacheNum>0){//至少有第一層
					// TODO:Edit by Hill 2018/8/23 檢查順序
					if (nowCacheNum==levelSet.fragLevel-1){
						fragCacheList.get(levelSet.fragLevel-1).setFrag(getScreenFrag());
						for (int i = 0; i <fragCacheList.size() ; i++) {
							Log.d("cache frag->"+fragCacheList.get(i).frag);
						}
					}else {//沒照順序 EX:只有第一層 直接跳到要記錄第3層
						throw new NullPointerException("Your cache level do not continuity!!");
					}
				}else {
					throw new NullPointerException("You must set level 1 fragment first!!");
				}
			}
		}
		public Frag getCacheFrag(@IntRange(from = 1,to = 2)int whichLevel){
			if (whichLevel>totalCacheLevel){
				throw new RuntimeException("Which Level must less than total level.");
			}else {
				if (fragCacheList.get(whichLevel-1).haveFrag)
					return fragCacheList.get(whichLevel-1).frag;
			}
			return null;
		}

		public int nowCurrentCacheNum(){
			nowCacheNum=0;
			for (int i = 0; i <fragCacheList.size() ; i++) {
				if (fragCacheList.get(i).haveFrag){
					nowCacheNum++;
				}else {
					break;
				}
			}
			return nowCacheNum;
		}

		public void removeLastCache() {
			fragCacheList.get(nowCacheNum-1).clearFrag();
			nowCurrentCacheNum();
		}
		public void removeAllCache(){
			fragCacheList.clear();
			nowCurrentCacheNum();
		}
	}

	protected class FragLevelSet{
		public int fragLevel;// TODO:Edit by Hill 2018/8/23 從1層級開始計算
		public Frag frag;
		public boolean haveFrag=false;
		public FragLevelSet(){
			fragLevel=1;
		}
		public FragLevelSet(@IntRange(from = 1) int level){
			fragLevel=level;
		}
		public FragLevelSet setFrag(Frag f){
			frag=null;
			frag=f;
			haveFrag=true;
			return this;
		}
		public void clearFrag(){
			frag=null;
			haveFrag=false;
		}
	}

	@Deprecated
	protected enum ActivityClassEnum {
		Main(R.menu.menu_main),
		Login(R.menu.menu_not_thing),
		DateSearch(R.menu.menu_date_search),
		//        CarType(R.menu.menu_car_type),
//        TireTypeSearch(R.menu.menu_tire_type_search),
//        ProductDetailAndOrder(R.menu.menu_product_detail_and_order),
//        AddAccount(R.menu.menu_add_account),
		//        ProductList(R.menu.menu_more_and_detail),
//        ShopCar(R.menu.menu_shopping_car),
		Search(R.menu.menu_search_activity),
		Reserva(R.menu.menu_reserva)
//        ShopLocation(R.menu.menu_shop_location),
//        PayPage(R.menu.menu_pay_page)
//        LeftDrawer(R.menu.menu_left_drawer_option)
		;
		public int menuRes;

		ActivityClassEnum(@MenuRes int r) {
			menuRes = r;
		}
	}

	//--------Life cycle-------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        getApp().addBaseActivity(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
        getApp().removeBaseActivity(this);
		super.onDestroy();
	}


	//----------cycle end-------------
    //---------Fragment Cache---------

    //---------Cache End--------------
    @Override
	public int setFragLoaderRes(){
		return R.id.fragViewLoader;
	}


    public void toLogin() {
	    Log.d("toLogin");
		//startActivitySwitchAnim(LoginActivity.class);
    }

    //protected Boolean isBackToMain=true;

    @Override
	public void toMain() {
		Log.w("---to Main--  hasMain->" + getApp().hasMainActivity());
		if (!getApp().hasMainActivity()) {
			startActivitySwitchAnim(MainActivity.class);
		}
		if (this instanceof ActivityBackResult) {
			ActivityBackResult impl = (ActivityBackResult) this;
			setResult(impl.activityBackCode(), impl.backData());
		}
		finish();
	}

	//泛型不先在這邊Override會導致子代再用的時候 會抓到Lib的
    @Override
    protected void replaceFragment(Frag fragment, String targetTag, String backTag, FragSwitcher switchType) {
        super.replaceFragment(fragment, targetTag, backTag, switchType);
    }

    //會去call子類別的replaceFragment 假如有複寫的話
	protected void replaceFragment(FragLevelSet fragSet, FragSwitcher switchType){
		switch (fragSet.fragLevel){
			case 1:
//                fragLevelCache.setCache(fragSet);
				replaceFragment(fragSet.frag,fragSet.frag.TAG,null,switchType);
				break;
			default://第2層以上才存cache
				//不同層級之間切換  同層級不存
				int cacheNum=fragLevelCache.nowCurrentCacheNum();
				if (cacheNum<fragSet.fragLevel &&
						cacheNum+1!=fragSet.fragLevel){

					fragLevelCache.setCache(new FragLevelSet(fragSet.fragLevel-1));
				}
				replaceFragment(fragSet.frag,fragSet.frag.TAG,getScreenFragTag(),switchType);
				break;
		}
	}

	protected SQLManager getSqlManager(){
		return getApp().getSqlManager();
	}

    @Override
    protected App getApp() {
        return App.getInstance();
    }

    @Override
    protected int getHeight() {
        return App.getScreenHeight();
    }

    @Override
    protected int getWidth() {
        return App.getScreenWidth();
    }

}
