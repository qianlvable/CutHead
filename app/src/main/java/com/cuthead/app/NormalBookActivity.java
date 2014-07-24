package com.cuthead.app;

/**
 * Created by asus on 2014/7/14.
 */

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


import com.cuthead.controller.AlertDialogRadio;
import com.cuthead.controller.AlertDialogRadio.AlertPositiveListener;


/** Since this class attaches the dialog fragment "AlertDialogRadio",
 *  it is suppose to implement the interface "AlertPositiveListener"
 */
public class NormalBookActivity extends Activity implements AlertPositiveListener {
    /** Stores the selected item's position */
    int position = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_book);

      //*****************************华丽的分割线****************************
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Getting the fragment manager */
                FragmentManager manager = getFragmentManager();

                /** Instantiating the DialogFragment class */
                AlertDialogRadio alert = new AlertDialogRadio();

                /** Creating a bundle object to store the selected item's index */
                Bundle b  = new Bundle();

                /** Storing the selected item's index in the bundle object */
                b.putInt("position", position);

                /** Setting the bundle object to the dialog fragment object */
                alert.setArguments(b);

                /** Creating the dialog fragment object, which will in turn open the alert dialog window */
                alert.show(manager, "alert_dialog_radio");
            }
        };

        /** Getting the reference of the button from the main layout */
        //Button btn = (Button) findViewById(R.id.btn_choose);
       // EditText et = (EditText) findViewById(R.id.et_normal_book);

        /** Setting a button click listener for the choose button */
     //   et.setOnClickListener(listener);
    }

    /** Defining button click listener for the OK button of the alert dialog window */

    public void onPositiveClick(int position) {
        this.position = position;

        /** Getting the reference of the textview from the main layout */
     //   TextView tv = (TextView) findViewById(R.id.et_normal_book);

        /** Setting the selected android version in the textview */
        //tv.setText(HairStyle.code[this.position]);
    }
}
