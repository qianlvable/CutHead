package com.cuthead.app;



import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.CustomRequest;
import com.cuthead.controller.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SubmitFragment extends Fragment {
    EditText etName;
    EditText etPhone;
    String name;
    String phone;
    String sex;
    Button btn;
    Spinner spinner;
    TextView phoneTitle;
    TextView nameTitle;
    RequestQueue mRequestQueue;
    int flag;
    private final int EMPTY_INFO_ERROR = 1;
    private final int NOT_VAILD_PHONE = 2;
    private final int VAILD_INFO = 0;
    boolean haveUsed = false;
    final String url = null;
    boolean firstInto = true;
    public SubmitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit, container, false);
        Bundle bundle = getArguments();
        flag = bundle.getInt("flag");

        spinner = (Spinner)view.findViewById(R.id.spiner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.sex_array,android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


        btn = (Button)view.findViewById(R.id.btn_submit);
        etName = (EditText)view.findViewById(R.id.et_user_name);
        etPhone = (EditText)view.findViewById(R.id.et_user_phone);
        phoneTitle = (TextView)view.findViewById(R.id.tv2);
        nameTitle = (TextView)view.findViewById(R.id.tv1);



        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (isVaildPhoneInput() == VAILD_INFO) {
                        checkRegister();
                        if (flag==0)
                            setJpushAlias();
                    }
                    else if (firstInto){
                        firstInto = false;
                    }
                    else {
                        Toast.makeText(getActivity(), "电话号码输入错误!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                if (name != null && !name.isEmpty()) {
                    saveInfo();
                    FragmentManager fm = getFragmentManager();
                    if (flag == 0)
                        fm.beginTransaction().replace(R.id.qb_container,new QBProgressWheelFragment()).commit();
                    else
                        ;// normal book thing
                }
                else
                    Toast.makeText(getActivity(),"还不知道你叫啥呢",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    /** Check the input vaildation*/
    private int isVaildPhoneInput(){
        name = etName.getText().toString();
        phone = etPhone.getText().toString();

        if (phone != null && (!phone.isEmpty()))
            if (phone.length() == 11)
                return VAILD_INFO;


        return NOT_VAILD_PHONE;

    }

    private void setJpushAlias(){
        JPushInterface.setAlias(getActivity(), phone, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String alias, Set<String> tags) {
                if (code == 60020) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    if (NetworkUtil.isNetworkConnected(getActivity()))
                        builder.setMessage("联网超时,稍后讲重试");
                    else builder.setMessage("您未联网,请开启流量!");

                    builder.setCancelable(true);
                    builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 开启设置
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });
    }

    /**Check whether the phone number has been register,and set the edit name to be visible and enable the button*/
    private boolean checkRegister(){
        phone = etPhone.getText().toString();

        mRequestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> paras = new HashMap<String, String>();
        paras.put("phone",phone);
        CustomRequest req = new CustomRequest(Request.Method.POST,url,paras,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject object) {
                try {
                    phoneTitle.animate().alpha(0.15f);
                    etPhone.animate().alpha(0.15f);
                    nameTitle.animate().alpha(1);
                    etName.setEnabled(true);
                    etName.animate().alpha(1);
                    spinner.animate().alpha(1);
                    btn.setEnabled(true);

                    boolean exist = object.getBoolean("exists");
                    if (exist) {
                        object.getString("sex");
                        etName.setText(object.getString("name"));

                        if (object.getString("sex").equals("male"))
                            spinner.setSelection(0);
                        else
                            spinner.setSelection(1);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    boolean exist = false;
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                phoneTitle.animate().alpha(0.15f);
                etPhone.animate().alpha(0.15f);
                nameTitle.animate().alpha(1);
                etName.setEnabled(true);
                etName.animate().alpha(1);
                spinner.animate().alpha(1);
                btn.setEnabled(true);
            }
        });
        mRequestQueue.add(req);

        return false;
    }

    /** Save userinfo to SharedPreferences */
    private void saveInfo(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.cuthead.app.sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",name);
        editor.putString("phone",phone);

        editor.commit();
    }


}
