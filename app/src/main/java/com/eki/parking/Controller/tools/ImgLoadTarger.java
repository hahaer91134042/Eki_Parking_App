package com.eki.parking.Controller.tools;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Hill on 2018/5/16.
 */
public abstract class ImgLoadTarger implements Target {

    public Bitmap imgMap;

    protected abstract void loadSuccess(Bitmap bitmap);
    protected abstract void loadPrepare(Drawable drawable);
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        imgMap=bitmap;
        loadSuccess(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        loadPrepare(placeHolderDrawable);
    }


}
