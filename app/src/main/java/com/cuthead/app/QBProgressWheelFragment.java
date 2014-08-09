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
import com.cuthead.controller.ProgressWheel;
import com.cuthead.controller.VollyErrorHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class QBProgressWheelFragment extends Fragment {
    final String url = null;
    private RequestQueue mRequestQueue;
    double longitude =0.0;
    double latitude = 0.0;
    ProgressWheel pw;
    String remark;
    String hair;
    String date;



    public QBProgressWheelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qbprogress_wheel, container, false);

        pw = (ProgressWheel)view.findViewById(R.id.qb_progress_wheel);
        pw.spin();


        Bundle bundle = getArguments();                                        //info from nbchoicefragment
        latitude = Double.valueOf(bundle.getString("latitude"));
        longitude = Double.valueOf(bundle.getString("longitude"));
        remark = bundle.getString("remark");
        hair = bundle.getString("hairstyle");
        date = bundle.getString("date");


                // Inflate the layout for this fragment
        mRequestQueue = Volley.newRequestQueue(getActivity());
        // 需添加地理位置信息
        Log.e("lon", Double.toString(longitude));
        Log.e("lat",Double.toString(latitude));


        SharedPreferences sp = getActivity().getSharedPreferences("com.cuthead.app.sp", Context.MODE_PRIVATE);

        Map<String, String> paras = new HashMap<String, String>();
        paras.put("longitude",Double.toString(longitude));
        paras.put("latitude",Double.toString(latitude));
        paras.put("remark",remark);
        /**    havn't bundle hair until you junp to NBBaberListFragment     */

        CustomRequest req = new CustomRequest(Request.Method.POST, url, paras, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VollyErrorHelper.getMessage(volleyError);
                Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_LONG).show();

            }
        });
        mRequestQueue.add(req);
        return view;
    }

}
