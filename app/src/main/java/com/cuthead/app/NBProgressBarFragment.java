package com.cuthead.app;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.CustomRequest;
import com.cuthead.controller.NetworkUtil;
import com.cuthead.controller.ProgressWheel;
import com.cuthead.models.OrderAccept;

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

    public NBProgressBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nbprogress_bar, container, false);
        ProgressWheel progressbar = (ProgressWheel)view.findViewById(R.id.progress_wheel);
        progressbar.spin();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> para = new HashMap<String, String>();
        // add para

        CustomRequest req = new CustomRequest(Request.Method.POST,url,para,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                  OrderAccept order = NetworkUtil.pharseOrderAcceptJson(json);
                  Bundle bundle = new Bundle();
                  bundle.putParcelable("order",order);
                  
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


        return view;
    }


}
