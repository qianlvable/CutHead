package com.cuthead.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cuthead.controller.CardDashboardAdapter;
import com.cuthead.controller.DashboardAdapter;
import com.cuthead.models.LauncherIcon;

import cn.jpush.android.api.JPushInterface;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener{

    boolean isFirstIn;

    static final LauncherIcon[] ICONS = {
            new LauncherIcon("快速预约",R.drawable.icon_quick,Color.rgb(247,218,129),Color.rgb(244,187,28)),
            new LauncherIcon("普通预约",R.drawable.icon_normal,Color.rgb(239,133,99),Color.rgb(234,86,42)),
            new LauncherIcon("用户信息",R.drawable.icon_user,Color.rgb(76,175,80),Color.rgb(56,142,60)),
            new LauncherIcon("添加",R.drawable.icon_add,Color.rgb(98,195,212),Color.rgb(39,180,200)),

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_layout);


        // To ensure first in to show the welcome page
        SharedPreferences preferences = getSharedPreferences("com.cuthead.app.sp",MODE_APPEND);
        isFirstIn = preferences.getBoolean("isFirstIn",true);
        if (isFirstIn){
            Intent intent = new Intent(this,WelcomePageActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstIn",false);
            editor.apply();
        }

/*
       // GridView gridview = (GridView) findViewById(R.id.dashboard_grid);
        GridView gridview = (GridView) findViewById(R.id.new_grid);
        gridview.setAdapter(new CardDashboardAdapter(this,ICONS));
        gridview.setOnItemClickListener(this);

        // Hack to disable GridView scrolling
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
        */
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;

       switch (i){

           case 0:
               intent = new Intent(MainActivity.this,QuickBookActivitiy.class);

               startActivity(intent);
               break;
           case 1:
               intent = new Intent(MainActivity.this,NormalBookActivity.class);
               startActivity(intent);
               break;
           case 2:
               intent = new Intent(MainActivity.this,UserInfoActivity.class);
               startActivity(intent);
               break;
           case 3:
               Toast.makeText(this,"攻城师正在努力...",Toast.LENGTH_LONG).show();
               break;

       }
    }
}
