package com.eki.parking.Controller.tools

import com.eki.parking.Controller.impl.IMultiOverCheck
import com.eki.parking.Controller.process.BaseProcess

/**
 * Created by Hill on 2020/02/20
 */
class ProcessExecutor(): Runnable {

    interface ExecuteBack{
        fun onAllOver()
    }

    var process=ArrayList<BaseProcess>()
    private var isOverList=ArrayList<IMultiOverCheck>()
    var onAllProcessOver: ExecuteBack?=null

    constructor(vararg process: BaseProcess):this(){
        this.process.addAll(process)
    }
    constructor(process:List<BaseProcess>):this(){
        this.process.addAll(process)
    }

    fun setOnAllOverBack(b:()->Unit){
        onAllProcessOver=object :ExecuteBack{
            override fun onAllOver()=b()
        }
    }

    fun add(vararg array: BaseProcess){
        process.addAll(array)
    }

    override fun run() {
        process.forEach {p->
            isOverList.add(p)
            p.onCheckAll={

                if (isOverList.none { !it.isCheckOk }){
                    onAllProcessOver?.onAllOver()
                }
            }

            p.run()
        }
    }

}