package com.cuthead.app;


import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
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
import com.cuthead.controller.GetFragment;
import com.cuthead.controller.ProgressWheel;
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
    String addressStr;
    String time;
    String orderID;
    String name;
    String barberNameStr;
    String phone;
    String sex;
    String baberphone;
    String hairstyle;
    String distance;
    String remark;
    RequestQueue requestQueue;
    String ip = "http://123.57.13.137";
    String sumbit_url  = "/appointment/normal/submit-order/";
    int flag = 1;   // if the message come from QuickReceiver the flag will be zero,the other is 1 which comes from normalbook
    OrderAccept order;
    GetFragment getFragment;

    public OrderSuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_order_success, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        getFragment.getNumber(5);
        Bundle bundle = getArguments();
        flag = bundle.getInt("flag_order");
        if (flag == 1){                         //Come form normal book
            ProgressWheel pw = (ProgressWheel)view.findViewById(R.id.nb_progress_wheel);
            pw.spin();
            phone = bundle.getString("cusphone");
            sex = bundle.getString("sex");
            name = bundle.getString("cusname");
            orderID = bundle.getString("orderID");
            time = bundle.getString("time");
            baberphone = bundle.getString("barphone");
            hairstyle = bundle.getString("hairstyle");
            distance = bundle.getString("distance");
            remark = bundle.getString("remark");
            addressStr = bundle.getString("address");
            String parts[] = addressStr.split("\\s");
            final String shop= parts[1];
            barberNameStr = bundle.getString("barberName");

            Map<String, String> paras = new HashMap<String, String>();
            paras.put("cusphone", phone);
            paras.put("cusname", name);
            paras.put("sex", sex);
            paras.put("barphone", baberphone);
            paras.put("hairstyle", hairstyle);
            paras.put("distance", distance);
            paras.put("time", time);
            paras.put("remark", remark);

            order = new OrderAccept();
            order.setPhone(phone);
            order.setAddress(addressStr);
            order.setBaber(barberNameStr);
            order.setShop(shop);

            for (Map.Entry<String,String> entry : paras.entrySet()){
                Log.d("CAO",entry.getKey()+" "+entry.getValue());
            }
            CustomRequest req = new CustomRequest(Request.Method.POST, ip + sumbit_url, paras, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    VisibiltyChange(view);                               //if submit succeed ,put the information into SharedPreferences

                    SharedPreferences countfile = getActivity().getSharedPreferences("countfile",0);         //countfile to count how many files should be there
                    SharedPreferences.Editor editor = countfile.edit();
                    int i = countfile.getInt("time",0)+1;
                    editor.putInt("time",i);
                    editor.commit();

                    SharedPreferences newfile = getActivity().getSharedPreferences(Integer.toString(i),0);   //the file whose number is i
                    Log.d("wtf i",i+"");
                    SharedPreferences.Editor neweditor = newfile.edit();
                    neweditor.putString("cusphone", phone);   Log.d("cusname!@#$%^&*",name);
                    neweditor.putString("cusname", name);
                    neweditor.putString("barbername",barberNameStr);
                    neweditor.putString("sex", sex);
                    neweditor.putString("orderID",orderID);
                    neweditor.putString("barberphone", baberphone);  Log.d("barberphone",baberphone);
                    neweditor.putString("hairstyle", hairstyle);
                    neweditor.putString("distance", distance);
                    neweditor.putString("time", time);              Log.d("time",time);
                    neweditor.putString("remark", remark);
                    neweditor.putString("address",addressStr);
                    neweditor.commit();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String errorMsg = VollyErrorHelper.getMessage(volleyError);
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                   Log.d("CAO",volleyError.toString());
                }
            });
            requestQueue.add(req);




        }
        else {      // come form quickBook
            order = this.getArguments().getParcelable("order");
            VisibiltyChange(view);
        }




        baberName = (TextView)view.findViewById(R.id.baber_name);
        baberPhone = (TextView)view.findViewById(R.id.baber_phone);
        address = (TextView)view.findViewById(R.id.address);
        shopName = (TextView)view.findViewById(R.id.baber_shop);

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

    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (!(activity instanceof GetFragment)) {
            throw new IllegalStateException("Fragment");
        }
        getFragment=(GetFragment) activity;
    }

    private void VisibiltyChange(View view){
        ViewGroup progressLayout = (ViewGroup) view.findViewById(R.id.progressbar_layout);
        progressLayout.setVisibility(View.GONE);
        ViewGroup orderLayout = (ViewGroup) view.findViewById(R.id.sucess_layout);
        orderLayout.setVisibility(View.VISIBLE);
    }
}
