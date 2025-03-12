package com.eki.parking.Controller.activity.frag.Main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.intent.NotifyDetailIntent
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.NotifyType
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.NotifyBody
import com.eki.parking.Model.response.NotifyResponse
import com.eki.parking.R
import com.eki.parking.View.ViewType
import com.eki.parking.View.recycleview.adapter.ViewTypeAdaptor
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.FragNotifyBinding
import com.eki.parking.extension.sendRequest
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.startActivityAnim
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IRecycleViewModelSet
import com.hill.devlibs.tools.Log

class NotifyFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragNotifyBinding
    private var notifyList = ArrayList<NotifyItemData>()

    override fun initFragView() {
        binding.recycleView.useSimpleDivider()

        EkiRequest<NotifyBody>().also {
            it.body = NotifyBody()
        }.sendRequest(context, true, object : OnResponseListener<NotifyResponse> {
            override fun onReTry() {

            }

            override fun onFail(errorMsg: String, code: String) {

            }

            override fun onTaskPostExecute(result: NotifyResponse) {
                Log.d("info size->${result.info.size}")
                setUpList(result.info)
            }
        })

    }

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.Notify_option)
    }

    private fun setUpList(info: ArrayList<ResponseInfo.EkiNotify>) {
        var list = info.sortedByDescending { it.Type }
        list.forEach {
            when (it.type) {
                NotifyType.Server -> {
                    notifyList.add(NotifyItemData(ViewType.item, it))
                }
                NotifyType.Action -> {
                    notifyList.add(NotifyItemData(ViewType.item2, it))
                }
            }
        }

        binding.recycleView.adapter = NotifyAdaptor()
    }

    private inner class NotifyAdaptor :
        ViewTypeAdaptor<ResponseInfo.EkiNotify>(context) {
        override val modelList: ModelList<ResponseInfo.EkiNotify>
            get() = ModelList(notifyList)
        override val viewSets: SetList<ResponseInfo.EkiNotify>
            get() = SetList(object : ItemTypeSet<ResponseInfo.EkiNotify> {
                override val viewType: Int
                    get() = ViewType.item

                override fun itemBack(parent: ViewGroup): ItemLayout<ResponseInfo.EkiNotify> =
                    ServerNotifyItem(getItemView(R.layout.item_notify, parent)).also { it.init() }
            }, object : ItemTypeSet<ResponseInfo.EkiNotify> {
                override val viewType: Int
                    get() = ViewType.item2

                override fun itemBack(parent: ViewGroup): ItemLayout<ResponseInfo.EkiNotify> =
                    ActionNotifyItem(getItemView(R.layout.item_notify, parent)).also { it.init() }
            })


    }

    private data class NotifyItemData(
        override val viewType: Int,
        override val data: ResponseInfo.EkiNotify?
    ) : IRecycleViewModelSet<ResponseInfo.EkiNotify>

    private class ServerNotifyItem(itemView: View) : ItemLayout<ResponseInfo.EkiNotify>(itemView) {

        private var title = itemView.findViewById<TextView>(R.id.titleText)
        private var msg = itemView.findViewById<TextView>(R.id.msgText)
        private var img = itemView.findViewById<ImageView>(R.id.headerIcon)
        private var parentView = itemView.findViewById<View>(R.id.parentView)

        override fun init() {
            img.setImageResource(R.drawable.icon_server_notify)
            parentView.setOnClickListener {
                context.startActivityAnim(NotifyDetailIntent(context, itemData))
            }
        }

        override fun refresh(data: ResponseInfo.EkiNotify?) {
            super.refresh(data)
            data.notNull {

                var notify = it.Server
                title.text = notify?.Title
                msg.text = notify?.Msg
            }
        }
    }

    private class ActionNotifyItem(itemView: View) : ItemLayout<ResponseInfo.EkiNotify>(itemView) {

        private var title = itemView.findViewById<TextView>(R.id.titleText)
        private var msg = itemView.findViewById<TextView>(R.id.msgText)
        private var img = itemView.findViewById<ImageView>(R.id.headerIcon)
        private var parentView = itemView.findViewById<View>(R.id.parentView)

        override fun init() {
            img.setImageResource(R.drawable.icon_action_notify)
            parentView.setOnClickListener {
                context.startActivityAnim(NotifyDetailIntent(context, itemData))
            }
        }

        override fun refresh(data: ResponseInfo.EkiNotify?) {
            super.refresh(data)
            data.notNull {
                var action = it.Action
                title.text = getString(R.string.Event_information)
                msg.text = action?.Name
            }
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragNotifyBinding.inflate(inflater, container, false)
        return binding
    }
}