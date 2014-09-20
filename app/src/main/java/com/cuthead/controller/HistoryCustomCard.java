package com.cuthead.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cuthead.app.R;
import com.cuthead.app.RebookActivity;
import com.cuthead.models.HairStyle;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Jiaqi Ning on 2014/7/17.
 */
public class HistoryCustomCard extends Card{
    ImageView mIcon;
    TextView mBabarName;
    TextView mHairStyle;
    TextView mLocation;
    RatingBar mRatingBar;
    Button mButton;
    String barbername;
    String hairstyle;
    String location;
    SharedPreferences file;
    String filenumber;

    public void setFile(int file,Context context) {
        filenumber = Integer.toString(file);
        this.file = context.getSharedPreferences(filenumber, 0);
        setBarbername();
        setHairstyle();
        setLocation();
    }


    public String getBarbername() {
        return barbername;
    }



    public String getHairstyle() {
        return hairstyle;
    }



    public String getLocation() {
        return location;
    }



    public void setBarbername() {
        this.barbername = file.getString("barbername","张三");
    }
    public void setHairstyle() {
        this.hairstyle = file.getString("hairstyle","剪发");
    }
    public void setLocation() {
        this.location = file.getString("address","吉大南校");
    }


    public HistoryCustomCard(Context context) {
        super(context, R.layout.custom_history_card);
    }

    public HistoryCustomCard(Context context,int innerLayout){
        super(context,innerLayout);

    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);

        mBabarName = (TextView)parent.findViewById(R.id.tv_babaer_name_history);
        mHairStyle = (TextView)parent.findViewById(R.id.tv_hair_style_history);
        mLocation = (TextView)parent.findViewById(R.id.tv_history_address);
        mIcon = (ImageView)parent.findViewById(R.id.iv_history_icon);
        mRatingBar = (RatingBar)parent.findViewById(R.id.ratingBar_history);
        mButton = (Button)parent.findViewById(R.id.btn_history_book);

        mBabarName.setText(getBarbername());
        mHairStyle.setText(HairStyle.getHair(getHairstyle()));

        mLocation.setText(getLocation());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RebookActivity.class);
                intent.putExtra("filenumber",filenumber);
                getContext().startActivity(intent);
            }
        });

    }


}
