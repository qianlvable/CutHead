package com.cuthead.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cuthead.app.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Jiaqi Ning on 2014/7/31.
 */
public class CustomOrderSuccessCard extends Card{
    public CustomOrderSuccessCard(Context context) {
        super(context, R.layout.fragment_order_success);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
    }
}
