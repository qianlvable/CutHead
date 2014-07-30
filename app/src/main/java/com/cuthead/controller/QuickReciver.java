package com.cuthead.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cuthead.models.OrderAccept;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class QuickReciver extends BroadcastReceiver {
    public QuickReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Log.d("TEST_JPUSH","[QuickReciver] onReceive " + intent.getAction()+ printBundle(bundle));
        String jsonstr = bundle.getString("cn.jpush.android.MESSAGE");
        try {
            JSONObject json = new JSONObject(jsonstr);
            OrderAccept order = new OrderAccept();
            order.setAddress(json.getString("address"));
            order.setBaber(json.getString("baber"));
            order.setDistance(json.getDouble("distance"));
            order.setOrderID("orderID");
            order.setPhone(json.getString("phone"));
            order.setTime(json.getString("date "));
            order.setShop(json.getString("shop"));


        } catch (JSONException e) {
            Log.d("Phase json","Pharse exception!");
        }

    }
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
