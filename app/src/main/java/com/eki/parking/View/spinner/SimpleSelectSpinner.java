package com.eki.parking.View.spinner;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;

import com.eki.parking.View.impl.ISpinnerItem;

import java.util.ArrayList;

/**
 * Created by Hill on 2018/9/13.
 */
public class SimpleSelectSpinner extends AppCompatSpinner {

    protected ArrayList<String> optionList;
    private Context context;
    public OnSelectListener listener;

    public interface OnSelectListener{
        void onStringSelect(int position, String str);
    }

    public SimpleSelectSpinner(Context context,ArrayList<String> list) {
        super(context);
        this.context=context;
        optionList=list;
        initView();
    }

    private void initView() {
        OptionAdapter adapter;

        if (this instanceof ISpinnerItem){
            adapter=new OptionAdapter(context,optionList,(ISpinnerItem)this);
        }else {
            adapter=new OptionAdapter(context,optionList,OptionViewEnum.Default);
            adapter.setTextGravity(SpinnerTextGravity.Center);
        }

        setAdapter(adapter);
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listener!=null)
                    listener.onStringSelect(position,optionList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public SimpleSelectSpinner setSelectListener(OnSelectListener l){
        listener=l;
        return this;
    }

}
