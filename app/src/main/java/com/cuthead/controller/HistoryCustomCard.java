package com.cuthead.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cuthead.app.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by Jiaqi Ning on 2014/7/17.
 */
public class HistoryCustomCard extends Card{
    ImageView mIcon;
    TextView mBabarName;
    TextView mHairStyle;
    TextView mLocation;
    RatingBar mRatingBar;


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

    }


}
