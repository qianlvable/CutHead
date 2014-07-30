package com.cuthead.app;



import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.transition.Scene;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.CustomRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class QBRequestFragment extends Fragment {

    FragmentManager fm;


    public QBRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qbrequest, container, false);
        Button btn = (Button)view.findViewById(R.id.btn_request);
        fm = getActivity().getFragmentManager();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fm.beginTransaction().replace(R.id.qb_container, new QBProgressWheelFragment()).commit();


            }
        });
        return view;
    }


}
