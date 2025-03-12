package com.eki.parking.Controller.activity.frag.SiteDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.SiteDetail.child.SiteBaseInfoFrag
import com.eki.parking.Controller.activity.frag.SiteDetail.child.SiteLocInfoFrag
import com.eki.parking.Controller.activity.frag.SiteDetail.child.SiteRestrictionFrag
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IPagerTabContent
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.pager.view.TablePagerView
import com.eki.parking.databinding.FragSiteDetailInfo2Binding
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/03/18
 */
class SiteDetailInfoFrag : SearchFrag(), ISetData<ManagerLocation>,IFragViewBinding {

    private lateinit var binding: FragSiteDetailInfo2Binding
    private lateinit var location: ManagerLocation

    var getEditBackLoc: (() -> ManagerLocation) = {

        //去取得各個子畫面裡面的設定資料
        editBasicInfo?.invoke(location)
        editPlaceInfo?.invoke(location)
        editRestrictionInfo?.invoke(location)

        location
    }

    private var editBasicInfo: ((ManagerLocation) -> Unit)? = null
    private var editPlaceInfo:((ManagerLocation)->Unit)?=null
    private var editRestrictionInfo:((ManagerLocation)->Unit)?=null

    override fun initFragView() {

        binding.tabPager.setSelectedTabIndicatorColor(getColor(R.color.Eki_green_2))

        var pageList = ArrayList<IPagerTabContent<BaseFragment<*>>>()
        pageList.add(getPageContent(getString(R.string.Basic_message),
                SiteBaseInfoFrag().also {
                    it.setData(location)
                    editBasicInfo=it.onEdit
                }))
        pageList.add(getPageContent(getString(R.string.Parking_place),
                SiteLocInfoFrag().also {
                    it.setData(location)
                    editPlaceInfo=it.onEdit
                }))
        pageList.add(getPageContent(getString(R.string.Parking_restrictions),
                SiteRestrictionFrag().also {
                    it.setData(location)
                    editRestrictionInfo=it.onEdit
                }))

        binding.tabPager.setPagerList(childFragmentManager, pageList)
    }

    private fun getPageContent(title: String, f: BaseFragment<*>) =
            object : IPagerTabContent<BaseFragment<*>>() {
                override val frag: BaseFragment<*>
                    get() = f
                override val icon: Int
                    get() = 0
                override val tabBackgroundRes: Int
                    get() = R.drawable.selector_tab_gray
                override val title: String
                    get() = title
                override val tab: TablePagerView.ITabSet?
                    get() = TablePagerView.TabView(context)
                override val textColorSelector: Int
                    get() = R.color.selector_text_color_green
            }

    override fun setData(data: ManagerLocation?) {
        data.notNull { location = ManagerLocation().apply { copyFrom(it) } }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragSiteDetailInfo2Binding.inflate(inflater,container,false)
        return binding
    }
}