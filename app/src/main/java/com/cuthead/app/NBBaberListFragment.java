package com.cuthead.app;



import android.app.Fragment;
import android.os.Bundle;
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
    String orderID;
    ArrayList<Card> cards;
    CardArrayAdapter mCardArrayAdapter;
    private ViewGroup indicatorLayout;
    private TextView dot1;
    private TextView dot2;
    private ImageView bar;

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


        Bundle bundle = this.getArguments();
        try {
            JSONObject jsonObject = new JSONObject(bundle.getString("BABER_LIST"));
            orderID = bundle.getString("ORDER_ID");
            ArrayList<Barber> barbers = NetworkUtil.phraseBaerListFromJson(jsonObject);
            cards = new ArrayList<Card>();

            for (Barber b : barbers){
               cards.add(new CustomBaberCard(getActivity(),b));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        CardListView listView = (CardListView)view.findViewById(R.id.baber_list);
       // listView.setEmptyView();
        mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        listView.setAdapter(mCardArrayAdapter);


        // When click open time choose fragment and put the baber phone and orderId,time to next fragment
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                TextView phoneView  = (TextView)view.findViewById(R.id.phone);
                String phone = phoneView.getText().toString();
                TextView timeView  = (TextView)view.findViewById(R.id.tv_book_time);
                String[] timeStr = timeView.getText().toString().split("\\s");
                String time = timeStr[1];
                Fragment timeFragment = new NBTimeFragment();
                Bundle bget = getArguments();                                                     //get data from latest fragment
                String hairstyle = bget.getString("hairstyle");
                String remark = bget.getString("remark");
                String date = bget.getString("date");

                Bundle bundle = new Bundle();
                bundle.putString("choice_phone",phone);
                bundle.putString("orderID",orderID);
                bundle.putString("time",time);
                bundle.putString("hairstyle",hairstyle);
                bundle.putString("remark",remark);
                bundle.putString("date",date);
                timeFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,timeFragment).commit();

            }
        });


        return view;
    }


}
