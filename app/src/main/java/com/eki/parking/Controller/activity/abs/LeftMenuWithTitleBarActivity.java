package com.eki.parking.Controller.activity.abs;

import android.os.Bundle;

import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.eki.parking.R;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


/**
 * Created by Hill on 2017/11/20.
 */

public abstract class LeftMenuWithTitleBarActivity extends TitleBarActivity {

    protected DrawerLayout drawerLayout;
    protected LeftMenuAndTitleBarComponent leftMenuCom=setLeftMenuAndTitleBarComponent();
    protected AppBarLayout appBarLayout;
    protected EkiActionBarToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLeftMenuDrawer();
    }

    @Override
    protected int toolBarRes() {
        return leftMenuCom.toolBarRes;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        closeDrawer(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //    private CoordinatorLayout mCoordinatorLayout;
//    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private void initLeftMenuDrawer() {
        drawerLayout = findViewById(leftMenuCom.drawerLayoutRes);
//        banner = findViewById(leftMenuCom.bannerRes);
        appBarLayout = findViewById(leftMenuCom.appBarLayoutRes);
//        mCoordinatorLayout=findViewById(R.id.coordinatorLayout);
//        mCollapsingToolbarLayout=findViewById(R.id.collapsing_toolbar_layout);


//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);

        final ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.hamburger);
//            actionBar.setHomeButtonEnabled(true);
//            mToolbar.setLogo(R.drawable.hamburger);

            mDrawerToggle = new EkiActionBarToggle(this,
                    drawerLayout,
                    mToolbar
            );
            mDrawerToggle.init();

            //這邊 因為漢堡比較不一樣 所以這邊也要設置 不然會無效
            if (this instanceof TitleBarActivity.ToolbarIconSet)
                mToolbar.setNavigationIcon(((TitleBarActivity.ToolbarIconSet)this).toolbarIcon());

        }
//        initBanner();
        //setLeftMenuList();

    }

    protected abstract LeftMenuAndTitleBarComponent setLeftMenuAndTitleBarComponent();

    protected void ifDrawerOpenToClose(boolean isAnim){
//        Log.e("isDrawer Open right->"+drawerLayout.isDrawerOpen(Gravity.RIGHT));
//        Log.w("isDrawer Open left->"+drawerLayout.isDrawerOpen(Gravity.LEFT));
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            closeDrawer(isAnim);
    }
    protected void closeDrawer(boolean isAnim){
        drawerLayout.closeDrawer(Gravity.LEFT,isAnim);
    }
    protected void openDrawer(boolean isAnim){
        drawerLayout.openDrawer(Gravity.LEFT,isAnim);
    }

    public void closeTitleBar() {// TODO: 2017/12/4 加點動畫看起來比較順
        Animation closeAm = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        appBarLayout.startAnimation(closeAm);
        handler.postDelayed(() -> {
            appBarLayout.setVisibility(View.GONE);
        }, 200);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void showTitleBar() {
        Animation showAm = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);


        appBarLayout.startAnimation(showAm);
        handler.postDelayed(() -> {
            appBarLayout.setVisibility(View.VISIBLE);
        }, 200);


        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }




    //---------------------------------------------------------
    public class LeftMenuAndTitleBarComponent {
        //        public int titleBarLayoutRes;
        public int drawerLayoutRes;
        public int toolBarRes;
        public int bannerRes;
        public int appBarLayoutRes;

        public LeftMenuAndTitleBarComponent setDrawerLayoutRes(int viewRes) {
            this.drawerLayoutRes = viewRes;
            return this;
        }

        public LeftMenuAndTitleBarComponent setToolBarRes(int res) {
            toolBarRes = res;
            return this;
        }


        public LeftMenuAndTitleBarComponent useDefaultSetRes() {
            this.drawerLayoutRes = R.id.drawerLayout;
            this.toolBarRes = R.id.toolbar;
            this.bannerRes = R.id.bannervp;
            this.appBarLayoutRes = R.id.appBarLayout;
            return this;
        }
    }

    private class EkiActionBarToggle extends ActionBarDrawerToggle {

        DrawerLayout drawerLayout;
        BaseActivity mActivity;

        public EkiActionBarToggle(BaseActivity activity, DrawerLayout drawerLayout, Toolbar toolbar) {
            super(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
            this.drawerLayout = drawerLayout;
            mActivity = activity;
        }

        public void init() {
            setDrawerIndicatorEnabled(true);


            drawerLayout.addDrawerListener(this);
            syncState();
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            mActivity.supportInvalidateOptionsMenu();
            //Log.d(classTag+" onDrawerOpen");
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            mActivity.supportInvalidateOptionsMenu();
            //Log.d(classTag+" onDrawerClosed");
        }
    }

}
