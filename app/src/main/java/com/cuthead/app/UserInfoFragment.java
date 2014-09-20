package com.cuthead.app;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cuthead.controller.HistoryCustomCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class UserInfoFragment extends Fragment {


    ImageButton im_set;
    ImageButton im_done;
    EditText et_username;
    EditText et_userphone;
    String username;
    String userphone;
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

        SharedPreferences countfile = getActivity().getSharedPreferences("countfile",0);
        SharedPreferences.Editor counteditor = countfile.edit();
        int n = countfile.getInt("time",0);
//***************************************************************************
/*
        SharedPreferences file = getActivity().getSharedPreferences("1", 0);
        SharedPreferences.Editor meditor = file.edit();
        meditor.putString("time","1");
        meditor.commit();
        Log.d("haha","1号创建");

        SharedPreferences file2 = getActivity().getSharedPreferences("2",0);
        SharedPreferences.Editor meditor2 = file2.edit();
        meditor2.putString("time","2");
        meditor2.commit();
        Log.d("haha","1号创建");

        SharedPreferences file3 = getActivity().getSharedPreferences("3",0);
        SharedPreferences.Editor meditor3 = file3.edit();
        meditor3.putString("time","3");
        meditor3.commit();
        Log.d("haha","1号创建");*/


        for (int i=1;i<=n;i++) {                                                   /**from 1 to begin count */
            HistoryCustomCard card= new HistoryCustomCard(context,R.layout.custom_history_card);
            TextView tv = (TextView) view.findViewById(R.id.userinfo_tv);
            tv.setVisibility(View.GONE);                                             //  make the textview gone
            card.setFile(i,getActivity());
            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

        CardListView listView = (CardListView) view.findViewById(R.id.history_list);

            listView.setAdapter(mCardArrayAdapter);
            Log.d("test","adapter set");
        SharedPreferences userfile = getActivity().getSharedPreferences("com.cuthead.app.sp",0);
        String userinfo = userfile.getString("USER_INFO","empty");
        if (!userinfo.equals("empty")) {
            String[] val = userinfo.split(";");
            username = val[0];
            userphone = val[1];
        }
        final SharedPreferences.Editor editor = userfile.edit();
        /*
        final String username = userfile.getString("username","新用户");
        final String userphone = userfile.getString("userphone","00000000000");*/

        et_username = (EditText) view.findViewById(R.id.et_user_name);et_username.setEnabled(false); et_username.setText(username);
        et_userphone = (EditText) view.findViewById(R.id.et_user_phone);et_userphone.setEnabled(false);et_userphone.setText(userphone);

        im_set = (ImageButton) view.findViewById(R.id.im_set);
        im_done = (ImageButton) view.findViewById(R.id.im_done);

        im_done.setVisibility(View.GONE);
        im_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setEnabled(true);
                et_userphone.setEnabled(true);
                im_set.setVisibility(View.GONE);
                im_done.setVisibility(View.VISIBLE);
                Log.e("hahahahaha","dgdfgfdgfdgfdg");
            }
        });
        im_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String innerusername = et_username.getText().toString();
                String inneruserphone = et_userphone.getText().toString();
                if(innerusername.equals("")){
                    innerusername = username;
                    Toast.makeText(getActivity(),"用户名为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(inneruserphone.length() != 11){
                    inneruserphone = userphone;
                    Toast.makeText(getActivity(),"手机号码格式不正确！",Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putString("username",innerusername);
                editor.putString("userphone",inneruserphone);
                editor.commit();
                im_done.setVisibility(View.GONE);
                im_set.setVisibility(View.VISIBLE);
                et_username.setEnabled(false);
                et_userphone.setEnabled(false);
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return view;
    }


}
