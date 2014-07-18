package com.cuthead.app;



import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cuthead.controller.HistoryCustomCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class UserInfoFragment extends Fragment {


    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.userinfo_fragment, container, false);

        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i =0;i<5;i++) {
            HistoryCustomCard card= new HistoryCustomCard(context,R.layout.custom_history_card);

            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

        CardListView listView = (CardListView) view.findViewById(R.id.history_list);

            listView.setAdapter(mCardArrayAdapter);
            Log.d("test","adapter set");


        return view;
    }


}
