package com.cuthead.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.cuthead.controller.CustomOrderSuccessCard;
import com.cuthead.controller.TimeUtil;
import com.cuthead.models.MyTimeMark;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;


public class HistoryBookActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_book);

        Card card = new CustomOrderSuccessCard(this);
        CardView cardView = (CardView)findViewById(R.id.carddemo_thumb_url);
        cardView.setCard(card);

        String testtime = "6:20-6:40-7:40-12:00-15:40-16:20";

        ArrayList<MyTimeMark> time = new ArrayList<MyTimeMark>(TimeUtil.getAvailableTime(TimeUtil.pharseTimeString(testtime)));
        for (MyTimeMark str : time){
            Log.d("Test Time",str.toString());

        }

    }





}

