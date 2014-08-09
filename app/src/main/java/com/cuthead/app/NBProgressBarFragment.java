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

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NBProgressBarFragment extends Fragment {
    private RequestQueue mRequestQueue;
    final String ip = "204.152.218.52";
    final String normal_url = "/appointment/normal/";
    String orderID;
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

        mRequestQueue = Volley.newRequestQueue(getActivity());
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

        Bundle bundle = getArguments();
        para.put("longitude",bundle.getString("longitude"));
        para.put("latitude",bundle.getString("latitude"));
        para.put("hairstyle",bundle.getString("hairstyle"));
        para.put("date",bundle.getString("date"));

        CustomRequest req = new CustomRequest(Request.Method.POST,ip+normal_url,para,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    int code = json.getInt("code");
                    if (code == 100){
                        orderID = json.getString("orderID");
                        Fragment barberListFragment = new NBBaberListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("BABER_LIST",json.toString());
                        bundle.putString("ORDER_ID",orderID);
                        barberListFragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.qb_container,barberListFragment).commit();
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


}
