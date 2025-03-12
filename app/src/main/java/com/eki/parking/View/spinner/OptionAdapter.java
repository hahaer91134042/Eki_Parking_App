package com.eki.parking.View.spinner;

import android.content.Context;
//import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eki.parking.R;
import com.eki.parking.Controller.util.ScreenUtils;
import com.eki.parking.View.impl.ISpinnerItem;

import java.util.ArrayList;

import androidx.collection.ArrayMap;

/**
 * Created by Hill on 2018/6/8.
 */
public class OptionAdapter extends BaseAdapter {
    private LayoutInflater inflator;
    private ArrayList<String> optionList;
    private OptionViewEnum optionView;
    private Context context;
    private ArrayMap<Integer,View> optionViewMap=new ArrayMap<>();
    private SpinnerTextGravity mGravity= SpinnerTextGravity.Center_Vertical;
    public float textSize=ScreenUtils.getScreenWidth()/50;
    private ISpinnerItem itemImpl;

    private OptionAdapter(Context context, ArrayList<String> list){
        this.context=context;
        inflator = LayoutInflater.from(context);
        optionList = list;
    }
    public OptionAdapter(Context context, ArrayList<String> list, ISpinnerItem impl){
        this(context,list);
        itemImpl=impl;
    }

    public OptionAdapter(Context context, ArrayList<String> list, OptionViewEnum ov) {
        this(context,list);
        optionView=ov;
    }

    public OptionAdapter setTextGravity(SpinnerTextGravity g){
        mGravity=g;
        return this;
    }

    @Override
    public int getCount() {
        if (optionList!=null)
            return optionList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (itemImpl!=null){
            convertView = inflator.inflate(itemImpl.getItemRes(), null);
            itemImpl.setUpItem(convertView,position);
        }else {
            convertView = inflator.inflate(optionView.viewRes, null);

            switch (optionView){
                case Default:
                    TextView tv = convertView.findViewById(R.id.textView);
                    tv.setText(optionList.get(position));
                    tv.setTextSize(textSize);
                    tv.setGravity(mGravity.getGravity());

                    break;
            }
        }



//            if (position==0){
//                convertView.setBackground(context.getDrawable(R.this.stroke_red));
//            }

//            optionViewMap.put(position,convertView);

        return convertView;
    }

    public void onViewSelected(int position) {
        for (ArrayMap.Entry<Integer,View> set:
                optionViewMap.entrySet()) {
            if (set.getKey()==position){
                set.getValue().setBackground(context.getDrawable(R.drawable.stroke_red));
            }else {
                set.getValue().setBackground(context.getDrawable(R.drawable.stroke_gray));
            }
        }
    }
}
