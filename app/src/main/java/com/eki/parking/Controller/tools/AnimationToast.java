package com.eki.parking.Controller.tools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.eki.parking.Controller.util.ScreenUtils;
import com.eki.parking.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AnimationToast{

    public Context context;
    public AnimationToast(Context context) {
        this.context = context;
    }

    public ArrayList<Object> getListo() {
        return listo;
    }

    public void setListo(ArrayList<Object> listo) {
        this.listo = listo;
    }

    ArrayList<Object> listo;

        boolean mShowTime;
        boolean mIsShow;
        WindowManager mWdm;
        Timer mTimer;
        View mToastView;
        WindowManager.LayoutParams mParams;
        Toast toast;

        public void initToast(boolean showTime) {

            toast = new Toast(context);
            mShowTime = showTime;//记录Toast的显示长短类型
            mIsShow = false;//记录当前Toast的内容是否已经在显示
            //mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mTimer = new Timer();
           // setParams();
            toast.setGravity(Gravity.CENTER, ScreenUtils.dpToPx(-40), ScreenUtils.dpToPx(80));
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(getView());
        }

        public void setToastShow(){
            if (!mIsShow) {
                mIsShow = true;
                toast.show();
                //mWdm.addView(toast.getView(), mParams); bug
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, (long) (mShowTime ? 5000 : 5000));
            }
        }

        private View getView(){

            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.item_choice_invoice_method, null);
            View view = LayoutInflater.from(context).inflate(R.layout.item_checkout_invoice_toast, viewGroup, false);

            TextView textView = view.findViewById(R.id.tv_checkout_invoice);
            textView.setTextColor(ColorStateList.valueOf(context.getColor(R.color.color_white)));
            textView.setTextSize(14f);
            textView.setText(context.getString(R.string.MemberShip_note));
            textView.setEms(15);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(ScreenUtils.dpToPx(10), ScreenUtils.dpToPx(10), ScreenUtils.dpToPx(10), ScreenUtils.dpToPx(10));

            return view;

        }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            toast.cancel();
            mIsShow = false;
            //Log.d(Constants.TAG, Constants.MSG + "msg.obj = " + msg.obj);
        }
    };

}