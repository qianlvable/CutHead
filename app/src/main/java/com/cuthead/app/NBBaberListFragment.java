package com.cuthead.app;



import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cuthead.controller.CustomBaberCard;
import com.cuthead.controller.NetworkUtil;
import com.cuthead.models.Barber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NBBaberListFragment extends Fragment {
    ArrayList<Card> cards;
    CardArrayAdapter mCardArrayAdapter;
    private ViewGroup indicatorLayout;
    private TextView dot1;
    private TextView dot2;
    private ImageView bar;
    String orderID;
    String hairstyle;
    String remark;
    String date;

    public NBBaberListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nbbaber_list, container, false);
        indicatorLayout = (RelativeLayout)view.findViewById(R.id.indicator2_2);
        bar = (ImageView)indicatorLayout.findViewById(R.id.phase1_bar);
        bar.setImageResource(R.drawable.progress_indicate_bar);
        dot1 = (TextView)indicatorLayout.findViewById(R.id.phase1_dot);
        dot1.setBackgroundResource(R.drawable.progress_bar_mark);
        dot2 = (TextView)indicatorLayout.findViewById(R.id.phase1_dot);
        dot2.setBackgroundResource(R.drawable.progress_bar_mark);


        Bundle bundleget = this.getArguments();
        try {
            JSONArray jsonArray= new JSONArray(bundleget.getString("barberlist"));

            ArrayList<Barber> barbers = NetworkUtil.phraseBaerListFromJson(jsonArray);
            cards = new ArrayList<Card>();

            for (Barber b : barbers){
                Log.d("WTF",b.toString());
                Card card = new CustomBaberCard(getActivity(),b);
                card.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        TextView phoneView  = (TextView)view.findViewById(R.id.phone);
                        String barphone = phoneView.getText().toString();
                        TextView timeView  = (TextView)view.findViewById(R.id.tv_book_time);
                        String[] timeStr = timeView.getText().toString().split("\\s");
                        TextView addressView = (TextView)view.findViewById(R.id.tv_baber_address);
                        String address = addressView.getText().toString();
                        String time = timeStr[1];
                        TextView nameView = (TextView)view.findViewById(R.id.tv_babaer_name);
                        String barberName = nameView.getText().toString();

                        Fragment timeFragment = new NBTimeFragment();
                        Bundle bundleget = getArguments();                                                     //get data from latest fragment
                        hairstyle = bundleget.getString("hairstyle");
                        remark = bundleget.getString("remark");
                        date = bundleget.getString("date");

                        Bundle bundle = new Bundle();
                        bundle.putString("barphone",barphone);
                        bundle.putString("barberName",barberName);
                        bundle.putString("time",time);
                        bundle.putString("hairstyle",hairstyle);
                        bundle.putString("address",address);
                        bundle.putString("remark",remark);
                        bundle.putString("date",date);
                        bundle.putString("distance","0");             /**    distance              */
                        timeFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,timeFragment).addToBackStack(null).commit();
                    }
                });
               cards.add(card);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        CardListView listView = (CardListView)view.findViewById(R.id.baber_list);
        mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        listView.setAdapter(mCardArrayAdapter);



        // When click open time choose fragment and put the baber phone and orderId,time to next fragment



        return view;
    }


}
