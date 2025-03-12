package com.eki.parking.View.recycleview.item;

import android.view.View;

import com.eki.parking.App;
import com.hill.devlibs.impl.AppFeature;
import com.hill.devlibs.recycleview.item.LibItemLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by Hill on 2017/4/5.
 */

public abstract class ItemLayout <VO> extends LibItemLayout<VO>
                                      implements AppFeature<App> {

    public interface ItemEvent<T>{
        void onItemSelect();
        void itemNoSelect();
        T value();
    }
    public @Nullable
    ItemEvent itemEvent;

    public ItemLayout(@NonNull View itemView) {
        super(itemView);
        if (this instanceof ItemEvent)
            itemEvent=(ItemEvent) this;

    }


    public void init(int lenght){
        super.init(lenght);
    }


    public void refresh(VO data){
        super.refresh(data);
    }


    public App getApp(){
        return App.getInstance();
    }

    @Override
    public int getScreenHeight() {
        return App.getScreenHeight();
    }

    @Override
    public int getScreenWidth() {
        return App.getScreenWidth();
    }
}
