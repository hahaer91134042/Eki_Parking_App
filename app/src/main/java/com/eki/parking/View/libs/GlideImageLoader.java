package com.eki.parking.View.libs;

import android.content.Context;
import android.widget.ImageView;

import com.eki.parking.R;
import com.hill.devlibs.util.StringUtil;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by markyaoyao on 2018/1/18.
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        String url=(String)path;
        if (!StringUtil.isEmptyString(url))
            Picasso.with(context).load(url).placeholder(R.drawable.none_img).into(imageView);
        else
            Picasso.with(context).load(R.drawable.none_img).into(imageView);
    }
}