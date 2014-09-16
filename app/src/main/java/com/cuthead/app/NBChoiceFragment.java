package com.cuthead.app;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.cuthead.controller.LocationUtil;

import java.util.Calendar;

/**
 * Created by shixu on 2014/7/28.
 */
public class NBChoiceFragment extends Fragment {
    private View mView;
    private RadioGroup cutGroup;
    private RadioGroup permGroup;
    private RadioGroup dyeGroup;
    private RadioGroup washGroup;
    private Boolean changeedGroup = false;
    private TextView tv_show;
    private RadioButton rb_bancun;                 //剪发小组
    private RadioButton rb_yuancun;
    private RadioButton rb_xiuliuhai;
    private RadioButton rb_tiguang;
    private RadioButton rb_resutang;               //烫发小组
    private RadioButton rb_taocitang;
    private RadioButton rb_lizitang;
    private RadioButton rb_quantouran;              //染发小组
    private RadioButton rb_pianran;
    private RadioButton rb_tiaoran;
    private RadioButton rb_juse;                     //洗发小组
    private RadioButton rb_ganxi;
    private RadioButton rb_shuixi;
    private Button btn_date;
    private int year, monthOfYear, dayOfMonth;
    int Year=0;                                   //要提交的日期参数
    int Month = 0;
    int Day = 0;
    private Button btn_next;
    private EditText et_customzed_hair;
    private ViewGroup indicatorLayout;
    private TextView dot;
    private String url = null;
    private RequestQueue mRequestQueue;
    String date = null;
    double latitude=0.0;
    double longitude =0.0;
    String remark = "wtf";
    String hairstyle;

    public NBChoiceFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            mView = inflater.inflate(R.layout.fragment_nb_choice, container, false);

            // for indicator view
            indicatorLayout = (RelativeLayout)mView.findViewById(R.id.indicator1);
            dot = (TextView)indicatorLayout.findViewById(R.id.phase1_dot);
            dot.setBackgroundResource(R.drawable.progress_bar_mark);

            tv_show = (TextView) mView.findViewById(R.id.tv_show);
            rb_bancun = (RadioButton) mView.findViewById(R.id.rb_bancun);
            rb_yuancun = (RadioButton) mView.findViewById(R.id.rb_yuancun);
            rb_xiuliuhai = (RadioButton) mView.findViewById(R.id.rb_xiuliuhai);
            rb_tiguang = (RadioButton) mView.findViewById(R.id.rb_tiguang);                    //********
            rb_lizitang = (RadioButton) mView.findViewById(R.id.rb_lizitang);
            rb_taocitang = (RadioButton) mView.findViewById(R.id.rb_taocitang);
            rb_resutang = (RadioButton) mView.findViewById(R.id.rb_resutang);                 //*********
            rb_quantouran = (RadioButton) mView.findViewById(R.id.rb_quantouran);
            rb_pianran = (RadioButton) mView.findViewById(R.id.rb_pianran);
            rb_tiaoran = (RadioButton) mView.findViewById(R.id.rb_tiaoran);
            rb_juse = (RadioButton) mView.findViewById(R.id.rb_juse);                         //*******
            rb_shuixi = (RadioButton) mView.findViewById(R.id.rb_shuixi);
            rb_ganxi = (RadioButton) mView.findViewById(R.id.rb_ganxi);

            cutGroup = (RadioGroup) mView.findViewById(R.id.cutGroup);
            cutGroup.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
            permGroup = (RadioGroup) mView.findViewById(R.id.permGroup);
            permGroup.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
            dyeGroup = (RadioGroup) mView.findViewById(R.id.dyeGroup);
            dyeGroup.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
            washGroup = (RadioGroup) mView.findViewById(R.id.washGroup);
            washGroup.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
            btn_date = (Button) mView.findViewById(R.id.btn_date);    //设置datepicker
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            monthOfYear = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            btn_next = (Button) mView.findViewById(R.id.btn_choice_next);
            et_customzed_hair = (EditText) mView.findViewById(R.id.et_customzed_hair);

