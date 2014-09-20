package com.cuthead.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.CustomRequest;
import com.cuthead.controller.ProgressWheel;
import com.cuthead.controller.VollyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shixu on 2014/9/20.
 */
public class RebookProgressBarFragment extends Fragment {

    private RequestQueue mRequestQueue;
    final String ip = "http://123.57.13.137";
    final String rebook_url = "/appointment/get/barber/";
    String barberphone;
    String hairstyle;
    String remark;
    String date;
    String time;
    String filenumber;
    private ViewGroup indicatorLayout;
    private TextView dot;
    private ImageView bar;
    public RebookProgressBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nbprogress_bar, container, false);
        final ProgressWheel progressbar = (ProgressWheel)view.findViewById(R.id.progress_wheel);
        progressbar.spin();

        indicatorLayout = (RelativeLayout)view.findViewById(R.id.indicator2);
        indicatorLayout.setVisibility(View.GONE);

        mRequestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> para = new HashMap<String, String>();

        Bundle bundleget = getArguments();
        filenumber = bundleget.getString("filenumber");
        SharedPreferences file = getActivity().getSharedPreferences(filenumber,0);
        barberphone  = file.getString("barberphone","null");
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        date = correcDate(year,month,day);

        Log.d("date",date);
        Log.d("barberphone",barberphone);
        Log.d("barberphone",filenumber);


        para.put("date",date);
        para.put("phone",barberphone);

        CustomRequest req = new CustomRequest(Request.Method.POST,ip+rebook_url,para,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    FragmentManager fm = getFragmentManager();
                    int code = json.getInt("code");
                    Log.d("Test Volly", json.toString());

                    if (code == 100){

                        String data = json.getString("data");
                        if (data == null || data.isEmpty() || data.equals("null")){
                            fm.beginTransaction().replace(R.id.fragment_container,new EmptyBarberList())
                                    .commit();
                            return;
                        }

                        /*
                        Fragment barberListFragment = new NBBaberListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("barberlist",json.getString("data").toString());
                        bundle.putString("hairstyle",hairstyle);
                        bundle.putString("date",date);
                        bundle.putString("remark",remark);
                        barberListFragment.setArguments(bundle);*/

                        Log.d("string data",data);
                        String [] valone = json.toString().split(",");
                        Log.e("String time",valone[3]);
                        String timestr = valone[3];
                        String valtwo[] = timestr.split("\"");
                        Log.e("String time",valtwo[3]);
                        time = valtwo[3];
                        ReorderFragment reorderFragment = new ReorderFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("time",time);
                        bundle.putString("filenumber",filenumber);
                        reorderFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.rebook_container,reorderFragment).commit();
                    } else {
                        switch (code){
                            case 303:
                                Toast.makeText(getActivity(), "respne location errror", Toast.LENGTH_LONG).show();
                                break;
                            case 304:
                                Toast.makeText(getActivity(),"respne location errror",Toast.LENGTH_LONG).show();
                                break;
                            case 305:
                                Toast.makeText(getActivity(),"respne date errror",Toast.LENGTH_LONG).show();
                                break;
                            case 306:
                                Toast.makeText(getActivity(),"barber unregister",Toast.LENGTH_LONG).show();
                                break;

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VollyErrorHelper.getMessage(volleyError);
                Toast.makeText(getActivity(),errorMsg, Toast.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(req);


        return view;
    }

    public String correcDate(int year,int month,int day){
        String Month = month+"";
        String Day = day+"";
        if(month<10)
            Month = "0"+month;
        if(day<10)
            Day = "0"+day;
        return year+"."+Month+"."+Day;
    }
}
