package com.cuthead.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cuthead.app.R;
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

            Intent i = new Intent();
            i.putExtra("flag",true);
            i.putExtra("order",order);
            i.setClassName("com.cuthead.app","com.cuthead.app.QuickBookActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.drawable.ic_notification, "Hello", when);
            CharSequence contentTitle = "您有新的消息";
            CharSequence contentText = "订单已被接受!";
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
            notification.defaults |= Notification.DEFAULT_SOUND;
            mNotificationManager.notify(1,notification);


            //context.startActivity(i);
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
