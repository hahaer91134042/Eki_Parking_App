package com.hill.devlibs.impl

import com.hill.devlibs.model.ValueObjContainer
import com.hill.devlibs.model.ValueObject

/**
 * Created by Hill on 2020/06/20
 */
interface IValueObjContainer<VO:ValueObject>{
    fun getContainer(): ValueObjContainer<VO>
}