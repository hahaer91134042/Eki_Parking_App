package com.eki.parking.Controller.filter

import com.eki.parking.Controller.impl.IFilterSet
import com.eki.parking.Model.DTO.OpenSet
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/07/08
 */
class TimeOverlapFilter(private var from:DateTime,private var list:List<OpenSet>):IFilterSet.Filter<OpenSet> {
    override fun filter(data: OpenSet): Boolean {
//        Log.d("--filter date open--")
//        data.printValue()
        var start=data.startDateTime(from)
        var end=data.endDateTime(from)
//        Log.w("start->$start stamp->${start.toStamp()} end->$end stamp->${end.toStamp()}")

        var dayOpen=list.filter { it.startDateTime(from).date==start.date }
        var overlap=dayOpen
                .filter {
                    var setStart=it.startDateTime(from)
                    var setEnd=it.endDateTime(from)
                    var isStartEqual=setStart==start
                    var isEndEqual=setEnd==end
//                    Log.i("TimeOverlapFilter time equal s->$setStart e->$setEnd  equal->${isStartEqual&&isEndEqual} ")
//                    Log.w("equal start->$isStartEqual end->$isEndEqual")

                    !(isStartEqual&&isEndEqual)
                }//過濾掉依樣的
                .filter {
                    var setStart=it.startDateTime(from)
                    var setEnd=it.endDateTime(from)
                    var isStartOverlap=setStart<start && start<setEnd
                    var isEndOverlap=setStart<end && end<setEnd
//                    Log.d("TimeOverlapFilter time overlap s->$setStart e->$setEnd  startOverlap->$isStartOverlap endOverlap->$isEndOverlap  result->${!(isStartOverlap||isEndOverlap)}")

                    !(isStartOverlap||isEndOverlap)
                }//先過濾掉已開放時段內 已經包含時間的
                .filter {
                    var setStart=it.startDateTime(from)
                    var setEnd=it.endDateTime(from)
                    var isStartInclude=start<setStart
                    var isEndInclude=end>setEnd
//                    Log.e("TimeOverlapFilter time include s->$setStart e->$setEnd  startInclude->$isStartInclude endInclude->$isEndInclude result->${!(isStartInclude&&isEndInclude)}")

                    !(isStartInclude&&isEndInclude)
                }//在過濾掉包在想開放時間內的已開放時段

//        Log.w("TimeOverlapFilter  overlap size->${overlap.size}")
        //假如數量都依樣 表示這是可以開啟的時段(不一樣就是上面有些條件 被過濾掉)
        return overlap.size==dayOpen.size
    }
}