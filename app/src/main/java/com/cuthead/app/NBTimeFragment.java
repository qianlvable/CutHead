package com.cuthead.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.cuthead.controller.TimeUtil;
import com.cuthead.models.MyTimeMark;

import java.util.ArrayList;

/**
 * Created by shixu on 2014/7/28.
 */
public class NBTimeFragment extends Fragment {
    private View mView;
    private NumberPicker hour_Picker;
    private NumberPicker minute_Picker;
    private TextView tv_showtime;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_nb_time, container, false);
        tv_showtime = (TextView) mView.findViewById(R.id.tv_showtime);

        final String testtime = "6:20-6:40-7:40-12:00-15:40-16:20";

        final ArrayList<MyTimeMark> time = new ArrayList<MyTimeMark>(TimeUtil.getAvailableTime(TimeUtil.pharseTimeString(testtime)));
        MyTimeMark myTimeMark;     //  store bianliang
        String []hour = new String[time.size()];                        //get String for hour_picker
        for(int i = 0;i<time.size();i++)
        {
            hour[i] =Integer.toString(time.get(i).getHour());
        }
        minute_Picker = (NumberPicker) mView.findViewById(R.id.minute_picker);      // minute_picker
        hour_Picker = (NumberPicker) mView.findViewById(R.id.hour_picker);    //hour_picker
        hour_Picker.setMaxValue(hour.length-1);
        hour_Picker.setMinValue(0);
        hour_Picker.setDisplayedValues(hour);
        hour_Picker.setWrapSelectorWheel(false);
        hour_Picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, final int newVal) {                     //newVal is index
                ArrayList<MyTimeMark> timeInner = new ArrayList<MyTimeMark>(TimeUtil.getAvailableTime(TimeUtil.pharseTimeString(testtime)));
                int zeroMark = timeInner.get(newVal).getZeroMark();
                int twentyMark = timeInner.get(newVal).getTwentyMark();
                int fourtyMark = timeInner.get(newVal).getFourtyMark();
                int sum = zeroMark*0+twentyMark*20+fourtyMark*40;
                final int newhour = newVal;
                switch (zeroMark+twentyMark+fourtyMark)
                {
                    case 1: String minute1 [] = {sum+"","vfv"};
                            minute_Picker.setMaxValue(0);
                            minute_Picker.setMinValue(0);
                            minute_Picker.setDisplayedValues(minute1);
                            minute_Picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                @Override
                                public void onValueChange(NumberPicker picker, int moldVal, int mnewVal) {

                                    tv_showtime.setText("已选择时间"+ newhour+"时"+mnewVal+"分");
                                }
                            });
                            break;
                    case 2: String []minute2 = new String[2];
                            switch (sum)
                            {
                                case 20: minute2[0] = "00";minute2[1] = "20";break;
                                case 40: minute2[0] = "00";minute2[1] = "40";break;
                                case 60: minute2[0] = "20";minute2[1] = "40";break;
                            }
                            //String []minute2 = {"20","40"};
                            minute_Picker.setMaxValue(1);
                            minute_Picker.setMinValue(0);
                            minute_Picker.setDisplayedValues(minute2);
                            minute_Picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                @Override
                                public void onValueChange(NumberPicker picker, int moldVal, int mnewVal) {
                                    tv_showtime.setText("已选择时间"+ newhour+"时"+mnewVal+"分");
                                }
                            });break;
                    case 3: String minute3 [] = {"00","20","40",""};
                            minute_Picker.setMaxValue(2);
                            minute_Picker.setMinValue(0);
                            minute_Picker.setDisplayedValues(minute3);
                            minute_Picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                @Override
                                public void onValueChange(NumberPicker picker, int moldVal, int mnewVal) {
                                    tv_showtime.setText("已选择时间"+ newhour+"时"+mnewVal+"分");
                                }
                            });
                }
            }
        });







        return mView;
    }

}
