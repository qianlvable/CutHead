package com.cuthead.app;



import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.CustomRequest;
import com.cuthead.controller.ProgressWheel;

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
    final String url = null;
    String orderID;
    public NBProgressBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nbprogress_bar, container, false);
        final ProgressWheel progressbar = (ProgressWheel)view.findViewById(R.id.progress_wheel);
        progressbar.spin();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> para = new HashMap<String, String>();
        // add data
        Bundle bundle = getArguments();
        para.put("phone",bundle.getString("phone"));
        para.put("name",bundle.getString("name"));
        para.put("longitude",bundle.getString("longitude"));
        para.put("latitude",bundle.getString("latitude"));
        para.put("hairstyle",bundle.getString("hairstyle"));
        para.put("sexy",bundle.getString("sexy"));
        para.put("date",bundle.getString("date"));

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


                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.qb_container,barberListFragment).commit();

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


        return view;
    }


}
