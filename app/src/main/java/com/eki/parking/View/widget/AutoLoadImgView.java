package com.eki.parking.View.widget;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

import android.util.AttributeSet;

import com.eki.parking.R;
import com.hill.devlibs.util.StringUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by Hill on 2018/5/23.
 */
public class AutoLoadImgView extends AppCompatImageView {

    private Context mContext;

    public AutoLoadImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public AutoLoadImgView(Context context) {
        this(context, null);
    }

    public void loadUrl(String url) {
        loadUrl(url, false);
    }

    public void loadUrl(String url, boolean isFit) {
        if (!StringUtil.isEmptyString(url)) {
            RequestCreator creator = Picasso.with(mContext)
                    .load(url)
//                    .centerCrop()
                    .placeholder(R.drawable.none_img);
            if (isFit)
                creator.fit();//填滿
            creator.into(this);
        } else
            Picasso.with(mContext).load(R.drawable.none_img).into(this);
    }

    public void loadUrl(String url, int width, int height) {
        if (!StringUtil.isEmptyString(url))
            Picasso.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.none_img)
                    .resize(width, height)
                    .into(this);
        else
            Picasso.with(mContext).load(R.drawable.none_img).resize(width, height).into(this);
    }
}
