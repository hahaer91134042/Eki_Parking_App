package com.eki.parking.Controller.activity

import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.eki.parking.Controller.activity.abs.IToolBarItemSet
import com.eki.parking.Controller.activity.abs.LeftMenuWithTitleBarActivity
import com.eki.parking.Controller.activity.frag.Main.MapFrag
import com.eki.parking.Controller.activity.frag.Main.NotifyFrag
import com.eki.parking.Controller.activity.frag.Main.OrderFragment
import com.eki.parking.Controller.activity.frag.Main.SettingFrag
import com.eki.parking.Controller.activity.frag.Main.leftMenu.NavigationDrawerFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.LoadLocationConfig
import com.eki.parking.R
import com.eki.parking.View.widget.PpypBottomBar
import com.eki.parking.View.widget.SearchToolBarMain
import com.eki.parking.extension.showProgress
import com.eki.parking.extension.sqlDataList
import com.eki.parking.extension.sqlDataListAsync
import com.hill.devlibs.extension.toJsonStr
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet
import com.hill.devlibs.impl.IMainActivity
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2018/04/15.
 */

class MainActivity : LeftMenuWithTitleBarActivity(),
    IMainActivity,
    IToolBarItemSet,
    SearchFrag.ISetToolBarLayout {

    private lateinit var menuFrag: NavigationDrawerFrag
    private var searchBar: SearchToolBarMain? = null

    private val mapFrag = MapFrag()
    private val orderFragment = OrderFragment()
    private val settingFrag = SettingFrag()
    private lateinit var bottomBar:PpypBottomBar

    private val toChargePage: () -> Unit = {
        startActivitySwitchAnim(ChargeParkingSpaceActivity::class.java)
    }

    override fun setToolBarActionViewText(text: String) {
        searchBar?.searchTex?.text  = text
    }

    override fun getToolBarActionView(): View? =searchBar

    override fun itemRes(): Int =R.id.my_search

    override fun initMenuItem(item: MenuItem) {
        Log.w("---$classTag---  initMenuItem---")
        searchBar= SearchToolBarMain(this)
        item.actionView=searchBar
        searchBar?.searchTex?.setOnClickListener {
            startActivitySwitchAnim(SearchActivity::class.java)
        }

        //目前沒用
        searchBar?.startSearchBtn?.setOnClickListener {
            mapFrag.onStartSearch()
        }
        //目前沒用
        searchBar?.cleanSearchBtn?.setOnClickListener {
            mapFrag.onCleanSearch()
            setToolBarActionViewText("")
        }

    }

    override fun setUpActivityView(): Int = R.layout.activity_main_no_banner

    override fun initActivityView() {
        bottomBar = findViewById<PpypBottomBar>(R.id.bottomBar)

        val leftFrag = getScreenFrag<Fragment>(R.id.navigation_drawer)
        if (leftFrag is NavigationDrawerFrag){
            menuFrag= leftFrag
            menuFrag.setDrawerCallkBack(object :NavigationDrawerFrag.DrawerSwitchCallBack{
                override fun onCloseDrawer(isAnim: Boolean) {
                    ifDrawerOpenToClose(isAnim)
                }

                override fun onOpenDrawer(isAnim: Boolean) {
                }
            })
        }

        bottomBar.addMapEvent(object :PpypBottomBar.SelectEvent(){
            override fun onClick() {
                openToolBarActionView()
                replaceFragment(FragLevelSet(1)
                    .setFrag(mapFrag.also { it.toChargePage = toChargePage })
                    ,FragSwitcher.SWITCH_FADE)
            }
        })

        bottomBar.addCheckoutEvent(object :PpypBottomBar.SelectEvent(){
            override fun onClick() {
                replaceFragment(FragLevelSet(1)
                    .setFrag(orderFragment.also {
                        it.toReserveDetail2 = { order ->
                            val intent = Intent()
                            intent.setClass(this@MainActivity, ReserveDetailActivity::class.java)
                            intent.putExtra("order",order.toJsonStr())
                            startActivity(intent)
                        }
                    }),
                    FragSwitcher.SWITCH_FADE
                )
                val progress=showProgress()

                sqlDataListAsync<EkiOrder> { list->
                    progress.dismiss()
                    orderFragment.setData(list)

                }
                closeToolBarActionView()
            }
        })

        bottomBar.addNotifyEvent(object :PpypBottomBar.SelectEvent(){
            override fun onClick() {
                Log.w("notify click")
                closeToolBarActionView()
                replaceFragment(FragLevelSet(1)
                    .setFrag(NotifyFrag()),FragSwitcher.SWITCH_FADE)
            }
        })

        bottomBar.addSettingEvent(object :PpypBottomBar.SelectEvent(){
            override fun onClick() {
                Log.w("setting click")
                closeToolBarActionView()
                replaceFragment(FragLevelSet(1)
                    .setFrag(settingFrag),FragSwitcher.SWITCH_FADE)
            }
        })

        bottomBar.selectMap()
    }

    override fun setUpResumeComponent() {
//        Log.d("---$classTag---  setUp resume tool bar action view  isOpenSearch->$isOpenSearch")
        setToolBarActionViewText(sqlDataList<LoadLocationConfig>().first().Address)
    }

    override fun setLeftMenuAndTitleBarComponent(): LeftMenuAndTitleBarComponent {
        return LeftMenuAndTitleBarComponent().useDefaultSetRes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //        Log.i(classTag+"->onActivityResult");
        menuFrag.onActivityResult(requestCode, resultCode, data)
    }

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_main
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        sqlDataListAsync<EkiOrder> { list->
            orderFragment.setData(list)
        }
    }
}