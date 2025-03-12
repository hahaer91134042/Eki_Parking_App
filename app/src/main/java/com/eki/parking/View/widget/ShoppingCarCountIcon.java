package com.eki.parking.View.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eki.parking.R;
import com.eki.parking.AppFlag;
import com.eki.parking.Controller.manager.SQLManager;

import com.eki.parking.View.abs.FrameCustomView;
import com.hill.devlibs.receiver.BaseReceiver;
import com.hill.devlibs.tools.Log;

//import butterknife.BindView;
//import butterknife.ButterKnife;

/**
 * Created by Hill on 2018/5/21.
 */
public class ShoppingCarCountIcon extends FrameCustomView
                                  implements SQLManager.OnActionListener {

    ImageView mShopCarIcon;
    TextView mShopNumText;
    FrameLayout mRedCircle;

    private ShoppingCarReceiver carReceiver;
    private int shopNumCount=0;

    public ShoppingCarCountIcon(Context context, AttributeSet attrs) {
        super(context, attrs);

        mShopCarIcon=itemView.findViewById(R.id.shopCarIcon);
        mShopNumText=itemView.findViewById(R.id.shopNumText);
        mRedCircle=itemView.findViewById(R.id.redCircle);

        init();
    }

    private void init() {

        reloadAmount();
        setOnClickListener(v -> {
            Log.i("--shop car press--");
            if (shopNumCount>0){
//                Intent intent=new Intent();
//                intent.setClass(context, ShoppingCarActivity.class);
//                getApp().getTopStackActivity().startActivitySwitchAnim(ShoppingCarActivity.class);
            }else {
                Toast.makeText(context,getString(R.string.Shopping_car_no_item),Toast.LENGTH_SHORT).show();
            }

        });

        carReceiver=new ShoppingCarReceiver(context, AppFlag.ShoppingHasChange);
        carReceiver.register();
    }

    public void reloadAmount(){
//        shopNumCount=getApp().getSqlManager().getAmountInShoppingCar();
//        if (shopNumCount>0){
//            mRedCircle.setVisibility(VISIBLE);
//            mShopNumText.setText(String.valueOf(shopNumCount));
//        }else {
//            mRedCircle.setVisibility(INVISIBLE);
//        }
    }

    public void onResume(){
        if (carReceiver!=null)
            carReceiver.register();
        if (!SQLManager.hasListener(this))
            SQLManager.addListener(this);
    }
    public void onRemove(){
        if (carReceiver!=null)
            carReceiver.unRegister();

    }
    public void onDestory(){
        if (SQLManager.hasListener(this))
            SQLManager.removeListener(this);
    }

    @Override
    public ViewGroup.LayoutParams initNewLayoutParams() {
        return null;
    }

    @Override
    public int setInflatView() {
        return R.layout.item_shopping_car_counter;
    }

    @Override
    public void onManagerChanged(SQLManager.NowAction action) {
        switch (action){
            case ShopCarNumber:
                reloadAmount();
                break;
        }
    }

    private class ShoppingCarReceiver extends BaseReceiver {

        public ShoppingCarReceiver(Context from, String... actions) {
            super(from, actions);
        }

        @Override
        public void onCatchReceiver(String action, Intent intent) {
            reloadAmount();
        }
    }

}
