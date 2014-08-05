package com.cuthead.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
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
    private String hair_style;
    private double latitude=0.0;
    private double longitude =0.0;
    int Year=0;                                   //要提交的日期参数
    int Month = 0;
    int Day = 0;
    private Bundle bundle;
    GetDate getDate;
    GetStyle getStyle;
    GetLocation getLocation;


    public NBChoiceFragment() {
            // Required empty public constructor
        }


    public interface GetDate
    {
        public void getDate(int year,int month,int day);
    }
    public interface GetStyle
    {
        public void getStyle(int style);
    }
    public interface GetLocation
    {
        public void getLocation(double longitude,double lantitude);
    }


    public void onAttach(Activity activity){
        try {
            getDate = (GetDate) activity;
            getStyle = (GetStyle) activity;
            getLocation = (GetLocation) activity;
        } catch (Exception e) {
            // TODO: handle exception
            throw new ClassCastException(activity.toString()
                    + "must implement photoUrlTransferMsg");
        }
        super.onAttach(activity);
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
                                //tv_show.setText("日期是"+Year+" "+Month+" "+ Day);
                            }
                            else                                                                                 //选择时刻是将来
                            {
                                Year = Setyear;Month = SetmonthOfYear+1;Day = SetdayOfMonth;    //初始化要提交的日期  获取的月份总是比实际小一所以要在事后加一作调整
                                //tv_show.setText("日期是"+Year+" "+Month+" "+ Day);
                            }
                            tv_show.setText("日期是"+Year+" "+Month+" "+ Day);
                            getDate.getDate(Year,Month,Day);
                        }
                    }, year, monthOfYear, dayOfMonth);
                    datePickerDialog.show();
                }
            });
            latitude = LocationUtil.getLatitude(getActivity());
            longitude = LocationUtil.getLongitude(getActivity());
            getLocation.getLocation(longitude,latitude);
            //Log.d("longitudehaha", Double.toString(longitude));
            //Log.d("latitudeahah", Double.toString(latitude));




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
                        case R.id.rb_bancun : hair_style = (String)rb_bancun.getTag();getStyle.getStyle(11);break;
                        case R.id.rb_yuancun: hair_style = (String)rb_yuancun.getTag();getStyle.getStyle(12);break;
                        case R.id.rb_xiuliuhai: hair_style = (String)rb_xiuliuhai.getTag();getStyle.getStyle(13);break;
                        case R.id.rb_tiguang: hair_style = (String)rb_tiguang.getTag();getStyle.getStyle(14);break;
                    }

                } else if (group == permGroup) {
                    cutGroup.clearCheck();
                    dyeGroup.clearCheck();
                    washGroup.clearCheck();
                    switch (permGroup.getCheckedRadioButtonId())
                    {
                        case R.id.rb_lizitang : hair_style = (String)rb_lizitang.getTag();getStyle.getStyle(21);break;
                        case R.id.rb_resutang: hair_style = (String)rb_resutang.getTag();getStyle.getStyle(22);break;
                        case R.id.rb_taocitang: hair_style = (String)rb_taocitang.getTag();getStyle.getStyle(23);break;
                    }
                } else if (group == dyeGroup) {
                    cutGroup.clearCheck();
                    permGroup.clearCheck();
                    washGroup.clearCheck();
                    switch (dyeGroup.getCheckedRadioButtonId())
                    {
                        //case R.id.rb_museran : tv_show.setText("您选择的是"+rb_museran.getText());break;
                        case R.id.rb_quantouran: hair_style = (String)rb_quantouran.getTag();getStyle.getStyle(31);break;
                        case R.id.rb_pianran : hair_style = (String)rb_pianran.getTag();getStyle.getStyle(32);break;
                        case R.id.rb_tiaoran : hair_style = (String)rb_tiaoran.getTag();getStyle.getStyle(33);break;
                        case R.id.rb_juse : hair_style = (String)rb_juse.getTag();getStyle.getStyle(34);break;
                    }
                }else if (group == washGroup) {
                    cutGroup.clearCheck();
                    permGroup.clearCheck();
                    dyeGroup.clearCheck();
                    switch (washGroup.getCheckedRadioButtonId())
                    {
                        case R.id.rb_shuixi : hair_style = (String)rb_shuixi.getTag();getStyle.getStyle(41);break;
                        case R.id.rb_ganxi: hair_style = (String)rb_ganxi.getTag();getStyle.getStyle(42);break;
                    }
                }
                changeedGroup = false;
                //getStyle.getStyle(Integer.getInteger(hair_style));
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
}





