package com.hill.devlibs.recycleview.item;

import android.view.View;

import com.hill.devlibs.BaseApp;

/**
 * Created by Hill on 2018/10/26.
 */
@Deprecated
public abstract class ChannelLayout<Channel,VO,APP extends BaseApp> extends LibItemLayout<VO> {
    public Channel channel;
    public ChannelLayout(View itemView) {
        super(itemView);
    }
}
