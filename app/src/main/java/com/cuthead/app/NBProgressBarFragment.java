package com.cuthead.app;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.cuthead.controller.ProgressWheel;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NBProgressBarFragment extends Fragment {
    private RequestQueue mRequestQueue;
    final String url = null;
    String orderID;
    String hairstyle;
    String latitude;
    String longitude;
    String date;
    String remark;
    private ViewGroup indicatorLayout;
    private TextView dot;
    private ImageView bar;
    public NBProgressBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nbprogress_bar, container, false);
        final ProgressWheel progressbar = (ProgressWheel)view.findViewById(R.id.progress_wheel);
        progressbar.spin();



        indicatorLayout = (RelativeLayout)view.findViewById(R.id.indicator2);
        bar = (ImageView)indicatorLayout.findViewById(R.id.phase1_bar);
        bar.setImageResource(R.drawable.progress_indicate_bar);
        dot = (TextView)indicatorLayout.findViewById(R.id.phase1_dot);
        dot.setBackgroundResource(R.drawable.progress_bar_mark);

        //mRequestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> para = new HashMap<String, String>();
        Button btn_next = (Button) view.findViewById(R.id.btn_pro_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                NBTimeFragment nbTimeFragment = new NBTimeFragment();
                ft.replace(R.id.fragment_container,nbTimeFragment).addToBackStack(null);
                ft.commit();
            }
        });
        // add data
       /*
         Bundle bget = getArguments();
        hairstyle = bget.getString("hairstyle");
        latitude = bget.getString("latitude");
        longitude = bget.getString("longitude");
        remark = bget.getString("remark");

        para.put("longitude",longitude);
        para.put("latitude",latitude);
        para.put("hairstyle",hairstyle);
        para.put("date",date);
        para.put("reamrk",remark);

        CustomRequest req = new CustomRequest(Request.Method.POST,url,para,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                        orderID = json.getString("orderID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Fragment barberListFragment = new NBBaberListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("BABER_LIST",json.toString());
                bundle.putString("ORDER_ID",orderID);
                bundle.putString("hairstyle",hairstyle);
                bundle.putString("remark",remark);
                bundle.putString("date",date);
                barberListFragment.setArguments(bundle);


                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.qb_container,barberListFragment).commit();

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VollyErrorHelper.getMessage(volleyError);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });*/


        return view;
    }


}
