package com.eki.parking.Controller.activity.frag.Search.adaptor

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.eki.parking.Controller.listener.OnSelectListener
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.SearchListItem
import com.hill.devlibs.impl.ISearchableItem
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 23,10,2019
 */
class SearchItemAdapter<T:ISearchableItem>(context: Context?,
                        var inputList:ArrayList<T>,
                        var set: ItemSet) : BaseAdapter<SearchListItem>(context) {

    class ItemSet{
        @LayoutRes var layoutId:Int=0
        @IdRes var parentId:Int=0
        @IdRes var contentId:Int=0
    }
    private var searchList=ArrayList<ISearchableItem>()
    var selectListener:OnSelectListener<String>?=null

    init {
        searchList.addAll(inputList)
        setItemListClickListener {
            selectListener?.onSelect(searchList[it].searchContent())
        }
    }

    fun refresh(list:ArrayList<T>){
        searchList.clear()
        list.mapTo(searchList,{it})
        Log.w("refresh  searchList size->${searchList.size} content->$searchList")
        notifyDataSetChanged()
    }

    fun onSearch(text:String){
        var list= inputList.filter { it.searchContent().contains(text) }
        searchList.clear()
        searchList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListItem {
        var view=getItemView(set.layoutId,parent)
        return SearchListItem(view).apply {
            itemSet=set
            init(width*2/10)
        }
    }

    override fun getItemCount(): Int =searchList.size

    override fun onBindViewHolder(item: SearchListItem, position: Int) {
        super.onBindViewHolder(item, position)
        item.refresh(searchList[position])
    }
}