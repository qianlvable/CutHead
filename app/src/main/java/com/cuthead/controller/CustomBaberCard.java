package com.cuthead.controller;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cuthead.app.EmptyBarberList;
import com.cuthead.app.NBBaberListFragment;
import com.cuthead.app.NormalBookActivity;
import com.cuthead.app.R;
import com.cuthead.models.Barber;
import com.cuthead.models.Comment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Jiaqi Ning on 2014/7/28.
 */
public class CustomBaberCard extends Card{
    TextView mDistance;
    TextView mName;
    TextView mAddress;
    TextView mPhone;
    TextView mTime;
    ImageView mIcon;
    Barber mBarber;
    private RequestQueue mRequestQueue;
    String url= "/barber/getcomment/";
    String ip = "http://123.57.13.137";
    String murl = "http://123.57.13.137/barber/getcomment/";
    private List<Comment> commentList = new ArrayList<Comment>();
/*
    final String ip = "http://123.57.13.137";
    final String normal_url = "/appointment/normal/";*/

    Button mInfo;
    public CustomBaberCard(Context context,Barber barber){
        super(context, R.layout.custom_baber_card);
        mBarber = barber;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        mDistance = (TextView)parent.findViewById(R.id.tv_distance);
        mName = (TextView)parent.findViewById(R.id.tv_babaer_name);
        mAddress = (TextView)parent.findViewById(R.id.tv_baber_address);
        mIcon = (ImageView)parent.findViewById(R.id.iv_baber_icon);
        mPhone = (TextView)parent.findViewById(R.id.phone);
        mTime = (TextView)parent.findViewById(R.id.tv_book_time);
        mInfo = (Button)parent.findViewById(R.id.baber_info_button);

        mDistance.setText(String.valueOf(mBarber.getDistance())+" 米");
        mName.setText(mBarber.getName());
        mAddress.setText(mBarber.getAddress()+" "+mBarber.getShop());
        mPhone.setText(mBarber.getPhone());
        mTime.setText("预约时段: "+mBarber.getTime());
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("testinfo",mBarber.getPhone());
                mRequestQueue = Volley.newRequestQueue(mContext);
                Map<String,String> para = new HashMap<String, String>();
                para.put("phone",mBarber.getPhone());
                CustomRequest req = new CustomRequest(Request.Method.POST,ip+url,para,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            int code = json.getInt("code");
                            if (code == 100){
                                String data = json.getString("data");
                                Log.e("data",data);
                                if (data == null || data.isEmpty() || data.equals("null") || data.equals("[]")){
                                    Toast.makeText(mContext,"暂时没有评论",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String info[] = data.split(",");
                                String name = "user";
                                String content = "content";
                                String time = "0000";
                                for(int i=0;i<info.length;i++){
                                    Log.d("infotest",info[i]);
                                    switch (i%3){
                                        case 0:
                                            String tempname[] = info[i].split("\"");
                                            name = tempname[3];
                                            break;
                                        case 1:
                                            String tempcontent[] = info[i].split("\"");
                                            content = tempcontent[3];
                                            break;
                                        case 2:
                                            String temptime[] = info[i].split("\"");
                                            time = temptime[3];
                                            Comment comment = new Comment(R.drawable.user_icon,5,name,content,time);
                                            commentList.add(comment);
                                    }

                                }
                                ContentAdapter adapter = new ContentAdapter(mContext,R.layout.card_comment,commentList);

                                //创建dialog的界面
                                LinearLayout layout = new LinearLayout(mContext);
                                layout.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                ListView listView = new ListView(mContext);
                                listView.setAdapter(adapter);
                                layout.addView(listView);

                                //创建dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setView(layout);
                                alertDialog.show();

                                //调整dialog大小
                                DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
                                Window dialogWindow = alertDialog.getWindow();
                                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                                lp.width = dm.widthPixels; // 宽度
                                lp.height = 6*dm.heightPixels/7; // 高度
                                //lp.alpha = 0.7f; // 透明度
                                dialogWindow.setAttributes(lp);

                            } else {
                                Log.d("unknown errors","errors");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String errorMsg = VollyErrorHelper.getMessage(volleyError);
                        Toast.makeText(mContext,errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                mRequestQueue.add(req);


            }
        });


    }


}
