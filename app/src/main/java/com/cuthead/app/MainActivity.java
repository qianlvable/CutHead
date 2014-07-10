package com.cuthead.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cuthead.controller.DashboardAdapter;
import com.cuthead.models.LauncherIcon;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    static final LauncherIcon[] ICONS = {
            new LauncherIcon("快速预约",R.drawable.quick1),
            new LauncherIcon("普通预约",R.drawable.book1),
            new LauncherIcon("照旧剪",R.drawable.history1),
            new LauncherIcon("用户信息",R.drawable.user3),

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.dashboard_grid);
        gridview.setAdapter(new DashboardAdapter(this,ICONS));
        gridview.setOnItemClickListener(this);

        // Hack to disable GridView scrolling
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
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
               intent = new Intent(MainActivity.this,HistoryBookActivity.class);
               startActivity(intent);
               break;
           case 3:
               intent = new Intent(MainActivity.this,UserInfoActivity.class);
               startActivity(intent);
               break;
       }
    }
}
