package com.eki.parking.View.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eki.parking.R;
import com.eki.parking.View.abs.RelativeCustomView;

/**
 * Created by Hill on 2017/12/27.
 */

public class LogoImgView extends RelativeCustomView {


    private ImageView iconImg;

//    private TextView iconTex;

    public LogoImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iconImg=(ImageView)itemView.findViewById(R.id.iconImg);
//        iconTex=(TextView)itemView.findViewById(R.id.iconTex);
    }


    @Override
    public ViewGroup.LayoutParams initNewLayoutParams() {
        return null;
    }

    @Override
    public int setInflatView() {
        return R.layout.item_logo_view;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int iconSize=Math.min(getWidth(),getHeight());
        iconImg.getLayoutParams().height=iconSize;
        iconImg.getLayoutParams().width=iconSize;

//        PrintLogKt.e("LogoImg","---Logo Img Width---"+getWidth());
//        PrintLogKt.e("LogoImg","---Logo Img canvas Width---"+canvas.getWidth());
        setUpLogo();
    }
    private void setUpLogo() {

//        iconTex.setTextColor(Color.WHITE);
//        iconTex.setTextSize(getWidth()/10);
    }
}
