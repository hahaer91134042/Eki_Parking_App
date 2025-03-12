package com.eki.parking.Controller.activity.frag.SiteDetail.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Model.EnumClass.SitePosition
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.pager.view.TablePagerView
import com.eki.parking.databinding.FragSiteLocInfoBinding
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/07/10
 */
class SiteLocInfoFrag : BaseFragment<SiteLocInfoFrag>(),
    TablePagerView.OnSelectPageEvent,
    ISetData<ManagerLocation>, IFragViewBinding {

    private lateinit var binding: FragSiteLocInfoBinding
    private var loc: ManagerLocation? = null

    var onEdit: (ManagerLocation) -> Unit = {
        it.Info?.sitePosition = when (binding.positionSelector.checkedRadioButtonId) {
            R.id.inside -> SitePosition.InSide
            else -> SitePosition.OutSide
        }
    }

    override fun initFragView() {
        loc.notNull { mLoc ->
            binding.addressText.text = mLoc.Address?.fullName
        }
    }

    override fun setData(data: ManagerLocation?) {
        loc = data
    }

    override fun onSelectPage() {
        loc.notNull { mLoc ->
            binding.positionSelector.check(
                when (mLoc.Info?.sitePosition) {
                    SitePosition.InSide -> R.id.inside
                    else -> R.id.outside
                }
            )
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragSiteLocInfoBinding.inflate(inflater,container,false)
        return binding
    }
}