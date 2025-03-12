package com.eki.parking.Controller.activity.frag.ParkingSite

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.frag.ParkingSite.adaptor.SiteListAdaptor
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IVisibleSet
import com.eki.parking.Controller.process.ManagerLocationProcess
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.databinding.FragParkingSiteBinding
import com.eki.parking.extension.sqlDataListAsync
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/02/21
 */
class ParkingSiteFrag : SearchFrag(), IVisibleSet, IFragViewBinding {

    private lateinit var binding: FragParkingSiteBinding
    private var mLocList = ArrayList<ManagerLocation>()

    override val visible: Boolean
        get() = true

    override fun initFragView() {
        super.initFragView()
        toolBarTitle = getString(R.string.Parking_Site_Setting)
        initNotZero()
        setRefresh()
        data()
        setBtn()
    }

    private fun initNotZero() {
        registerReceiver(AppFlag.UpdateView)
        binding.recycleView.useSimpleDivider()

    }

    private fun setRefresh() {
        binding.refreshView.setSize(Int.MIN_VALUE)
        binding.refreshView.setProgressViewOffset(false, 0,(binding.refreshView.resources.displayMetrics.heightPixels*0.35).toInt())
        binding.refreshView.setColorSchemeResources(R.color.Eki_green_2, R.color.Eki_green_2, R.color.Eki_green_2)
        binding.refreshView.setOnRefreshListener {
            binding.refreshView.isRefreshing = false
            data()
        }
    }

    private fun data() {
        binding.progressLayout.visibility = View.VISIBLE
        mLocList.clear()
        //下載地主車位
        val dateTime = DateTime()

        //重新下載
        object : ManagerLocationProcess(context, dateTime, true) {}.also {
            it.onLocation = { list ->
                binding.progressLayout.visibility = View.GONE
                mLocList.addAll(list)
                binding.recycleView.adapter = mLocList.let { it1 ->
                    SiteListAdaptor(
                        context,
                        it1,
                        childFragmentManager
                    )
                }

            }
            it.retryProcess = {
                data()
            }
            it.onFail = {
                binding.progressLayout.visibility = View.GONE
            }
        }.run()
    }

    private fun setBtn() {
        binding.relativeLayout.setOnClickListener {
            context?.sendBroadcast(Intent(AppFlag.SteamLocomotive))
        }
    }

    private fun getDataFromSql() {
        binding.progressLayout.visibility = View.VISIBLE

        mLocList.clear()

        sqlDataListAsync<ManagerLocation> { list ->
            mLocList.addAll(list)
            binding.recycleView.adapter = mLocList.let {
                SiteListAdaptor(
                    context,
                    it,
                    fragManager = childFragmentManager
                )
            }

        }
        binding.progressLayout.visibility = View.GONE
    }

    override fun onCatchReceive(action: String?, intent: Intent?) {
        when (action) {
            AppFlag.UpdateView -> {
                getDataFromSql()
            }
        }
    }

    override fun onBackPress() {
        context?.sendBroadcast(Intent(AppFlag.IsBackToMain))
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragParkingSiteBinding.inflate(inflater, container, false)
        return binding
    }
}