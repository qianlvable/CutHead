package com.cuthead.app;



import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cuthead.controller.CircularImageView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class WelcomePageThree extends Fragment {


    public WelcomePageThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page_three, container, false);


        CircularImageView maleIcon = (CircularImageView)view.findViewById(R.id.male_icon);
        maleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.root_container,new SignUpFragment()).commit();
            }
        });

        CircularImageView femaleIcon = (CircularImageView)view.findViewById(R.id.female_icon);
        femaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.root_container,new SignUpFragment()).commit();
            }
        });
        return view;
    }


}
