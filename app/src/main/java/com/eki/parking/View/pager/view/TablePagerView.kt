package com.eki.parking.View.pager.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Controller.impl.IPagerTabContent
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.View.abs.RelativeCustomView
import com.eki.parking.View.pager.ViewPagerFragAdapter
import com.eki.parking.extension.colorStateList
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.drawable
import com.google.android.material.tabs.TabLayout
import com.hill.devlibs.extension.isNull
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.onNotNull

/**
 * Created by Hill on 14,11,2019
 */

class TablePagerView(context: Context?, attrs: AttributeSet?) : LinearCustomView(context, attrs) {

    private var indicatorHeight= dpToPx(3.0f)
    private lateinit var fragManager:FragmentManager
    private var pageList:List<BaseFragment<*>>?=null

    interface OnSelectPageEvent{
        fun onSelectPage()
    }

    private val pagerTab:TabLayout = findViewById(R.id.pagerTab)
    private val viewPager:ViewPager = findViewById(R.id.viewPager)

    init {
        pagerTab.isSmoothScrollingEnabled=true
        pagerTab.setSelectedTabIndicatorHeight(indicatorHeight)
        //自訂義View使用 https://blog.csdn.net/zhangphil/article/details/48934039
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(pagerTab))
        pagerTab.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                pageList?.get(position).notNull { f->
//                    Log.d("select page->${f.javaClass.simpleName}")
                    if (f is OnSelectPageEvent)
                        f.onSelectPage()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun setSelectedTabIndicatorColor(@ColorInt color:Int){
        pagerTab.setSelectedTabIndicatorColor(color)
    }

    fun setSelectedTabIndicator(drawable:Int) {
        pagerTab.setSelectedTabIndicator(drawable)
    }

    private fun clearView(){
        pagerTab.removeAllTabs()
        viewPager.removeAllViews()
        viewPager.adapter=null
        //這邊要先commit一次 不然會跳錯
        fragManager.beginTransaction().commitNow()
    }

    fun <F:BaseFragment<*>> setPagerList(fragManager:FragmentManager,list:ArrayList<IPagerTabContent<F>>,isRecycle:Boolean=false){
        this.fragManager=fragManager
        clearView()
        if (!isRecycle)
            viewPager.offscreenPageLimit=list.size

        creatTab(list)
        setContentFrag(list)
    }

    private fun <F:BaseFragment<*>> setContentFrag(list:ArrayList<IPagerTabContent<F>>){
        pageList=list.map { it.frag }
        val fragList:List<F> =list.map { it.frag }

        viewPager.adapter.isNull {
            viewPager.adapter=ViewPagerFragAdapter<F>(fragManager,fragList)
        }.onNotNull {
            val adapter=viewPager.adapter as ViewPagerFragAdapter<F>
            adapter.refresh(fragList)
        }
    }

    private fun <F:BaseFragment<*>> creatTab(list: ArrayList<IPagerTabContent<F>>) {
        list.forEach {content->
            val newTab=pagerTab.newTab().also { tab->
                if (content.tab!=null){
                    val tabSet=content.tab!!
                    tab.customView=tabSet.view
                    if (content.tabBackgroundRes>0)
                        tabSet.setBackground(content.tabBackgroundRes)
                    if (content.icon>0)
                        tabSet.setIcon(content.icon)

                    tabSet.setText(content.title)

                    if (content.textColorSelector>0)
                        tabSet.setTextColor(content.textColorSelector)
                }else{
                    tab.text = content.title
                    if (content.icon>0)
                        tab.icon= drawable(content.icon)

                    if (content.tabBackgroundRes>0)
                        pagerTab.background= drawable(content.tabBackgroundRes)
                }
            }
            pagerTab.addTab(newTab)
        }
        setTabLayoutMatchParent(pagerTab)
    }
    private fun setTabLayoutMatchParent(tabLayout: TabLayout) {
        val tabLayoutChild = tabLayout.getChildAt(0) as ViewGroup
        val tabLen = tabLayoutChild.childCount

        for (j in 0 until tabLen) {
            val v = tabLayoutChild.getChildAt(j)
            v.setPadding(0, 0, 0, indicatorHeight)
        }
    }

    override fun setInflatView(): Int = R.layout.item_tab_viewpager
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null

    interface ITabSet{
        fun setText(text:String)
        fun setTextColor(@ColorRes res: Int)
        fun setIcon(@DrawableRes res:Int)
        fun setBackground(@DrawableRes res:Int)
        val view: View
    }

    class TabView(context: Context?) : RelativeCustomView(context),ITabSet{
        var parentView:RelativeLayout=itemView.findViewById(R.id.parentView)
        var imgView:ImageView=itemView.findViewById(R.id.imgView)
        var textView:TextView=itemView.findViewById(R.id.textView)


        override fun setInflatView(): Int =R.layout.item_tab_view
        override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
        override fun setText(text: String) {
            textView.text=text
        }

        override fun setTextColor(res: Int) {
            textView.setTextColor(colorStateList(res))
        }

        override fun setIcon(res: Int) {
            imgView.setImageResource(res)
        }

        override fun setBackground(res: Int) {
            parentView.background= drawable(res)
        }

        override val view: View
            get() = this
    }
}