package com.cuthead.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cuthead.app.R;
import com.cuthead.models.Barber;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Jiaqi Ning on 2014/7/28.
 */
public class CustomBaberCard extends Card{
    TextView mDistance;
    TextView mName;
    TextView mAddress;
    TextView mPhone;
    TextView mTime;
    ImageView mIcon;
    Barber mBarber;
    public CustomBaberCard(Context context,Barber barber){
        super(context, R.layout.custom_baber_card);
        mBarber = barber;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        mDistance = (TextView)parent.findViewById(R.id.tv_distance);
        mName = (TextView)parent.findViewById(R.id.tv_babaer_name);
        mAddress = (TextView)parent.findViewById(R.id.tv_baber_address);
        mIcon = (ImageView)parent.findViewById(R.id.iv_baber_icon);
        mPhone = (TextView)parent.findViewById(R.id.phone);
        mTime = (TextView)parent.findViewById(R.id.tv_book_time);

        mDistance.setText(mBarber.getDistance());
        mName.setText(mBarber.getName());
        mAddress.setText(mBarber.getAddress()+" "+mBarber.getShop());
        mPhone.setText(mBarber.getPhone());
        mTime.setText("预约时段: "+mBarber.getTime());


    }
}
