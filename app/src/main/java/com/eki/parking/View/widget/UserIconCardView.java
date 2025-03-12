package com.eki.parking.View.widget;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eki.parking.R;
import com.eki.parking.View.abs.FrameCustomView;



// TODO:Edit by Hill 2018/4/18 api need add
public class UserIconCardView extends FrameCustomView {

    AutoLoadImgView mImgView;
    TextView mTexView;
    CardView mCardView;

    public UserIconCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCard();
    }

    private void initCard() {
        mImgView=itemView.findViewById(R.id.imgView);
        mTexView=itemView.findViewById(R.id.texView);

        mImgView.setImageResource(R.drawable.icon_boy);
        mTexView.setText("UserName");
    }

    @Override
    public ViewGroup.LayoutParams initNewLayoutParams() {
        return null;
    }

    @Override
    public int setInflatView() {
        return R.layout.item_bottom_tex_card;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        super.dispatchDraw(canvas);
    }
}
