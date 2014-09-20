package com.cuthead.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.cuthead.controller.TimeUtil;
import com.cuthead.models.MyTimeMark;

import java.util.ArrayList;

/**
 * Created by shixu on 2014/9/19.
 */
public class ReorderFragment extends Fragment {
    View view;
    TextView tv_rb_hairstyle;
    TextView tv_rb_add;
    TextView tv_rb_date;
    String filenumber;
    String hairstyle;
    String datetime;

    NumberPicker hour_Picker;
    NumberPicker minute_Picker;
    EditText et_showtime;

    String commitTime;
    String []hour;
    Button btn_next;
    boolean isChooseTime = false;
    String tempsum ;
    String remark;
    String barphone;
    String distance;
    String barberName;
    String getTime;

    String cusname;
    String cusphone;
    String sex;
    String address;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reorder, container, false);

        Bundle bundle = getArguments();
        filenumber = bundle.getString("filenumber");
        getTime = bundle.getString("time");

        SharedPreferences file = getActivity().getSharedPreferences(filenumber,0);
        hairstyle = file.getString("hairstyle","无法显示");
        address = file.getString("address","无法显示");
        datetime = file.getString("time","无法显示");
        cusname = file.getString("cusname","无法显示");
        cusphone = file.getString("cusphone","无法显示");
        sex = file.getString("sex","无法确定");
        distance = file.getString("distance","无法确定");
        barphone = file.getString("barberphone","你猜");
        barberName = file.getString("barbername","无法确定");
        remark = file.getString("remark","无法确定");

        String[] val1 = datetime.split(";");                              //make the date right expression
        final String date = val1[0];

        tv_rb_hairstyle = (TextView)view.findViewById(R.id.tv_rebook_hairstyle);
        tv_rb_add = (TextView) view.findViewById(R.id.tv_rebook_add);
        tv_rb_date = (TextView) view.findViewById(R.id.tv_rebook_date);
        et_showtime = (EditText) view.findViewById(R.id.et_rebook_time);
        btn_next = (Button) view.findViewById(R.id.btn_submit);

        tv_rb_hairstyle.setText(hairstyle);
        tv_rb_add.setText(address);
        tv_rb_date.setText(date);

        final ArrayList<MyTimeMark> time = new ArrayList<MyTimeMark>(TimeUtil.getAvailableTime(TimeUtil.pharseTimeString(getTime)));
        MyTimeMark myTimeMark;                                                                 //  store bianliang
        hour = new String[time.size()];                                                        //get String for hour_picker
        for(int i = 0;i<time.size();i++)
        {
            hour[i] =Integer.toString(time.get(i).getHour());
        }
        minute_Picker = (NumberPicker) view.findViewById(R.id.rebook_minute_picker);                   // minute_picker
        hour_Picker = (NumberPicker) view.findViewById(R.id.rebook_hour_picker);                       //hour_picker
        hour_Picker.setDisplayedValues(hour);
        hour_Picker.setMaxValue(hour.length-1);
        hour_Picker.setMinValue(0);

        hour_Picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, final int newVal) {                     //newVal is index
                isChooseTime = true;
                ArrayList<MyTimeMark> timeInner = new ArrayList<MyTimeMark>(TimeUtil.getAvailableTime(TimeUtil.pharseTimeString(getTime)));
                int zeroMark = timeInner.get(newVal).getZeroMark();
                int twentyMark = timeInner.get(newVal).getTwentyMark();                          //监听器的参数都是数组下标所以要通过下标来确定所选时间
                int fourtyMark = timeInner.get(newVal).getFourtyMark();                          //对于小时可从上面的hour[]数组获得  即hour[newhour]
                int sum = zeroMark*0+twentyMark*20+fourtyMark*40;
                final int newhour = newVal;
                switch (zeroMark+twentyMark+fourtyMark)
                {
                    case 1: if(sum == 0)
                        tempsum = sum+"0";
                        String minute1 [] = {sum+""};
                        minute_Picker.setMaxValue(0);                                         //只有一种分钟选择的时候  不可能检测到分钟改变所以 直接以默认分钟为所选分钟
                        minute_Picker.setDisplayedValues(minute1);
                        minute_Picker.setMinValue(0);
                        et_showtime.setText(hour[newhour]+"时"+sum+"分");
                        //commitTime = date+";"+hour[newhour]+"."+sum;
                        commitTime = correctTime(date,hour[newhour],sum+"");
                        isChooseTime = true;
                        //getFinalTime.getFinalTime(commitTimw);                                 //接口传值
                        break;
                    case 2: String []minute2 = new String[2];                                     //两种和三种分钟情 的情况差不多    分钟从 minute[]数组里面取  但是内部类访问要求数组为final
                        switch (sum)                                                         //所以吧minute[]数组赋给拎一个 final数组 从而取值
                        {
                            case 20: minute2[0] = "00";minute2[1] = "20";break;
                            case 40: minute2[0] = "00";minute2[1] = "40";break;
                            case 60: minute2[0] = "20";minute2[1] = "40";break;
                        }
                        minute_Picker.setDisplayedValues(minute2);
                        minute_Picker.setMaxValue(1);
                        minute_Picker.setMinValue(0);
                        final String getMinute2[] = minute2;
                        String initminute2 = getMinute2[0];                                    //如果用户不选择分钟那么默认的分钟时 数组的第0个
                        //commitTime = date+";"+hour[newhour]+"."+initminute2;
                        commitTime = correctTime(date,hour[newhour],initminute2);
                        //getFinalTime.getFinalTime(commitTimw);                                 //以上三行就是设置默认时间的
                        et_showtime.setText(hour[newhour] + "时" + initminute2 + "分");
                        minute_Picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int moldVal, int mnewVal) {
                                et_showtime.setText(hour[newhour] + "时" + getMinute2[mnewVal] + "分");
                                //commitTime = date+";"+hour[newhour]+"."+getMinute2[mnewVal];                 //最后的提交带有日期和时间的  最终时间
                                commitTime = correctTime(date, hour[newhour], getMinute2[mnewVal]);
                                isChooseTime = true;
                                //getFinalTime.getFinalTime(commitTimw);
                            }
                        });break;
                    case 3: String minute3 [] = {"00","20","40"};
                        minute_Picker.setDisplayedValues(minute3);
                        minute_Picker.setMaxValue(2);
                        minute_Picker.setMinValue(0);
                        final String getMinute3[] = minute3;
                        String initminute3 = getMinute3[0];                                    //如果用户不选择分钟那么默认的分钟时 数组的第0个
                        //commitTime = date+";"+hour[newhour]+"."+initminute3;
                        commitTime = correctTime(date,hour[newhour],initminute3);
                        //getFinalTime.getFinalTime(commitTimw);                                 //以上三行就是设置默认时间的
                        et_showtime.setText(hour[newhour] + "时" + initminute3 + "分");
                        minute_Picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int moldVal, int mnewVal) {
                                et_showtime.setText(hour[newhour] + "时" + getMinute3[mnewVal] + "分");
                                //commitTime = date+";"+hour[newhour]+"."+getMinute3[mnewVal];                 //最后的提交带有日期和时间的  最终时间
                                commitTime = correctTime(date, hour[newhour], getMinute3[mnewVal]);
                                isChooseTime = true;
                                //getFinalTime.getFinalTime(commitTimw);
                            }
                        });
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment orderFragment = new OrderSuccessFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cusname",cusname);
                bundle.putString("cusphone",cusphone);
                bundle.putString("sex",sex);
                bundle.putString("barphone",barphone);
                bundle.putString("hairstyle",hairstyle);
                bundle.putString("distance",distance);
                bundle.putString("time",commitTime);
                bundle.putString("remark",remark);
                bundle.putInt("flag_order",1);
                bundle.putString("address",address);
                bundle.putString("barberName",barberName);

                Log.e("cusname",cusname);
                Log.e("cusphone",cusphone);
                Log.e("sex",sex);
                Log.e("barphone",barphone);
                Log.e("hairstyle",hairstyle);
                Log.e("distance",distance);
                Log.e("time",commitTime);
                Log.e("remark",remark);
                Log.e("address",address);
                Log.e("barberName",barberName);
                orderFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.rebook_container,orderFragment).addToBackStack(null).commit();

            }
        });

        return view;
    }

    public String correctTime(String date,String hour,String minute){
        if(Integer.parseInt(hour)<10)
            hour = "0"+hour;
        if(minute == "0")
            minute = "00";
        return date+";"+hour+":"+minute;
    }
}
