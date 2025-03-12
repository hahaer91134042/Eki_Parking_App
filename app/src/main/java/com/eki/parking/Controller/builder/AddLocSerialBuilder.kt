package com.eki.parking.Controller.builder

import com.eki.parking.Controller.dialog.child.AddLocStep1Frag
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialFinish
import com.eki.parking.Model.request.body.AddLocationBody
import com.hill.devlibs.extension.notNull

/**
 * Created by Hill on 2020/04/13
 */
class AddLocSerialBuilder:ISerialBuilder<ISerialDialog>(){

    companion object {
        const val car = 0
        const val scooter = 2
    }

    var body=AddLocationBody()
    var type = 0

    var onFinish={}

    override fun start():ISerialDialog{
        val first:ISerialDialog=AddLocStep1Frag(body,type)
        first.setEvent(this)
        serialList.add(position,first)
        return first
    }

    override fun onNext() {
        serialList[position].next().notNull { next->
            position++
            next.setEvent(this)

            serialList.add(position,next)
            onSerialContent?.onNext(next)
            if (next is ISerialFinish)
                next.onSerialFinish=onFinish
        }
    }

    override fun onPrevious() {
        if (position>0){
            serialList.removeAt(position)
            position--
            onSerialContent?.onPrevious(serialList[position])
        }else if(position == 0){
            serialList.clear()
        }
    }

    fun getBody(oldBody: AddLocationBody){

        body = oldBody

    }
}