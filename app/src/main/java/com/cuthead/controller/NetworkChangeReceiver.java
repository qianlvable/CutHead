package com.cuthead.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cuthead.app.R;

/**
 * Created by Jiaqi Ning on 2014/7/23.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetworkStatus.isNetworkConnected(context)){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View view = inflater.inflate(R.layout.custom_toast,null);
            ViewGroup layout = (ViewGroup)view.findViewById(R.id.toast_layout_root);


            Toast toast = new Toast(context);
            TextView text = (TextView)layout.findViewById(R.id.toast_text);
            text.setText("网络未连接!");
            toast.setDuration(3000);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.setView(layout);
            toast.show();
        }


    }
}
