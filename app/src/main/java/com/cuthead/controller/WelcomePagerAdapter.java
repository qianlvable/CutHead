package com.cuthead.controller;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.cuthead.app.WelcomePagerOne;
import com.cuthead.app.WelcomePagerTwo;

/**
 * Created by Jiaqi Ning on 2014/7/25.
 */
public class WelcomePagerAdapter extends FragmentPagerAdapter {
    private final static int PAGE_NUMBER = 2;

    public WelcomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos){
            case 0:
                return new WelcomePagerOne();
            case 1:
                return new WelcomePagerTwo();

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }
}
