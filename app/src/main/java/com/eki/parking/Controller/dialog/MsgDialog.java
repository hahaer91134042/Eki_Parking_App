package com.eki.parking.Controller.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eki.parking.R;
import com.eki.parking.View.recycleview.adapter.BaseAdapter;
import com.eki.parking.View.recycleview.item.ItemLayout;

import com.hill.devlibs.dialog.ListItemValue;
import com.hill.devlibs.listener.OnMsgDialogBtnListener;
import com.hill.devlibs.recycleview.DividerItemDecoration;
import com.hill.devlibs.util.StringUtil;


import java.util.ArrayList;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Hill on 2017/7/31.
 */
public class MsgDialog extends AlertDialog.Builder {

    private TextView alarmMsgTextView;
    private RecyclerView recyclerView;
    private ArrayList<ListItemValue> itemList;
    private Context context;
    private MediaPlayer mPlayer;
    private Handler mHandler=new Handler();
    private SoundType mSoundType= SoundType.Silence;
    private SoundTime soundTime= SoundTime.During_3Sec;
    private OnMsgDialogBtnListener btnClickListener;
    private AlertDialog dialog;

    public enum SoundTime{
        During_3Sec(3000),
        During_5Sec(5000),
        During_7Sec(7000);

        public int DuringTime=0;
        SoundTime(int i) {
            DuringTime=i;
        }
    }
    public enum SoundType{
        Silence,
        Alarm
    }

    public static class MsgDialogSet{
        public String title="";
        public String msg="";
        public String pBtnTex="";
        public String nBtnTex="";
    }

    public MsgDialog(Context context) {
        super(context);
        this.context=context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_alarm_msg, null);
        alarmMsgTextView = (TextView) view.findViewById(R.id.alarm_msg);
        alarmMsgTextView.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        setView(view);
        setCancelable(false);
        setIcon(setIconRes());
//        setPositiveBtn(context.getString(R.string.Determine));
//        setNegativeBtn(context.getString(R.string.Cancel));
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                return false;
//            }
//        });
    }
    protected @DrawableRes int setIconRes(){
        return R.drawable.papaya_logo;
    }
    protected boolean isList=false;
    public MsgDialog(Context context, ArrayList<ListItemValue> list){
        super(context);
        this.context=context;
        itemList=list;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_alarm_msg_list, null);
        recyclerView=view.findViewById(R.id.recycleView);
        alarmMsgTextView = (TextView) view.findViewById(R.id.alarm_msg);
        alarmMsgTextView.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        setView(view);
        setCancelable(false);
        setIcon(setIconRes());

        isList=true;
    }

    private void initList() {
        ItemAdaptor adaptor=new ItemAdaptor(context);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.DividerColor.ColorLess));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptor);
    }

    public MsgDialog setShowMsg(String msg){
        alarmMsgTextView.setText(msg);
        return this;
    }

    public MsgDialog setShowTitle(String title){
        if(!StringUtil.isEmptyString(title))
            setTitle(title);
        return this;
    }

    public MsgDialog setSoundType(SoundType type){
        mSoundType=type;
        return this;
    }
    public MsgDialog setNegativeBtn(String text) {
        if(!StringUtil.isEmptyString(text))
            setNegativeButton(text, listener);
        return this;
    }
    public MsgDialog setPositiveBtn(String text) {
        if(!StringUtil.isEmptyString(text))
            setPositiveButton(text, listener);
        return this;
    }
    public MsgDialog setBtnClickListener(OnMsgDialogBtnListener listener){
        btnClickListener=listener;
        return this;
    }
    private DialogInterface.OnClickListener listener= new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
//            mHandler.post(stopRing);
            switch (which){
                case AlertDialog.BUTTON_POSITIVE:
                    if (btnClickListener!=null){
                        btnClickListener.onPostiveBtn();
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    if (btnClickListener!=null){
                        btnClickListener.onNegativeBtn();
                    }
                    break;
            }
        }
    };
//    public MsgDialog setSoundTime(SoundTime st){
//        soundTime=st;
//        return this;
//    }

    public void dismiss(){
        if (dialog!=null)
            dialog.dismiss();
    }

    @Override
    public AlertDialog show() {
//        startSound();
        dialog=super.show();
        if (isList){
            initList();
        }

        return dialog;
    }

//    public static class ListItemValue{
//        public String mainTexStr="";
//        public String subTexStr="";
//        public @ColorInt int mainTexColor=Color.BLACK;
//        public @ColorInt int subTexColor=Color.BLACK;
//    }

    private class ItemAdaptor extends BaseAdapter<ListItem> {

        public ItemAdaptor(Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.item_dialog_list_item,parent,false);
            ListItem item=new ListItem(view);
            item.init(getHeight()*15/100);
            return item;
        }

        @Override
        public void onBindViewHolder(ListItem item, int position) {
            super.onBindViewHolder(item, position);
            item.refresh(itemList.get(position));
        }


        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    private class ListItem extends ItemLayout<ListItemValue> {

        TextView mainText,subText;
        View parentView;
        public ListItem(@NonNull View itemView) {
            super(itemView);
            mainText =itemView.findViewById(R.id.mainText);
            mainText.setGravity(Gravity.CENTER);
            subText=itemView.findViewById(R.id.subText);
            parentView=itemView.findViewById(R.id.parentView);

        }

        @Override
        public void init(int lenght) {
            super.init(lenght);
            parentView.setBackground(context.getDrawable(R.drawable.stroke_round_corner_red));
        }

        @Override
        public void refresh(ListItemValue data) {
            super.refresh(data);
            mainText.setText(data.mainTexStr);
            mainText.setTextColor(data.mainTexColor);
            subText.setText(data.subTexStr);
            subText.setTextColor(data.subTexColor);
        }

    }

//    private void startSound() {
//        switch (mSoundType){
//            case Alarm:
//                mPlayer=MediaPlayer.create(context.getApplicationContext(), R.raw.security_alarm);
//                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mPlayer.setLooping(true);//會有 時常時短 要加
//                mPlayer.start();
//                break;
//        }
//        if (mPlayer!=null){
//            mHandler.postDelayed(stopRing,soundTime.DuringTime);
//        }
//    }
//    private Runnable stopRing=new Runnable() {
//        @Override
//        public void run() {
//            if (mPlayer!=null){
//                mPlayer.stop();
//                mPlayer.release();
//                mPlayer=null;
//            }
//            mHandler.removeCallbacks(this);
//        }
//    };


}
