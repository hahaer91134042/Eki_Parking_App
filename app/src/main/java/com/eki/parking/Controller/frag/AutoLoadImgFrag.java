package com.eki.parking.Controller.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.eki.parking.R;
import com.eki.parking.View.widget.AutoLoadImgView;
import com.hill.devlibs.impl.IFragViewRes;

//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;

public class AutoLoadImgFrag extends BaseFragment implements IFragViewRes {
//    @BindView(R.id.imgView)
    AutoLoadImgView mImgView;
//    Unbinder unbinder;

    private String url;
    private boolean isFit=false;

    @Override
    public int setFragViewRes() {
        return R.layout.item_autoloadimgview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        mImgView=getView().findViewById(R.id.imgView);

        mImgView.loadUrl(url,isFit);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    public void setImgUrl(String url) {
        this.url=url;
    }
    public void setImgUrl(String url,boolean isFit) {
        this.url=url;
        this.isFit=isFit;
    }
}
