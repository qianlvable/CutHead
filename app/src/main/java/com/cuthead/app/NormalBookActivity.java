package com.cuthead.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.GetFragment;


public class NormalBookActivity extends Activity implements GetFragment{

    private NBChoiceFragment nbChoiceFragment;
    private NBTimeFragment nbTimeFragment;
    private NBProgressBarFragment nbProgressBarFragment;
    private RequestQueue mRequestQuene;
    int fragmentNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_book);
        mRequestQuene = Volley.newRequestQueue(this);

        Log.d("fdfsssssssssss","   "+fragmentNumber);
        // Enabling Up Back navigation
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final FragmentManager fragmentManager = getFragmentManager();


        nbChoiceFragment = new NBChoiceFragment();

        FragmentTransaction inittransaction = fragmentManager.beginTransaction();                //add the first fragment
        inittransaction.add(R.id.fragment_container,nbChoiceFragment,"1").commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void onBackPressed(){

        FragmentManager fm = getFragmentManager();
      switch (fragmentNumber){
          case 0:
          case 5: this.finish();break;
          case 1: this.finish();break;
          case 2: super.onBackPressed();break;
          default: super.onBackPressed();
      }
        Log.d("fdf","   "+fragmentNumber);
    }


    @Override
    public void getNumber(int i) {
        fragmentNumber = i;
    }
}

