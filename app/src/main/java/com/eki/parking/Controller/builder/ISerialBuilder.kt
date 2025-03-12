package com.eki.parking.Controller.builder

import com.eki.parking.Controller.impl.IOnSerialContent
import com.eki.parking.Controller.impl.ISerialEvent
import java.io.Serializable

/**
 * Created by Hill on 2020/04/13
 */
abstract class ISerialBuilder<T>:ISerialEvent,Serializable{
    var position=0
    var onSerialContent: IOnSerialContent<T>?=null
    var serialList=ArrayList<T>()
    abstract fun start():T
}