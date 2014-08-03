package com.cuthead.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cuthead.models.Barber;
import com.cuthead.models.OrderAccept;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public static OrderAccept phraseOrderAcceptJson(JSONObject json){
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

    public static Barber phraseBaberInfo(JSONObject json){

        Barber barber = new Barber();
        try {
            barber.setAddress(json.getString("address"));
            barber.setName(json.getString("name"));
            barber.setTime(json.getString("time"));
            barber.setDistance(json.getInt("distance"));
            barber.setShop(json.getString("shop"));
            barber.setPhone(json.getString("phone"));
            barber.setTime(json.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return barber;

    }

    public static ArrayList<Barber> phraseBaerListFromJson(JSONObject json){
        try {
            JSONArray list = json.getJSONArray("barbers");
            ArrayList<Barber> result = new ArrayList<Barber>();
            for (int i = 0;i< list.length();i++){
                Barber barber = phraseBaberInfo(list.getJSONObject(i));
                result.add(barber);
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }


}
