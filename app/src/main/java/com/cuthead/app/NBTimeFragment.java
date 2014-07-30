package com.cuthead.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by shixu on 2014/7/28.
 */
public class NBTimeFragment extends Fragment {
    private View mView;
    private static final Integer[] hour={8,9,10,11,12,13,14,15,16,17,18,19,20};
    private static final Integer[] minute={0,20,40};
    private Spinner Sp_hour,Sp_minute;
    private ArrayAdapter adapter_hour,adapter_minute;
    private TextView tv_hour;
    private TextView tv_minute;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_nb_time, container, false);

        tv_hour = (TextView)mView.findViewById(R.id.tv_hour);
        tv_minute = (TextView) mView.findViewById(R.id.tv_minute);

        Sp_hour = (Spinner) mView.findViewById(R.id.Sp_hour);
        Sp_minute = (Spinner) mView.findViewById(R.id.Sp_minute);
        //设置adapter
        adapter_hour = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,hour);
        adapter_minute = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,minute);
        Sp_hour.setAdapter(adapter_hour);
        Sp_minute.setAdapter(adapter_minute);

        //设置监听
        Sp_hour.setOnItemSelectedListener(new SpinnerSelectedListener());
        Sp_minute.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置初始值
        Sp_hour.setVisibility(View.VISIBLE);
        Sp_minute.setVisibility(View.VISIBLE);

        return mView;
    }
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView arg0, View arg1, int arg2,long arg3) {

            //tv_hour.setText(arg1.toString());
        }

        public void onNothingSelected(AdapterView arg0) {
        }
    }
}
