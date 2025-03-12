package com.eki.parking.Controller.frag;

import android.content.Intent;
import android.os.Parcelable;

import com.eki.parking.AppFlag;
import com.eki.parking.Model.EnumClass.ClassLevel;
import com.hill.devlibs.impl.IValueObjContainer;
import com.hill.devlibs.model.ValueObjContainer;
import com.hill.devlibs.model.ValueObject;


import java.util.ArrayList;
import java.util.List;

public abstract class ChildFrag extends SearchFrag {

//    public void toMoreOrDetailActivity(FragDataContainer container){
//
//        Intent intent=container.intent;
//        intent.setClass(getActivity(), MoreProductAndDetailActivity.class);
//        getApp().getTopStackActivity().startActivitySwitchAnim(intent);
//
//    }


    protected class FragDataContainer<VO extends ValueObject>{

        public ClassLevel mClassLevel;
        private List<VO> voList;
        private VO data;
        public Intent intent=new Intent();

        public FragDataContainer(ClassLevel level) {
            mClassLevel =level;
            intent.putExtra(AppFlag.WHICH_FLAG,level.toString());
        }

        public FragDataContainer setListData(List<VO> list){
            voList=list;
            intent.putParcelableArrayListExtra(
                    AppFlag.DATA_FLAG,
                    getListParcelable()
            );
            return this;
        }

        public FragDataContainer setItemData(VO data) {
            this.data=data;
            intent.putExtra(
                    AppFlag.DATA_FLAG,
                    getDataParcelable()
            );
            return this;
        }
//        public void setMainCatalogId(int id) {
//            intent.putExtra(AppFlag.DATA_FLAG, id);
//        }

        private ValueObjContainer<VO> getDataParcelable() {
            if (data!=null && data instanceof IValueObjContainer){
                return ((IValueObjContainer)data).getContainer();
            }
            return null;
        }

        private ArrayList<? extends Parcelable> getListParcelable() {
            if (voList!=null){
                ArrayList<ValueObjContainer<VO>> list=new ArrayList<>();
                for (VO vo:
                        voList) {
                    if (vo instanceof IValueObjContainer)
                        list.add(((IValueObjContainer)vo).getContainer());
                }
                return list;
            }
            return new ArrayList<>();
        }
    }
}
