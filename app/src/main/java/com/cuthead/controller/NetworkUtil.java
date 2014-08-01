package com.cuthead.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cuthead.models.OrderAccept;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jiaqi Ning on 2014/7/28.
 */
public class NetworkUtil {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

    public static OrderAccept pharseOrderAcceptJson(JSONObject json){
        OrderAccept order = new OrderAccept();
        try {
            order.setAddress(json.getString("address"));
            order.setBaber(json.getString("baber"));
            order.setDistance(json.getDouble("distance"));
            order.setOrderID("orderID");
            order.setPhone(json.getString("phone"));
            order.setTime(json.getString("date "));
            order.setShop(json.getString("shop"));
            return order;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
