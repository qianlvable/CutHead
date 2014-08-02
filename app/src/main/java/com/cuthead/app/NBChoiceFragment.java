package com.cuthead.app;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private String hair_style;
    private double latitude=0.0;
    private double longitude =0.0;
    int Year=0;                                   //要提交的日期参数
    int Month = 0;
    int Day = 0;

    public NBChoiceFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            mView = inflater.inflate(R.layout.fragment_nb_choice, container, false);
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

            btn_date.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    /**
                     * 实例化一个DatePickerDialog的对象
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
                                Year = year;Month = monthOfYear+1;Day = dayOfMonth;                     //初始化要提交的日期  获取的月份总是比实际小一所以要在事后加一作调整
                                btn_date.setText("重新设定");
                                tv_show.setText("日期是"+Year+" "+Month+" "+ Day);
                            }
                            else                                                                                 //选择时刻是将来
                            {
                                Year = Setyear;Month = SetmonthOfYear+1;Day = SetdayOfMonth;    //初始化要提交的日期  获取的月份总是比实际小一所以要在事后加一作调整
                                tv_show.setText("日期是"+Year+" "+Month+" "+ Day);
                            }
                        }
                    }, year, monthOfYear, dayOfMonth);
                    datePickerDialog.show();
                }
            });
            getLocation();//  函数中获取位置信息 对  类成员变量  latitude longitude 赋值


            return mView;
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
                        case R.id.rb_bancun : hair_style = (String)rb_bancun.getTag();break;
                        case R.id.rb_yuancun: tv_show.setText("您选择的是"+hair_style);break;
                        case R.id.rb_xiuliuhai: tv_show.setText("您选择的是"+rb_xiuliuhai.getTag());break;
                        case R.id.rb_tiguang: tv_show.setText("您选择的是"+rb_tiguang.getTag());break;
                    }

                } else if (group == permGroup) {
                    cutGroup.clearCheck();
                    dyeGroup.clearCheck();
                    washGroup.clearCheck();
                    switch (permGroup.getCheckedRadioButtonId())
                    {
                        case R.id.rb_lizitang : tv_show.setText("您选择的是"+rb_lizitang.getText());break;
                        case R.id.rb_taocitang: tv_show.setText("您选择的是"+rb_taocitang.getText());break;
                        case R.id.rb_resutang: tv_show.setText("您选择的是"+rb_resutang.getText());break;
                    }
                } else if (group == dyeGroup) {
                    cutGroup.clearCheck();
                    permGroup.clearCheck();
                    washGroup.clearCheck();
                    switch (dyeGroup.getCheckedRadioButtonId())
                    {
                        //case R.id.rb_museran : tv_show.setText("您选择的是"+rb_museran.getText());break;
                        case R.id.rb_quantouran: tv_show.setText("您选择的是"+rb_quantouran.getText());break;
                        case R.id.rb_pianran : tv_show.setText("您选择的是"+rb_pianran.getText());break;
                        case R.id.rb_tiaoran : tv_show.setText("您选择的是"+rb_tiaoran.getText());break;
                        case R.id.rb_juse : tv_show.setText("您选择的是"+rb_juse.getText());break;
                    }
                }else if (group == washGroup) {
                    cutGroup.clearCheck();
                    permGroup.clearCheck();
                    dyeGroup.clearCheck();
                    switch (washGroup.getCheckedRadioButtonId())
                    {
                        case R.id.rb_shuixi : tv_show.setText("您选择的是"+rb_shuixi.getText());break;
                        case R.id.rb_ganxi: tv_show.setText("您选择的是"+rb_ganxi.getText());break;
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

    public void getLocation()
    {
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }else{
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {
                    Toast toast = Toast.makeText(getActivity(),"GPS已打开",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
               // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {
                    Toast toast = Toast.makeText(getActivity(),"GPS已关闭",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
            }
        }
    }
}





