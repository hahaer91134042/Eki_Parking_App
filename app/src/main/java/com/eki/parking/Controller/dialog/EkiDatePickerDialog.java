package com.eki.parking.Controller.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.eki.parking.R;
import com.hill.devlibs.tools.Log;


/**
 * Created by Hill on 2018/4/14.
 */

public class EkiDatePickerDialog extends AlertDialog.Builder{

    private Context context;
    private DatePicker datePicker;
    private OnDateSetListener listener;
    public interface OnDateSetListener{
        void onDateSet(int year, int month, int day);
    }

    public EkiDatePickerDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        init();
    }

    private void init() {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.item_datepicker,null);
        datePicker=itemView.findViewById(R.id.datePicker);



        setView(itemView);
        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("year->"+datePicker.getYear()+" month->"+(datePicker.getMonth()+1)+" day->"+datePicker.getDayOfMonth());
                if (listener!=null)
                    listener.onDateSet(datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
            }
        });
        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public EkiDatePickerDialog setOnDateSetListenet(OnDateSetListener l){
        listener=l;
        return this;
    }

}
