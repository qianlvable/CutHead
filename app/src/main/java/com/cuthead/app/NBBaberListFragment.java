package com.cuthead.app;



import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NBBaberListFragment extends Fragment {


    public NBBaberListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nbbaber_list, container, false);
        Context context = getActivity();
        CardListView listView = (CardListView)view.findViewById(R.id.baber_list);
       // listView.setEmptyView();
        return view;
    }


}
