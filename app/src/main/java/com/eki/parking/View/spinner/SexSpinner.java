package com.eki.parking.View.spinner;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import com.eki.parking.Model.EnumClass.SexEnum;

import java.util.ArrayList;

/**
 * Created by Hill on 2018/9/10.
 */
public class SexSpinner extends AppCompatSpinner {

    private Context context;
    private SpinnerTextGravity mGravity= SpinnerTextGravity.Center;
    private ArrayList<SexEnum> sexList=new ArrayList<>();
    {
        sexList.add(SexEnum.Male);
        sexList.add(SexEnum.Female);
    }
    public SexSelectListener listener;

    public interface SexSelectListener{
        void onSexSelect(SexEnum sex);
    }

    public SexSpinner(Context context) {
        this(context,null);
    }

    public SexSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
//        initView();
    }

    public SexSpinner setGravity(SpinnerTextGravity gravity){
        mGravity=gravity;
        return this;
    }

    public void initView() {
        ArrayList<String> list=new ArrayList<>();
        for (SexEnum sex:
             sexList) {
            list.add(context.getString(sex.getStrRes()));
        }

        OptionAdapter adapter=new OptionAdapter(context,list,OptionViewEnum.Default);
        adapter.setTextGravity(mGravity);
        setAdapter(adapter);

        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                HHLog.w("sex select->"+context.getString(sexList.get(position).getStrRes()));
                if (listener!=null)
                    listener.onSexSelect(sexList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public SexSpinner setSexSelectListener(SexSelectListener l){
        listener=l;
        return this;
    }
}
