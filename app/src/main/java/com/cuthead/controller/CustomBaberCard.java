package com.cuthead.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cuthead.app.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Jiaqi Ning on 2014/7/28.
 */
public class CustomBaberCard extends Card{
    TextView mDistance;
    TextView mName;
    TextView mAddress;
    ImageView mIcon;
    public CustomBaberCard(Context context){
        super(context, R.layout.custom_baber_card);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        mDistance = (TextView)parent.findViewById(R.id.tv_distance);
        mName = (TextView)parent.findViewById(R.id.tv_babaer_name);
        mAddress = (TextView)parent.findViewById(R.id.tv_baber_address);
        mIcon = (ImageView)parent.findViewById(R.id.iv_baber_icon);
    }
}
