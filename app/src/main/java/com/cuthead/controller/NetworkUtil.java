package com.cuthead.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.cuthead.app.MainActivity;
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

    public static void setNetworkDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("嗷,没有网络无法预约,是否去设置?").setPositiveButton("设置",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                context.startActivity(intent);
            }
        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        }).show();
    }

    public static final boolean isGPSOn(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

       return gps;
    }
    public static void setGeoDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("预约提示").setMessage("嗷,我们发现您木有打开网络 or GPS这样没法预约哦").setPositiveButton("设置网络",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                context.startActivity(intent);
            }
        }).setNeutralButton("设置GPS",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                context.startActivity(intent);
            }
        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        }).show();
    }


}
