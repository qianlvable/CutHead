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
import com.cuthead.controller.ProgressWheel;

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
    ProgressWheel pw;

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
        Map<String, String> paras = new HashMap<String, String>();
        CustomRequest req = new CustomRequest(Request.Method.POST, url, paras, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                pw.stopSpinning();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mRequestQueue.add(req);
        return view;
    }


}
