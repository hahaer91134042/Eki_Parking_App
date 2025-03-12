package com.eki.parking.View.recycleview;

import android.content.Context;
import android.util.AttributeSet;

import com.eki.parking.App;
import com.hill.devlibs.recycleview.LibBaseRecycleView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by Hill on 2017/12/1.
 */
public class BaseRecycleView extends LibBaseRecycleView<App> {


    public BaseRecycleView(Context context) {
        this(context,null);
    }

    public BaseRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void useHorizonView(){
        setLayoutManager(getHorizontalManager());
        setHasFixedSize(true);
    }

    private LinearLayoutManager getHorizontalManager(){
        LinearLayoutManager lm=new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        lm.setAutoMeasureEnabled(true);
        return lm;
    }

    @Override
    protected int getScreenWidth() {
        return App.getScreenWidth();
    }

    @Override
    protected int getScreenHeight() {
        return App.getScreenHeight();
    }

    @Override
    protected App getApp() {
        return App.getInstance();
    }
}
