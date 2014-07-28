package com.cuthead.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;


import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.cuthead.controller.WelcomePagerAdapter;


public class WelcomePageActivity extends Activity {
    ViewPager welcomePage;
    WelcomePagerAdapter myPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        FragmentManager manager = getFragmentManager();
        welcomePage = (ViewPager)findViewById(R.id.welcome_pager);
        myPageAdapter = new WelcomePagerAdapter(manager);
        welcomePage.setAdapter(myPageAdapter);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome_page, menu);
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
