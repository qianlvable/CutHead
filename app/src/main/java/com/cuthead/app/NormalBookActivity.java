package com.cuthead.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class NormalBookActivity extends Activity {
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
        pb = (ProgressBar)findViewById(R.id.progressBar);
        btn = (Button)findViewById(R.id.btn_nxt);
        tv_prog = (TextView) findViewById(R.id.tv_prog);

        nbChoiceFragment = new NBChoiceFragment();
        nbBarberFragment = new NBBarberFragment();
        commitFragment = new NBCommitFragment();
        nbTimeFragment = new NBTimeFragment();

        LinearLayout commitDialog = (LinearLayout)getLayoutInflater().inflate(R.layout.dialog_commit,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("提交订单");   //  make a dialog for commit function
        builder.setPositiveButton("提交",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new OrderSuccessFragment()).addToBackStack(null).commit();
                tv_prog.setText("感谢使用");
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        builder.setView(commitDialog);

        FragmentTransaction inittransaction = fragmentManager.beginTransaction();
        inittransaction.add(R.id.fragment_container,nbChoiceFragment).commit();
        tv_prog.setText("选择发型与日期");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                pb.incrementProgressBy(33);
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
                        builder.create().show();
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

