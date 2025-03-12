package com.eki.parking.View.spinner;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.eki.parking.R;
import com.eki.parking.Controller.util.ScreenUtils;
import com.eki.parking.View.abs.ConstrainCustomView;


import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Hill on 2018/9/10.
 */
public class BirthdaySelectView extends ConstrainCustomView {

    private SpinnerContainerView yearContainer;
    private SpinnerContainerView monthContainer;
    private SpinnerContainerView dayContainer;
    private OnDateSelectListener listener;

    public interface OnDateSelectListener{
        void onYearSelect(int year);
        void onMonthSelect(int month);
        void onDaySelect(int day);
    }

    public BirthdaySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBirthDaySpinner();
    }



    private void initBirthDaySpinner() {
        yearContainer=itemView.findViewById(R.id.yearContainer);
        monthContainer=itemView.findViewById(R.id.monthContainer);
        dayContainer=itemView.findViewById(R.id.dayContainer);

        YearSpinner yearSpinner=new YearSpinner(context);

        yearContainer.initView(yearSpinner,new LinearLayout.LayoutParams(
                ScreenUtils.getScreenWidth()*27/100,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        MonthSpinner monthSpinner=new MonthSpinner(context);
        monthContainer.initView(monthSpinner,new LinearLayout.LayoutParams(
                ScreenUtils.getScreenWidth()*17/100,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        DaySpinner daySpinner=new DaySpinner(context);
        dayContainer.initView(daySpinner,new LinearLayout.LayoutParams(
                ScreenUtils.getScreenWidth()*17/100,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

    }

    public BirthdaySelectView setDateSelectListener(OnDateSelectListener l){
        listener=l;
        return this;
    }

    @Override
    public ViewGroup.LayoutParams initNewLayoutParams() {
        return null;
    }

    @Override
    public int setInflatView() {
        return R.layout.item_birthday_spinner;
    }

    private class YearSpinner extends AppCompatSpinner{

        private ArrayList<String> yearList=new ArrayList<>();
        {
            yearList.add("出生年份");
        }

        public YearSpinner(Context context) {
            super(context);
            initView();
        }

        private void initView() {
            Calendar calendar=Calendar.getInstance();

            int nowYear=calendar.get(Calendar.YEAR);
//            HHLog.i("Now year->"+nowYear);
            int endYear=nowYear-80;

            for (int i = nowYear; i >=endYear ; i--) {
                String y=String.valueOf(i);
                yearList.add(y);
//                HHLog.w("Add year->"+y);
            }
            OptionAdapter adapter=new OptionAdapter(context,yearList,OptionViewEnum.Default);
            adapter.setTextGravity(SpinnerTextGravity.Center);
            setAdapter(adapter);
            setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    HHLog.d("Select year->"+yearList.get(position));
                    if (position<1){
                        if (listener!=null)
                            listener.onYearSelect(0);
                        return;
                    }
                    if (listener!=null)
                        listener.onYearSelect(Integer.valueOf(yearList.get(position)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private class MonthSpinner extends AppCompatSpinner{
        private ArrayList<String> monthList=new ArrayList<>();
        {
            monthList.add("月");
            for (int i = 1; i <=12 ; i++) {
                monthList.add(String.valueOf(i));
            }
        }
        public MonthSpinner(Context context) {
            super(context);
            initView();
        }

        private void initView() {
            OptionAdapter adapter=new OptionAdapter(context,monthList,OptionViewEnum.Default);
            adapter.setTextGravity(SpinnerTextGravity.Center);
            setAdapter(adapter);
            setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    HHLog.d("Select month->"+monthList.get(position));
                    if (position<1){
                        if (listener!=null)
                            listener.onMonthSelect(0);
                        return;
                    }
                    if (listener!=null)
                        listener.onMonthSelect(Integer.valueOf(monthList.get(position)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    private class DaySpinner extends AppCompatSpinner{
        private ArrayList<String> dayList =new ArrayList<>();
        {
            dayList.add("日");
            for (int i = 1; i <=31 ; i++) {
                dayList.add(String.valueOf(i));
            }
        }
        public DaySpinner(Context context) {
            super(context);
            initView();
        }

        private void initView() {
            OptionAdapter adapter=new OptionAdapter(context,dayList,OptionViewEnum.Default);
            adapter.setTextGravity(SpinnerTextGravity.Center);
            setAdapter(adapter);
            setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    HHLog.d("Select day->"+dayList.get(position));
                    if (position<1){//當使用者選到 "日" 這個選項傳回0 用來判斷還未選擇日期
                        if (listener!=null)
                            listener.onDaySelect(0);
                        return;
                    }
                    if (listener!=null)
                        listener.onDaySelect(Integer.valueOf(dayList.get(position)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
