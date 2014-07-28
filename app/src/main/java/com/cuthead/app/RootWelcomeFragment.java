package com.cuthead.app;



import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RootWelcomeFragment extends Fragment {


    public RootWelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_welcome, container, false);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.root_container,new WelcomePageThree()).commit();
        return view;
    }


}
