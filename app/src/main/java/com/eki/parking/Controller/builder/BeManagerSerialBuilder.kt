package com.eki.parking.Controller.builder

import com.eki.parking.Controller.dialog.child.LandArticleFrag
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialFinish
import com.hill.devlibs.extension.notNull

/**
 * Created by Hill on 2020/04/20
 */
class BeManagerSerialBuilder:ISerialBuilder<ISerialDialog>(){

    interface OnFinish{
        fun onFinish()
    }
//    interface IBankOver{
//        var onBankOver:(BeManagerProcess)->Unit
//    }

    private var first:ISerialDialog=LandArticleFrag()


    var onFinish={}

//    private var managerProcess:BeManagerProcess?=null

//    private val onBank:(BeManagerProcess)->Unit={
//        Log.d("get bank body from frag")
//        managerProcess=it
//    }

    inline fun setFinishBack(l:OnFinish){
        onFinish={
            l.onFinish()
        }
    }

    override fun start(): ISerialDialog {
        first.setEvent(this)
        serialList.add(position,first)
//        addBody(first.frag)
        return first
    }

    override fun onNext() {
        serialList[position].next().notNull { next->
            position++
            next.setEvent(this)

//            addBody(next.frag)
            serialList.add(position,next)
            onSerialContent?.onNext(next)
//            if (next is IBankOver)
//                next.onBankOver=onBank

            if (next is ISerialFinish){
//                FinishDelegate(next)
                next.onSerialFinish=onFinish
            }

        }
    }

    override fun onPrevious() {
        if (position>0){
            serialList.removeAt(position)
            position--
            onSerialContent?.onPrevious(serialList[position])
        }
    }

//    private inner class FinishDelegate(f:ISerialFinish):ISerialFinish by f{
//        init {
//            f.checkSerialFinish={
//                Log.i("go be manager process ->$managerProcess")
//
//                managerProcess?.apply {
//                    var process=from?.showProgress()
//                    onSuccess={
//                        process?.dismiss()
//                        //這邊會去call加入地點
//                        startSerialFinish()
//                    }
//                    onFail={
//                        process?.dismiss()
//                        from?.showToast("成為地主失敗!")
//                    }
//                }?.run()
//
//                false
//            }
//        }
//    }
}