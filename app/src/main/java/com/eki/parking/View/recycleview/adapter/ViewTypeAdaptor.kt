package com.eki.parking.View.recycleview.adapter

import android.content.Context
import android.view.ViewGroup
import com.eki.parking.View.recycleview.item.ItemLayout
import com.hill.devlibs.impl.IRecycleViewModelSet
import com.hill.devlibs.recycleview.model.RecycleViewModel
import com.hill.devlibs.tools.Log
import java.lang.NullPointerException
import kotlin.collections.ArrayList

/**
 * Created by Hill on 2020/01/16
 */
abstract class ViewTypeAdaptor<M>(context: Context?):
        BaseAdapter<ItemLayout<M>>(context) {

    class SetList<T>(vararg sets:ItemTypeSet<T>):ArrayList<ItemTypeSet<T>>(){
        init {
            sets.forEach { append(it) }
        }
        fun append(set:ItemTypeSet<T>): SetList<T> {
            add(set)
            return this
        }
    }
    class ModelList<T>():ArrayList<IRecycleViewModelSet<T>>(){
        constructor(list:List<IRecycleViewModelSet<T>>):this(){
//            list.forEach { append(RecycleViewModel(it.viewType,it.data)) }
            addAll(list)
        }
        constructor(vararg models:RecycleViewModel<T>):this(){
            addAll(models)
        }

        fun append(model:RecycleViewModel<T>):ModelList<T>{
            add(model)
            return this
        }
        fun append(model:IRecycleViewModelSet<T>):ModelList<T>{
            add(RecycleViewModel(model.viewType,model.data))
            return this
        }
    }
    interface ItemTypeSet<T>{
        val viewType:Int
        fun itemBack(parent:ViewGroup):ItemLayout<T>
    }

    protected val itemList=ArrayList<ItemLayout<M>>()
    protected val dataList:ModelList<M> by lazy { modelList }
    private val setList:SetList<M> by lazy { viewSets }

    protected abstract val modelList:ModelList<M>
    protected abstract val viewSets:SetList<M>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemLayout<M> {

//        Log.e("onCreateViewHolder dataList size->${dataList.size}")

        if (!setList.any { it.viewType==viewType })
            throw NullPointerException("View Type Not Exsist!")

        val set=setList.first { it.viewType==viewType }
//        Log.w("filter typeSet->${set.viewType}")

        val item=set.itemBack(parent)
        itemList.add(item)
        return item
    }

    override fun onBindViewHolder(item: ItemLayout<M>, position: Int) {
        super.onBindViewHolder(item, position)
        item.refresh(dataList[position].data)
    }

    override fun getItemViewType(position: Int): Int =dataList[position].viewType
    override fun getItemCount(): Int=dataList.size
}