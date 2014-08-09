package com.cuthead.app;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.CustomRequest;
import com.cuthead.controller.NetworkUtil;
import com.cuthead.controller.VollyErrorHelper;
import com.cuthead.models.OrderAccept;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class OrderSuccessFragment extends Fragment{
    TextView baberPhone;
    TextView baberName;
    TextView address;
    TextView shopName;
    String time;
    String orderID;
    String name;
    String phone;
    String sex;
    String baberphone;
    String hairstyle;
    String distance;
    String remark;
    RequestQueue requestQueue;
    String ip = "204.152.218.52";
    String sumbit_url  = "/appointment/normal/submit-order/";
    int flag = 1;   // if the message come from QuickReceiver the flag will be zero,the other is 1 which comes from normalbook
    OrderAccept order;
    public OrderSuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_order_success, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        Bundle bundle = new Bundle();
        flag = bundle.getInt("flag_order");
        if (flag == 1){                         //Come form normal book
            phone = bundle.getString("cusphone");
            sex = bundle.getString("sex");
            name = bundle.getString("cusname");
            orderID = bundle.getString("orderID");
            time = bundle.getString("time");
            baberphone = bundle.getString("barphone");
            hairstyle = bundle.getString("hairstyle");
            distance = bundle.getString("distance");
            remark = bundle.getString("remark");

            Map<String, String> paras = new HashMap<String, String>();
            paras.put("cusphone", phone);
            paras.put("cusname", name);
            paras.put("sex", sex);
            paras.put("barphone", baberphone);
            paras.put("hairstyle", hairstyle);
            paras.put("distance", distance);
            paras.put("time", time);
            paras.put("remark", remark);

            CustomRequest req = new CustomRequest(Request.Method.POST, ip + sumbit_url, paras, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    VisibiltyChange(view);
                    order = NetworkUtil.phraseOrderAcceptJson(jsonObject);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String errorMsg = VollyErrorHelper.getMessage(volleyError);
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(req);

        }
        else {      // come form quickBook
            order = this.getArguments().getParcelable("order");
        }
        VisibiltyChange(view);



        baberName = (TextView)view.findViewById(R.id.baber_name);
        baberPhone = (TextView)view.findViewById(R.id.baber_phone);
        address = (TextView)view.findViewById(R.id.address);
        shopName = (TextView)view.findViewById(R.id.baber_shop);
        VisibiltyChange(view);
        if (order != null){
            baberPhone.setText(order.getPhone());
            baberName.setText(order.getBaber());
            address.setText(order.getAddress());
            shopName.setText(order.getShop());

        } else {
            Log.d("CutHead", "Order empty error");
        }

        return view;
    }


    private void VisibiltyChange(View view){
        ViewGroup progressLayout = (ViewGroup) view.findViewById(R.id.progressbar_layout);
        progressLayout.setVisibility(View.GONE);
        ViewGroup orderLayout = (ViewGroup) view.findViewById(R.id.sucess_layout);
        orderLayout.setVisibility(View.VISIBLE);
    }
}
