package com.cuthead.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cuthead.controller.GetFragment;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by shixu on 2014/9/19.
 */
public class RebookActivity extends Activity implements GetFragment{

    String filenumber;
    int fragmentNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebook);

        Intent intent = getIntent();
        filenumber = intent.getStringExtra("filenumber");

        Log.d("wtf i",filenumber);
        Bundle bundle = new Bundle();
        bundle.putString("filenumber",filenumber);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RebookProgressBarFragment rebookProgressBarFragment = new RebookProgressBarFragment();
        rebookProgressBarFragment.setArguments(bundle);
        ft.replace(R.id.rebook_container,rebookProgressBarFragment).addToBackStack(null).commit();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JPushInterface.stopPush(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void onBackPressed(){
        switch (fragmentNumber){
            case 0:
            case 1:
            case 5:
                default: this.finish();

        }
    }

    @Override
    public void getNumber(int i) {
        fragmentNumber = i;
    }
}
