package com.eki.parking.Controller.activity

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.eki.parking.R
import com.eki.parking.Controller.activity.abs.IToolBarItemSet
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.Search.SearchListFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.View.widget.SearchToolBar
import com.eki.parking.extension.drawable
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 22,10,2019
 */
class SearchActivity: TitleBarActivity(),
                      IToolBarItemSet,
                      TitleBarActivity.SetToolbar,
                      TitleBarActivity.ToolbarIconSet,
                      SearchFrag.ISetToolBarLayout{


    override fun getToolBarActionView(): View =searchToolBar

    override fun setToolBarActionViewText(text: String) {
        searchToolBar.setText(text)
    }

    private lateinit var searchToolBar:SearchToolBar


    override fun itemRes(): Int =R.id.searchItem

    override fun initMenuItem(item: MenuItem) {
        searchToolBar= SearchToolBar(this)


        item.actionView = searchToolBar
//        Log.d("actionView->${item.actionView}")
//        var searchToolBar=item.actionView as SearchToolBar

//        Log.d("SearchView->${searchToolBar.searchView} cleanBtn->${searchToolBar.cleanBtn}")

//        searchToolBar.searchView.init()

        searchToolBar.onStartSearch={query->
            startQuery(query,true)
        }
        searchToolBar.onSearchTextChange={query->
            startQuery(query,false)
        }
        searchToolBar.onCleanText={

        }

        //        searchToolBar.showKeyBoard()

    }


    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_search_activity
    }
//    override fun getTitleBarClass(): ActivityClassEnum =ActivityClassEnum.Search

    override fun setUpActivityView(): Int = R.layout.activity_title_bar

    override fun initActivityView() {
        replaceFragment(FragLevelSet(1).setFrag(SearchListFrag()),FragSwitcher.NON)
    }

    override fun setUpResumeComponent() {

    }

    override fun setUpToolbar(toolbar: Toolbar) {
        toolbar.background= drawable(R.color.color_white)
    }

    override fun toolbarIcon(): Int =R.drawable.icon_back_arrow_orange
}