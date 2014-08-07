package com.cuthead.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class NormalBookActivity extends Activity {
    ProgressBar pb;
    Button btn;
    int pageCount = 1;
    public int year;
    private TextView tv_prog;
    private NBChoiceFragment nbChoiceFragment;
    private NBBaberListFragment nbBarberFragment;
    private NBCommitFragment commitFragment;
    private NBTimeFragment nbTimeFragment;
    private NBProgressBarFragment nbProgressBarFragment;
    private RequestQueue mRequestQuene;
    private int Year,Month,Day;
    private int Style;
    Fragment tempFragment;
    String url;
    double longitude;
    double lantitude;
    String orderID;
    String finalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_book);
        mRequestQuene = Volley.newRequestQueue(this);

        final FragmentManager fragmentManager = getFragmentManager();
        pb = (ProgressBar)findViewById(R.id.progressBar);
        tv_prog = (TextView) findViewById(R.id.tv_prog);

        nbChoiceFragment = new NBChoiceFragment();

        FragmentTransaction inittransaction = fragmentManager.beginTransaction();                //add the first fragment
        inittransaction.add(R.id.fragment_container,nbChoiceFragment,"1").commit();
        tv_prog.setText("选择发型与日期");
        Log.d("初始化",Integer.toBinaryString(pageCount));


    }

    public void onBackPressed() {

        super.onBackPressed();
        pageCount--;
        Log.e("count",""+pageCount);

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

