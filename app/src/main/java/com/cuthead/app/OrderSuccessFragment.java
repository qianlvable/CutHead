package com.cuthead.app;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cuthead.models.OrderAccept;

import it.gmariotti.cardslib.library.internal.Card;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class OrderSuccessFragment extends Fragment {


    public OrderSuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_success, container, false);
        //OrderAccept order = this.getArguments().getParcelable("order");

        return view;
    }


}
