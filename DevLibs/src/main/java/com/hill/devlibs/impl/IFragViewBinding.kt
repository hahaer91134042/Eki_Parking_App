package com.hill.devlibs.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Created by Hill on 2022/02/15
 */
interface IFragViewBinding {
    fun fragViewBinding(inflater:LayoutInflater,container:ViewGroup):ViewBinding
}