            btn_date.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    /**
                     * Create a DatePickerDialog
                     * 第二个参数是一个DatePickerDialog.OnDateSetListener匿名内部类，当用户选择好日期点击done会调用里面的onDateSet方法
                     */
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int Setyear, int SetmonthOfYear,int SetdayOfMonth)
                        {
                            if(isAfter(year,monthOfYear,dayOfMonth,Setyear,SetmonthOfYear,SetdayOfMonth))         //如果选择时刻已经过去
                            {
                                Toast toast = Toast.makeText(getActivity(),"不能预约过去哦！", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Year = year;Month = monthOfYear+1;Day = dayOfMonth;                     //initial the date ;and pay attention that the month we got is always smaller than reality,
                                btn_date.setText("重新设定");                                              // so we have to plus 1*/
                                //tv_show.setText("日期是"+Year+" "+Month+" "+ Day);
                            }
                            else                                                                                 //if the time chosed is in future
                            {
                                Year = Setyear;Month = SetmonthOfYear+1;Day = SetdayOfMonth;           /** initial the date ... */
                                //tv_show.setText("日期是"+Year+" "+Month+" "+ Day);
                            }
                            tv_show.setText("日期是"+Year+" "+Month+" "+ Day);
                            date = correcDate(Year,Month,Day);                                    //set the final date
                        }
                    }, year, monthOfYear, dayOfMonth);
                    datePickerDialog.show();
                }
            });


            getLocationDialog();
            Log.e("longitudehaha", Double.toString(longitude));
            Log.e("latitudeahah", Double.toString(latitude));


            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isDateWrong(Year,Month,Day) || isLocationWrong(latitude,longitude) || isHairWrong(hairstyle))
                    {
                        Toast toast = Toast.makeText(getActivity(),"信息不完整或网络未连接",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        return;
                    }
                    remark = et_customzed_hair.getText().toString();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    NBProgressBarFragment nbProgressBarFragment = new NBProgressBarFragment();
                    Bundle bundle = new Bundle();                              //send data
                    bundle.putString("hairstyle",hairstyle);
                    bundle.putString("remark",remark);
                    bundle.putString("date",date);
                    bundle.putString("longitude",Double.toString(longitude));
                    bundle.putString("latitude",Double.toString(latitude));
                    nbProgressBarFragment.setArguments(bundle);
                    ft.replace(R.id.fragment_container,nbProgressBarFragment).addToBackStack(null);
                    ft.commit();
                }
            });


            return mView;
        }
    public boolean isHairWrong(String style){if(style.equals("0")) return true;else return false;}
    public boolean isDateWrong(int year,int month,int day){if(year*month*day == 0)return true;else return false;}
    public boolean isLocationWrong(double latitude,double longitude){if(latitude*longitude == 0.0) return true;else return false;}
    public String correcDate(int year,int month,int day){
        String Month = month+"";
        String Day = day+"";
        if(month<10)
            Month = "0"+month;
        if(day<10)
            Day = "0"+day;
        return year+"."+Month+"."+Day;
    }
    //radiobutton监听器
    class MyRadioGroupOnCheckedChangedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (!changeedGroup) {
                changeedGroup = true;
                if (group == cutGroup) {
                    permGroup.clearCheck();
                    dyeGroup.clearCheck();
                    washGroup.clearCheck();
                    switch (cutGroup.getCheckedRadioButtonId())
                    {
                        case R.id.rb_bancun : hairstyle = (String)rb_bancun.getTag();break;
                        case R.id.rb_yuancun: hairstyle = (String)rb_yuancun.getTag();break;
                        case R.id.rb_xiuliuhai: hairstyle = (String)rb_xiuliuhai.getTag();break;
                        case R.id.rb_tiguang: hairstyle = (String)rb_tiguang.getTag();break;
                    }

                } else if (group == permGroup) {
                    cutGroup.clearCheck();
                    dyeGroup.clearCheck();
                    washGroup.clearCheck();
                    switch (permGroup.getCheckedRadioButtonId())
                    {
                        case R.id.rb_lizitang : hairstyle = (String)rb_lizitang.getTag();break;
                        case R.id.rb_resutang: hairstyle = (String)rb_resutang.getTag();break;
                        case R.id.rb_taocitang: hairstyle = (String)rb_taocitang.getTag();break;
                    }
                } else if (group == dyeGroup) {
                    cutGroup.clearCheck();
                    permGroup.clearCheck();
                    washGroup.clearCheck();
                    switch (dyeGroup.getCheckedRadioButtonId())
                    {
                        //case R.id.rb_museran : tv_show.setText("您选择的是"+rb_museran.getText());break;
                        case R.id.rb_quantouran: hairstyle = (String)rb_quantouran.getTag();break;
                        case R.id.rb_pianran : hairstyle = (String)rb_pianran.getTag();break;
                        case R.id.rb_tiaoran : hairstyle = (String)rb_tiaoran.getTag();break;
                        case R.id.rb_juse : hairstyle = (String)rb_juse.getTag();break;
                    }
                }else if (group == washGroup) {
                    cutGroup.clearCheck();
                    permGroup.clearCheck();
                    dyeGroup.clearCheck();
                    switch (washGroup.getCheckedRadioButtonId())
                    {
                        case R.id.rb_shuixi : hairstyle = (String)rb_shuixi.getTag();break;
                        case R.id.rb_ganxi: hairstyle = (String)rb_ganxi.getTag();break;
                    }
                }
                changeedGroup = false;
            }
        }
    }

    //判断所选时间是否是过去的时刻
    boolean isAfter(int year,int monthOfYear,int dayOfMonth,int Set_year,int Set_monthOfyear,int Set_dayOfMonth)
    {
        if(Set_year<year)
            return true;
        else
        {
            if(Set_monthOfyear<monthOfYear)
                return true;
            else
            {
                if(Set_dayOfMonth<dayOfMonth && Set_monthOfyear == monthOfYear)
                    return true;
                else
                    return false;
            }
        }
    }


    void getLocationDialog(){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setMessage("正紧张的获取地理位置....");
        dialog.setTitle("麻烦等一下");
        latitude = LocationUtil.getLatitude(getActivity());
        longitude = LocationUtil.getLongitude(getActivity());
        dialog.show();

        if ( !isLocationWrong(latitude,longitude))
            dialog.dismiss();
    }
}





