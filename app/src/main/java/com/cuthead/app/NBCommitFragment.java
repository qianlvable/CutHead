package com.cuthead.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shixu on 2014/7/28.
 */
public class NBCommitFragment extends Fragment{

    private View mView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_nb_commit, container, false);



        return mView;
    }
}
