package com.cuthead.app;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;



public class UserInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container_userinfo);

        if (fragment == null){
            fragment = new UserInfoFragment();
            manager.beginTransaction().add(R.id.fragment_container_userinfo,fragment)
                    .commit();

        }

    }

}
