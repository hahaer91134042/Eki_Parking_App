package com.hill.devlibs.tools


/**
 * Created by Hill on 2020/01/06
 */
abstract class InputCheck {
    abstract class CallBack{
//        var delegate:()->Boolean={false}
        abstract fun check():Boolean
        open fun onFalse(){}
        open fun onTrue(){}
    }
    private var checkList=ArrayList<CallBack>()
//    private val isEven: (Boolean) -> Boolean = { !it }

    fun add(back:CallBack){
        checkList.add(back)
    }

    fun start(){
        var bList=ArrayList<Boolean>()
        checkList.forEach {
            var b=it.check()

            if (b) it.onTrue()
            else it.onFalse()

            bList.add(b)
        }
        Log.d("bList->$bList")
        //有一個false的話
        if (bList.any{ !it }){
            whenCheckFalse()
        }else{
            whenAllTrue()
        }
    }

    abstract fun whenAllTrue()
    abstract fun whenCheckFalse()
}