package com.eki.parking.Controller.activity.frag.Search


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.frag.Search.adaptor.SearchItemAdapter
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnSelectListener
import com.eki.parking.Model.sql.SearchInfo
import com.eki.parking.R
import com.eki.parking.databinding.ItemRefreshRecycleviewBinding
import com.eki.parking.extension.addInfo
import com.eki.parking.extension.sqlDataList
import com.eki.parking.extension.sqlSaveAsync
import com.hill.devlibs.extension.isTrue
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.model.ValueObjContainer
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 22,10,2019
 */
class SearchListFrag: SearchFrag(),IFragViewBinding {

    private lateinit var binding: ItemRefreshRecycleviewBinding

    override fun onSearch(text: String) {
        Log.w("$TAG onSearch->$text")
        adapter?.onSearch(text)
    }

    override fun onSearchStart(text: String) {
        Log.i("$TAG onSearchStart->$text")
        if (text.isNullOrEmpty()){
            showToast(getString(R.string.Please_enter_a_search_address))
            return
        }

        var info=SearchInfo().apply {
            //            Id=infoList[infoList.size-1].Id+1
            Content=text
            craetTime= DateTime()
        }
        refreshData(info)
        sendBroadcast(Intent(AppFlag.GoSearch).apply {
            putExtra(AppFlag.DATA_FLAG,ValueObjContainer<SearchInfo>().apply {
                setValueObjData(info)
            })
        })
        toMainActivity()
    }

    private fun refreshData(info: SearchInfo) {
        infoList.addInfo(info).isTrue {
            infoList.sortByDescending { it.craetTime.toStamp() }
            sqlSaveAsync(infoList)
            adapter?.refresh(infoList)
        }
    }

    private lateinit var infoList:ArrayList<SearchInfo>
    private var adapter: SearchItemAdapter<SearchInfo>?=null

    override fun initFragView() {
        infoList= sqlDataList()
        infoList.sortByDescending { it.craetTime.toStamp() }

        adapter= SearchItemAdapter(context,
                infoList,
                SearchItemAdapter.ItemSet().apply {
                    layoutId = R.layout.item_search_group_type_1
                    parentId = R.id.parentView
                    contentId = R.id.searchText
                }).apply {

            selectListener=object :OnSelectListener<String>{
                override fun onSelect(data: String) {
                    setToolBarActionViewText(data)
                }
            }
        }
        binding.recycleView.adapter=adapter
        binding.recycleView.useSimpleDivider()
    }

    override fun onDestroyView() {
        closeKeyBoard()
        super.onDestroyView()
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = ItemRefreshRecycleviewBinding.inflate(inflater,container,false)
        return binding
    }
}