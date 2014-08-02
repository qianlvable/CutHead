package com.cuthead.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cuthead.controller.QuickReciver;


public class QuickBookActivitiy extends Activity {
private QuickReciver mQuickReciver;
        public static final String MESSAGE_RECEIVED_ACTION = "com.cuthead.controller.MESSAGE_RECEIVED";


    @Override
    protected void onResume() {
        super.onResume();
        registerMessageReceiver();
        Log.d("JPUSH TEST","register finish");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_book_activitiy);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(R.id.qb_container,new QBRequestFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mQuickReciver);
    }

    public void registerMessageReceiver() {
        mQuickReciver = new QuickReciver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction("cn.jpush.android.intent.REGISTRATION" );
        filter.addAction("cn.jpush.android.intent.MESSAGE_RECEIVED");
        filter.addAction("cn.jpush.android.intent.NOTIFICATION_RECEIVED" );
        filter.addAction("cn.jpush.android.intent.NOTIFICATION_OPENED" );
        filter.addAction("cn.jpush.android.intent.ACTION_RICHPUSH_CALLBACK");
        filter.addAction("cn.jpush.android.intent.CONNECTION");
        filter.addCategory("com.cuthead.app");
        registerReceiver(mQuickReciver, filter);
        registerReceiver(mQuickReciver,filter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quick_book_activitiy, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
