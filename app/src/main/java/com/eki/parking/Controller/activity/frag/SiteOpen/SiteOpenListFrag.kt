package com.eki.parking.Controller.activity.frag.SiteOpen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.dialog.EkiProgressDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.EditLocationProcess
import com.eki.parking.Controller.process.ManagerLocationProcess
import com.eki.parking.Model.request.body.EditLocationBody
import com.eki.parking.Model.response.EditLocationResponse
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.View.widget.AutoLoadImgView
import com.eki.parking.databinding.FragSiteOpenListBinding
import com.eki.parking.extension.show
import com.eki.parking.extension.showProgress
import com.eki.parking.extension.sqlDataListAsync
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/06/24
 */
class SiteOpenListFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragSiteOpenListBinding
    private val mLocList = ArrayList<ManagerLocation>()
    var onSelectLoc: ((ManagerLocation) -> Unit)? = null
    private var isFirst = true

    override fun initFragView() {
        binding.recycleView.useVerticalView()
        binding.refreshView.setOnRefreshListener {
            loadData()
        }
        loadData()
    }

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.Opening_hours)
        if (!isFirst)
            loadDataFromSql()
        else
            isFirst = false
    }

    private fun loadDataFromSql() {
        mLocList.clear()
        sqlDataListAsync<ManagerLocation> { list ->
            mLocList.addAll(list)

            binding.recycleView.adapter = LocOpenListAdaptor()
        }
    }

    private fun loadData() {

        var now = DateTime()

        binding.refreshView?.isRefreshing = true
        //重新下載
        object : ManagerLocationProcess(context, now, isClear = true) {}.also {
            it.onLocation = { list ->
//                progress?.dismiss()
                binding.refreshView?.isRefreshing = false
                loadDataFromSql()
            }
            it.retryProcess = {
                loadData()
            }
        }.run()
    }

    private inner class LocOpenListAdaptor : BaseAdapter<LocOpenItem>(context) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocOpenItem =
            LocOpenItem(getItemView(R.layout.item_loc_open_switcher, parent))
                .also { it.init() }

        override fun onBindViewHolder(item: LocOpenItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(mLocList[position])
        }

        override fun getItemCount(): Int = mLocList.size
    }

    private inner class LocOpenItem(itemView: View) : ItemLayout<ManagerLocation>(itemView) {

        private var img = itemView.findViewById<AutoLoadImgView>(R.id.imgView)
        private var serial = itemView.findViewById<TextView>(R.id.serialText)
        private var siteName = itemView.findViewById<TextView>(R.id.siteName)
        private var address = itemView.findViewById<TextView>(R.id.addressText)
        private var switcher = itemView.findViewById<SwitchCompat>(R.id.switcher)
        private var parentView = itemView.findViewById<View>(R.id.parentView)

        override fun init() {
            parentView.setOnClickListener {
//                Log.w("Item click")
                itemData.notNull {
                    onSelectLoc?.invoke(it)
                }
            }
            switcher.setOnCheckedChangeListener { buttonView, isChecked ->
//                Log.w("Switcher checked->$isChecked")
                itemData.notNull { loc ->
                    if (loc.Config?.beEnable != isChecked) {
                        EkiMsgDialog().also {
                            it.msg = when (isChecked) {
                                true -> getString(R.string.Are_you_sure_want_to_open_this_position).messageFormat(
                                    loc.Info?.Content ?: ""
                                )
                                false -> getString(R.string.Are_you_sure_want_to_close_this_position).messageFormat(
                                    loc.Info?.Content ?: ""
                                )
                            }
                            it.determinClick = {
//                                Log.i("DeterminClick")
                                LocationEnableProcess(
                                    context,
                                    loc.toData().apply { config.beEnable = isChecked }).apply {
                                    onSuccess = {
                                        loc.Config?.beEnable = isChecked
                                        loc.sqlSaveOrUpdate()

                                    }
                                    onFail = {
                                        switcher.isChecked = loc.Config?.beEnable ?: false
                                    }
                                }.run()
                            }
                            it.cancelClick = {
                                switcher.isChecked = itemData?.Config?.beEnable ?: false
                            }
                        }.show(childFragmentManager)
                    }
                }
            }
        }

        override fun refresh(data: ManagerLocation?) {
            super.refresh(data)
            data.notNull { loc ->

                img.loadUrl(loc.imgUrl(), true)
                serial.text = loc.Info?.SerialNumber
                siteName.text = loc.Info?.Content
                address.text = loc.Address?.shortName
                switcher.isChecked = loc.Config?.beEnable ?: false
            }
        }
    }

    private class LocationEnableProcess(from: Context?, private var data: EditLocationBody) :
        EditLocationProcess(from) {
        var onSuccess = {}
        var onFail = {}

        override val body: EditLocationBody
            get() = data
        override val onResponse: OnResponseListener<EditLocationResponse>?
            get() = object : OnResponseListener<EditLocationResponse> {
                override fun onReTry() {
                    progress?.dismiss()
                }

                override fun onFail(errorMsg: String, code: String) {
                    progress?.dismiss()
                    onFail()
                }

                override fun onTaskPostExecute(result: EditLocationResponse) {
                    progress?.dismiss()
                    onSuccess()
                }
            }

        var progress: EkiProgressDialog? = null
        override fun run() {
            progress = from?.showProgress(ProgressMode.PROCESSING_MODE)
            super.run()
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragSiteOpenListBinding.inflate(inflater, container, false)
        return binding
    }
}