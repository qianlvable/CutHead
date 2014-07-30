package com.cuthead.app;

/**
 * Created by asus on 2014/7/14.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class NormalBookActivity extends Activity{
    ProgressBar pb;
    Button btn;
    int pageCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_book);
        final FragmentManager fragmentManager = getFragmentManager();


        pb = (ProgressBar)findViewById(R.id.progressBar);
        btn = (Button)findViewById(R.id.btn_nxt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                pb.incrementProgressBy(20);
                switch (pageCount){
                    case 1:
                        transaction.add(R.id.nb_container,new NBProgressBarFragment()).commit();
                        break;
                    case 2:
                        //transaction.replace(R.id.fragment_container,new FragmentTwo()).addToBackStack(null).commit();
                        break;
                    case 3:
                       // transaction.replace(R.id.fragment_container,new FragmentThree()).addToBackStack(null).commit();
                        break;
                    default:
                        ;
                }
                pageCount++;

            }
        });

    }
}
