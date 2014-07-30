package com.cuthead.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class NormalBookActivity extends ActionBarActivity {
    ProgressBar pb;
    Button btn;
    int pageCount = 1;
    public int year;
    private TextView tv_prog;
    private NBChoiceFragment nbChoiceFragment;
    private NBBarberFragment nbBarberFragment;
    private NBCommitFragment commitFragment;
    private NBTimeFragment nbTimeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_book);
        final FragmentManager fragmentManager = getFragmentManager();

        nbChoiceFragment = new NBChoiceFragment();
        nbBarberFragment = new NBBarberFragment();
        commitFragment = new NBCommitFragment();
        nbTimeFragment = new NBTimeFragment();


        pb = (ProgressBar)findViewById(R.id.progressBar);
        btn = (Button)findViewById(R.id.btn_nxt);
        tv_prog = (TextView) findViewById(R.id.tv_prog);
        FragmentTransaction inittransaction = fragmentManager.beginTransaction();//������choiceFragment���ȼ���
        inittransaction.add(R.id.fragment_container,nbChoiceFragment).commit();
        tv_prog.setText("选择发型与日期");                                          //��ʼTextView����
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                pb.incrementProgressBy(25);
                switch (pageCount){
                    case 1:
                        transaction.replace(
                                R.id.fragment_container,new NBProgressBarFragment() ).addToBackStack(null).commit();
                        tv_prog.setText("选择理发师");
                        break;
                    case 2:
                        transaction.replace(R.id.fragment_container,nbTimeFragment).addToBackStack(null).commit();
                        tv_prog.setText("选择预约时间");
                        break;
                    case 3:
                        transaction.replace(R.id.fragment_container,commitFragment).addToBackStack(null).commit();
                        tv_prog.setText("提交订单");
                        break;
                    default:
                        ;
                }
                pageCount++;

            }
        });


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

