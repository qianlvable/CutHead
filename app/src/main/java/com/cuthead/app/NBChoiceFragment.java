package com.cuthead.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

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



            btn_date = (Button) mView.findViewById(R.id.btn_date);


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
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth)
                        {
                            //editText.setText("日期：" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, year, monthOfYear, dayOfMonth);
                    datePickerDialog.show();
                }
            });
          //**********************************************************************************
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

                    }

                    // Provider被disable时触发此函数，比如GPS被关闭
                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                    //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            Log.e("Map", "Location changed : Lat: "
                                    + location.getLatitude() + " Lng: "
                                    + location.getLongitude());
                        }
                    }
                };
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null){
                    latitude = location.getLatitude(); //经度
                    longitude = location.getLongitude(); //纬度
                }
            }
            //*******************************************************


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



}





