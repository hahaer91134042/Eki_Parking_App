package com.eki.parking.Controller.tools

import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.EnumClass.MarkerIcon
import com.eki.parking.Model.EnumClass.SiteSize
import com.eki.parking.Model.sql.EkiLocation

/**
 * Created by Hill on 2021/12/06
 * 因為找出相對應的marker需要充電樁的電流跟車位大小來判斷
 */
class FindMarker {
    //LinkedHashMap<SiteSize,HashMap<CurrentEnum,MarkerIcon>>().apply {  }
    companion object{
        private val markerMap= listOf(
            object :CurrentMarkerMap(){
                override val current: CurrentEnum
                    get() = CurrentEnum.NONE
                override val sizeMap: List<SizeMarkerMap>
                    get() = listOf(None_Motor(), None_Car())
            },
            object :CurrentMarkerMap(){
                override val current: CurrentEnum
                    get() = CurrentEnum.AC
                override val sizeMap: List<SizeMarkerMap>
                    get() = listOf(AC_Motor(),AC_Car())
            },
            object :CurrentMarkerMap(){
                override val current: CurrentEnum
                    get() = CurrentEnum.DC
                override val sizeMap: List<SizeMarkerMap>
                    get() = listOf(DC_Motor(),DC_Car())
            }
        )

        fun fromLoc(loc:EkiLocation):MarkerIcon{
            return when{
                loc.Socket.size<1->{
                    markerMap.first{it.current==CurrentEnum.NONE}
                        .sizeMap.first{ it.size.any {s->s==loc.Info?.siteSize } }
                        .marker
                }
                else->{
                    markerMap.first{it.current==loc.current}
                        .sizeMap.first{ it.size.any {s->s==loc.Info?.siteSize } }
                        .marker
                }
            }
        }
    }


    private class None_Motor:SizeMarkerMap(){
        override val size: List<SiteSize>
            get() = listOf(SiteSize.Motor)
        override val marker: MarkerIcon
            get() = MarkerIcon.NoneLocomotive
    }
    private class None_Car:SizeMarkerMap(){
        override val size: List<SiteSize>
            get() = listOf(SiteSize.Small,SiteSize.Standar)
        override val marker: MarkerIcon
            get() = MarkerIcon.NoneCar
    }
    private class AC_Motor:SizeMarkerMap(){
        override val size: List<SiteSize>
            get() = listOf(SiteSize.Motor)
        override val marker: MarkerIcon
            get() = MarkerIcon.AcLocomotive
    }
    private class AC_Car:SizeMarkerMap(){
        override val size: List<SiteSize>
            get() = listOf(SiteSize.Small,SiteSize.Standar)
        override val marker: MarkerIcon
            get() = MarkerIcon.AcCar
    }
    private class DC_Motor:SizeMarkerMap(){
        override val size: List<SiteSize>
            get() = listOf(SiteSize.Motor)
        override val marker: MarkerIcon
            get() = MarkerIcon.DcLocomotive
    }
    private class DC_Car:SizeMarkerMap(){
        override val size: List<SiteSize>
            get() = listOf(SiteSize.Small,SiteSize.Standar)
        override val marker: MarkerIcon
            get() = MarkerIcon.DcCar
    }

    private abstract class SizeMarkerMap{
        abstract val size:List<SiteSize>
        abstract val marker:MarkerIcon
    }
    private abstract class CurrentMarkerMap{
        abstract val current:CurrentEnum
        abstract val sizeMap:List<SizeMarkerMap>
    }
}