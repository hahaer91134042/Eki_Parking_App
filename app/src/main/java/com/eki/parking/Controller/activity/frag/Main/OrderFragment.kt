package com.eki.parking.Controller.activity.frag.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.Main.child.ReserveCalendarFragment
import com.eki.parking.Controller.activity.frag.Main.child.UnpaidFragment
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IPagerTabContent
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.View.pager.view.TablePagerView
import com.eki.parking.databinding.FragmentOrderBinding
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

class OrderFragment : SearchFrag(), ISetData<ArrayList<EkiOrder>>, IFragViewBinding {

    private lateinit var binding: FragmentOrderBinding
    private val unpaidFragment = UnpaidFragment()
    private val reserveCalendarFragment = ReserveCalendarFragment()

    var toReserveDetail2: ((EkiOrder) -> Unit)? = null

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.order)
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        return binding
    }

    override fun initFragView() {
        binding.orderTabPager.setSelectedTabIndicatorColor(getColor(R.color.dark_gray1))
        binding.orderTabPager.setSelectedTabIndicator(R.drawable.line_dark_gray1)

        val pageList = ArrayList<IPagerTabContent<BaseFragment<*>>>()
        pageList.add(
            getPageContent(getString(R.string.Unpaid) + "/" + getString(R.string.Go_checkout),
                unpaidFragment)
        )
        pageList.add(
            getPageContent(getString(R.string.reserve),
                reserveCalendarFragment.apply {
                    this.toReserveDetail = { order ->
                        toReserveDetail2?.invoke(order)
                    }
                })
        )

        binding.orderTabPager.setPagerList(childFragmentManager, pageList)
    }

    override fun setData(data: ArrayList<EkiOrder>?) {
        data?.let {

            val pageList = ArrayList<IPagerTabContent<BaseFragment<*>>>()
            pageList.add(
                getPageContent(getString(R.string.Unpaid) + "/" + getString(R.string.Go_checkout),
                    unpaidFragment)
            )
            unpaidFragment.setData(data)
            pageList.add(
                getPageContent(getString(R.string.reserve),
                    reserveCalendarFragment)
            )
            reserveCalendarFragment.setData(data)

            binding.orderTabPager.setPagerList(childFragmentManager, pageList)
        }
    }



    private fun getPageContent(title: String, f: BaseFragment<*>) =
        object : IPagerTabContent<BaseFragment<*>>() {
            override val frag: BaseFragment<*>
                get() = f
            override val icon: Int
                get() = 0
            override val tabBackgroundRes: Int
                get() = R.drawable.selector_tab_white
            override val title: String
                get() = title
            override val tab: TablePagerView.ITabSet
                get() = TablePagerView.TabView(context)
            override val textColorSelector: Int
                get() = R.color.dark_gray1
        }
}