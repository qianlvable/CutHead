package com.cuthead.app;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import com.cuthead.controller.GetFragment;
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
public class NBProgressBarFragment extends Fragment {
    private RequestQueue mRequestQueue;
    final String ip = "http://123.57.13.137";
    final String normal_url = "/appointment/normal/";
    String orderID;
    String hairstyle;
    String remark;
    String date;
    private ViewGroup indicatorLayout;
    private TextView dot;
    private ImageView bar;
    GetFragment getFragment;
    public NBProgressBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nbprogress_bar, container, false);
        final ProgressWheel progressbar = (ProgressWheel)view.findViewById(R.id.progress_wheel);
        progressbar.spin();

        getFragment.getNumber(1);
        indicatorLayout = (RelativeLayout)view.findViewById(R.id.indicator2);
        bar = (ImageView)indicatorLayout.findViewById(R.id.phase1_bar);
        bar.setImageResource(R.drawable.progress_indicate_bar);
        dot = (TextView)indicatorLayout.findViewById(R.id.phase1_dot);
        dot.setBackgroundResource(R.drawable.progress_bar_mark);

        mRequestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> para = new HashMap<String, String>();

        Bundle bundleget = getArguments();
        hairstyle = bundleget.getString("hairstyle");
        date = bundleget.getString("date");
        remark = bundleget.getString("remark");

        para.put("longitude",bundleget.getString("longitude"));
        para.put("latitude",bundleget.getString("latitude"));
        para.put("date",date);
        Log.d("testdate",date);

        CustomRequest req = new CustomRequest(Request.Method.POST,ip+normal_url,para,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    FragmentManager fm = getFragmentManager();
                    int code = json.getInt("code");
                    Log.d("Test Volly",json.toString());
                    if (code == 100){

                        String data = json.getString("data");
                        if (data == null || data.isEmpty() || data.equals("null")){
                            fm.beginTransaction().replace(R.id.fragment_container,new EmptyBarberList())
                                    .commit();
                            return;
                        }

                        Fragment barberListFragment = new NBBaberListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("barberlist",json.getString("data").toString());
                        bundle.putString("hairstyle",hairstyle);
                        bundle.putString("date",date);
                        bundle.putString("remark",remark);
                        barberListFragment.setArguments(bundle);

                        fm.beginTransaction().replace(R.id.fragment_container,barberListFragment).commit();
                    } else {
                        switch (code){
                            case 303:
                                Toast.makeText(getActivity(),"respne location errror",Toast.LENGTH_LONG).show();
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

    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(!(activity instanceof GetFragment)){
            throw new IllegalStateException("madan");
        }
        getFragment = (GetFragment)activity;
    }

}
