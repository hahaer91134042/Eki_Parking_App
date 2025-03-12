package com.eki.parking.Controller.activity.abs

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.drawerlayout.widget.DrawerLayout
import com.eki.parking.Controller.frag.SearchFrag

/**
 * Created by Hill on 01,10,2019
 */
@Deprecated(message = "Not Use")
abstract class LeftMenuNoTitleBarActivity:BaseActivity<SearchFrag>() {

    protected lateinit var drawerLayout: DrawerLayout
    private var classEnum:ActivityClassEnum=getClassEnum()
    private lateinit var drawerView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLeftMenuDrawer()
    }

    private fun initLeftMenuDrawer() {
        drawerLayout=findViewById(leftDrawerRes())
        drawerView=findViewById(drawerViewRes())
    }

    @IdRes
    abstract fun drawerViewRes(): Int

    @IdRes
    abstract fun leftDrawerRes(): Int
    protected abstract fun getClassEnum(): ActivityClassEnum

    protected fun toggleLeftDrawer(){
        if (drawerLayout.isDrawerOpen(drawerView))
            closeDrawer()
        else
            openDrawer()
    }

    protected fun openDrawer(){
        drawerLayout.openDrawer(drawerView)
    }
    protected fun closeDrawer(){
        drawerLayout.closeDrawer(drawerView)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}