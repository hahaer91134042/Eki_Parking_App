package com.eki.parking.Controller.tools

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class AddSetLinearRv {
    fun setLinearRv(recyclerView: RecyclerView, context: Context): RecyclerView {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        return recyclerView
    }
}