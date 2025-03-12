package com.eki.parking.View.recycleview.item

import android.view.View
import android.widget.TextView
import com.eki.parking.Controller.activity.frag.Search.adaptor.SearchItemAdapter
import com.hill.devlibs.impl.ISearchableItem

/**
 * Created by Hill on 23,10,2019
 */
class SearchListItem(itemView: View) : ItemLayout<ISearchableItem>(itemView) {

    lateinit var itemSet: SearchItemAdapter.ItemSet

    private lateinit var contentText:TextView
    private lateinit var parentView:View
    override fun init(lenght: Int) {
        super.init(lenght)
        parentView=itemView.findViewById(itemSet.parentId)
        parentView.setOnClickListener {
            itemClick()
        }
        contentText=itemView.findViewById(itemSet.contentId)
    }

    override fun refresh(data: ISearchableItem?) {
        super.refresh(data)
        contentText.text=data?.searchContent()
    }
}