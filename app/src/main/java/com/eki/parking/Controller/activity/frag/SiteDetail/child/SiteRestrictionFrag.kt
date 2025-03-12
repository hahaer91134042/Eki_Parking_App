package com.eki.parking.Controller.activity.frag.SiteDetail.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.View.pager.view.TablePagerView
import com.eki.parking.databinding.FragSiteRestrictionBinding
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/07/10
 */
class SiteRestrictionFrag : BaseFragment<SiteRestrictionFrag>(),
    ISetData<ManagerLocation>,
    TablePagerView.OnSelectPageEvent, IFragViewBinding {

    private lateinit var binding: FragSiteRestrictionBinding
    private lateinit var loc: ManagerLocation

    var onEdit: (ManagerLocation) -> Unit = {

        binding.siteRestricView.getEditLocInfo(it)
        //備註
        it.Config?.Text = binding.remarkText.text.toString().cleanTex

    }

    override fun initFragView() {
        binding.siteRestricView.creatFrom(loc)
        binding.remarkText.setText(loc.Config?.Text)
    }

    override fun setData(data: ManagerLocation?) {
        data.notNull { loc = it }
    }

    //因為radioButton會導致設定失效 所以在show出頁面的時候在即時設定
    override fun onSelectPage() {
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragSiteRestrictionBinding.inflate(inflater, container, false)
        return binding
    }
}