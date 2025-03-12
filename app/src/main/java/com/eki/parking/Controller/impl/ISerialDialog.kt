package com.eki.parking.Controller.impl

import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.hill.devlibs.impl.ISetEvent

/**
 * Created by Hill on 2020/04/13
 */
interface ISerialDialog:ISetEvent<ISerialEvent>,INext<ISerialDialog?>{
    val frag:DialogChildFrag<*>
    val title:String

    interface TitleSet{
        val titleVisible:Boolean
    }
}