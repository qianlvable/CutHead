package com.cuthead.app;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SignUpFragment extends Fragment {
    EditText etName;
    EditText etPhone;
    String name;
    String phone;
    private final int EMPTY_INFO_ERROR = 1;
    private final int NOT_VAILD_PHONE = 2;
    private final int VAILD_INFO = 0;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Button btn = (Button)view.findViewById(R.id.btn_sign_up);
        etName = (EditText)view.findViewById(R.id.et_user_name);
        etPhone = (EditText)view.findViewById(R.id.et_user_phone);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVaild() == VAILD_INFO) {
                    saveInfo();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }else if(EMPTY_INFO_ERROR == isVaild())
                    Toast.makeText(getActivity(),"用户名,电话不能为空!",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(),"电话号码不正确!",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    /** Check the input vaildation*/
    private int isVaild(){
        name = etName.getText().toString();
        phone = etPhone.getText().toString();

        if (name != null && phone != null
                && (!name.isEmpty() ) && (!phone.isEmpty())){
            if (phone.length() == 10)
                return VAILD_INFO;
            else
                return NOT_VAILD_PHONE;

        } else
            return EMPTY_INFO_ERROR;


    }

    /** Save userinfo to SharedPreferences */
    private void saveInfo(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.cuthead.app.sp", Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",name);
        editor.putString("phone",phone);
        editor.commit();
    }


}
