package com.cuthead.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class NormalBookActivity extends Activity {

    private NBChoiceFragment nbChoiceFragment;


    private NBTimeFragment nbTimeFragment;
    private NBProgressBarFragment nbProgressBarFragment;
    private RequestQueue mRequestQuene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_book);
        mRequestQuene = Volley.newRequestQueue(this);

        // Enabling Up Back navigation
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final FragmentManager fragmentManager = getFragmentManager();


        nbChoiceFragment = new NBChoiceFragment();

        FragmentTransaction inittransaction = fragmentManager.beginTransaction();                //add the first fragment
        inittransaction.replace(R.id.fragment_container,nbChoiceFragment,"1").addToBackStack(null).commit();

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

}

