package com.cuthead.app;



import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.CustomRequest;
import com.cuthead.controller.LocationUtil;
import com.cuthead.controller.ProgressWheel;
import com.cuthead.controller.VollyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class QBProgressWheelFragment extends Fragment {
    private RequestQueue mRequestQueue;
    double longitude =0.0;
    double latitude = 0.0;
    String name;
    String phone;
    String sex;
    ProgressWheel pw;

    final String ip = "http://123.57.13.137";
    final String quick_url = "/appointment/quick/";


    public QBProgressWheelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qbprogress_wheel, container, false);

        pw = (ProgressWheel)view.findViewById(R.id.qb_progress_wheel);
        pw.spin();
        // Inflate the layout for this fragment
        mRequestQueue = Volley.newRequestQueue(getActivity());
        // 需添加地理位置信息
        latitude = LocationUtil.getLatitude(getActivity());
        longitude = LocationUtil.getLongitude(getActivity());
        Log.d("lon", Double.toString(longitude));
        Log.d("lat",Double.toString(latitude));


        SharedPreferences sp = getActivity().getSharedPreferences("com.cuthead.app.sp", Context.MODE_PRIVATE);
        String userinfo = sp.getString("USER_INFO","empty");
        String[] val = userinfo.split(";");
        name = val[0];
        phone = val[1];
        sex = val[2];


        Map<String, String> paras = new HashMap<String, String>();
        paras.put("longitude",Double.toString(longitude));
        paras.put("latitude",Double.toString(latitude));
        paras.put("name",name);
        paras.put("phone",phone);
        paras.put("sex",sex);


        CustomRequest req = new CustomRequest(Request.Method.POST, ip+quick_url, paras, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                int code = 0;
                try {
                    code = json.getInt("code");
                    Log.d("Test network",json.getString("log"));
                    switch (code){
                        case 203:
                            Log.d("Test Network",json.getString("log"));
                            Toast.makeText(getActivity(),"phone error",Toast.LENGTH_LONG).show();
                            break;
                        case 204:
                            Log.d("Test Network",json.getString("log"));
                            Toast.makeText(getActivity(),"sex error",Toast.LENGTH_LONG).show();
                            break;
                        case 205:
                            Log.d("Test Network",json.getString("log"));
                            Toast.makeText(getActivity(),"name error",Toast.LENGTH_LONG).show();
                            break;
                        case 206:
                            Log.d("Test Network",json.getString("log"));
                            Toast.makeText(getActivity(),"location error",Toast.LENGTH_LONG).show();
                            break;
                        case 207:
                            Log.d("Test Network",json.getString("log"));
                            Toast.makeText(getActivity(),"location error",Toast.LENGTH_LONG).show();
                            break;
                        case 208:
                            Log.d("Test Network",json.getString("log"));
                            Toast.makeText(getActivity(),"don`t have barber error",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VollyErrorHelper.getMessage(volleyError);
                Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_LONG).show();
                Log.d("TEST",volleyError.toString());
            }
        });
        mRequestQueue.add(req);
        return view;
    }

}
