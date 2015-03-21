package com.cuthead.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.cuthead.controller.GetFragment;
import com.cuthead.controller.NetworkUtil;
import com.cuthead.controller.QuickReciver;
import com.cuthead.models.OrderAccept;


public class QuickBookActivitiy extends Activity implements GetFragment{
    private QuickReciver mQuickReciver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.cuthead.controller.MESSAGE_RECEIVED";
    private final int QUICKBOOK_FLAG = 0;
    int fragmentNumber = 1;

    @Override
    protected void onResume() {
        super.onResume();
        registerMessageReceiver();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mQuickReciver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the progresswheel in the actionbar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_quick_book);

        // Set the NetworkSetting dialog
        if (!NetworkUtil.isNetworkConnected(this)){
            NetworkUtil.setNetworkDialog(this);
        }

        // Enabling Up Back navigation
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Handle the situation that QuickRecevier open the activity
        Intent intent = getIntent();
        boolean receive = intent.getBooleanExtra("flag",false);
        if (receive) {
            OrderAccept order = intent.getParcelableExtra("order");
            FragmentManager fm = getFragmentManager();
            Fragment fragment = new OrderSuccessFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("order",order);
            bundle.putInt("flag_order", QUICKBOOK_FLAG);
            fragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.qb_container,fragment).commit();
        }

        else {
            Fragment fragment = new QuickMapFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().add(R.id.qb_container, fragment).commit();
        }
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
        // automatically handle clicks on the Home/Up button, so long+
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getNumber(int i) {
        fragmentNumber = i;
    }

    private void startHideRevealEffect(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.cuthead.app.sp",MODE_APPEND);
        int cx = sharedPreferences.getInt("x",0);
        int cy = sharedPreferences.getInt("y",0);
        if (cx != 0 && cy != cy){

        }
    }


}
