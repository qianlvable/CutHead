package com.cuthead.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cuthead.controller.AnimationUtil;
import com.cuthead.models.LauncherIcon;

import cn.jpush.android.api.JPushInterface;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class MainActivity extends Activity{

    boolean isFirstIn;

   View revealView;
    SharedPreferences preferences;
    int selectedIcon = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);


        revealView = findViewById(R.id.reveal_view);




        // To ensure first in to show the welcome page
        preferences = getSharedPreferences("com.cuthead.app.sp",MODE_APPEND);
        isFirstIn = preferences.getBoolean("isFirstIn",true);
        if (isFirstIn){
            Intent intent = new Intent(this,WelcomePageActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstIn",false);
            editor.apply();
        }

    }

    SupportAnimator.AnimatorListener revealAnimationListener = new SupportAnimator.AnimatorListener() {
        Intent intent;
        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {

            switch (selectedIcon){
                case 1:
                    intent = new Intent(MainActivity.this,QuickBookActivitiy.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(MainActivity.this,NormalBookActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(MainActivity.this,UserInfoActivity.class);
                    startActivity(intent);

            }

            selectedIcon = 0;


        }

        @Override
        public void onAnimationCancel() {

        }

        @Override
        public void onAnimationRepeat() {

        }
    };


    public void onClickIconView(View view){
        int clickColor = 0;
        switch (view.getId()){
            case R.id.icon_1:
                clickColor =  getResources().getColor(R.color.quick_bg_color);
                selectedIcon = 1;
                break;
            case R.id.icon_2:
                clickColor =  getResources().getColor(R.color.normal_bg_color);
                selectedIcon = 2;
                break;
            case R.id.icon_3:
                clickColor =  getResources().getColor(R.color.user_bg_color);
                selectedIcon = 3;

        }

        revealView.setBackgroundColor(clickColor);
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("x",location[0]);
        editor.putInt("y",location[1]);
        editor.apply();

        AnimationUtil.showRevealEffect(revealView,location[0],location[1],revealAnimationListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JPushInterface.stopPush(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // work around: to ensure the reveal view is invisible when user hit the back bottom
        revealView.setVisibility(View.INVISIBLE);
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

}
