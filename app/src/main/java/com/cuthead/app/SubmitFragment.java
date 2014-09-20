package com.cuthead.app;



import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.cuthead.controller.VollyErrorHelper;
import com.cuthead.models.HairStyle;

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
    Button btn;
    Spinner spinner;
    TextView phoneTitle;
    TextView nameTitle;
    RequestQueue mRequestQueue;

    private ViewGroup indicatorLayout;
    private TextView dot1;
    private TextView dot2;
    private ImageView bar1;
    private ImageView bar2;
    private final int NOT_VAILD_PHONE = 2;
    private final int VAILD_INFO = 0;
    final String ip = "http://123.57.13.137";
    String phone_url = "/customer/isregister/";

    boolean firstInto = true;   // Use for help onFoucusChangeListener
    String cusname;
    String cusphone;
    String sex;
    String barphone;
    String hairstyle;
    String distance;
    String time;
    String remark;
    String orderID;
    String address;
    String barberName;
    int flag;

    public SubmitFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_submit, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.cuthead.app.sp", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // Set the NetworkSetting dialog
        if (!NetworkUtil.isNetworkConnected(getActivity())){
            NetworkUtil.setNetworkDialog(getActivity());
        }

        final Bundle bundleget = getArguments();
        flag = bundleget.getInt("flag");      // if QuickActivity open flag will be zero,NormalBook will be one


        // Get info for normal activity
        if (flag == 1) {
            hairstyle = bundleget.getString("hairstyle");
            time = bundleget.getString("time");
            remark = bundleget.getString("remark");
            orderID = bundleget.getString("orderID");
            distance = bundleget.getString("distance");
            barphone = bundleget.getString("barphone");
            address = bundleget.getString("address");
            barberName = bundleget.getString("barberName");
        }


        // Set the top indicator bar
        if (flag == 0) {
            ViewGroup viewGroup = (RelativeLayout) view.findViewById(R.id.indicator4);
            viewGroup.setVisibility(View.GONE);
        } else {
            indicatorLayout = (RelativeLayout)view.findViewById(R.id.indicator4);
            bar1 = (ImageView)indicatorLayout.findViewById(R.id.phase1_bar);
            bar1.setImageResource(R.drawable.progress_indicate_bar);
            dot1 = (TextView)indicatorLayout.findViewById(R.id.phase1_dot);
            dot1.setBackgroundResource(R.drawable.progress_bar_mark);
            dot2 = (TextView)indicatorLayout.findViewById(R.id.phase2_dot);
            dot2.setBackgroundResource(R.drawable.progress_bar_mark);
        }


        btn = (Button)view.findViewById(R.id.btn_submit);
        etName = (EditText)view.findViewById(R.id.et_user_name_submit);
        etPhone = (EditText)view.findViewById(R.id.et_user_phone_submit);
        phoneTitle = (TextView)view.findViewById(R.id.tv2);
        nameTitle = (TextView)view.findViewById(R.id.tv1);
        spinner = (Spinner)view.findViewById(R.id.spiner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.sex_array,android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


        // restore user info
        String userinfo = sharedPreferences.getString("USER_INFO","empty");
        if (!userinfo.equals("empty")){
            String[] val = userinfo.split(";");
            cusname = val[0];
            cusphone = val[1];
            sex = val[2];
            etName.setText(cusname);
            etPhone.setText(cusphone);
            if (sex.equals("Male"))
                spinner.setSelection(0);
            else
                spinner.setSelection(1);

            Interpolator interpolator = new AccelerateInterpolator();
            etName.animate().alpha(1).setInterpolator(interpolator);
            etPhone.animate().alpha(1).setInterpolator(interpolator);
            spinner.animate().alpha(1).setInterpolator(interpolator);
            nameTitle.animate().alpha(1).setInterpolator(interpolator);
        }


        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    cusphone = etPhone.getText().toString();


                    if (isPhoneValid(cusphone)) {
                        getActivity().setProgressBarIndeterminateVisibility(true);
                        checkRegister();
                        getActivity().setProgressBarIndeterminateVisibility(false);
                        if (flag==0)
                            setJpushAlias();
                        else{
                            bar2 = (ImageView)indicatorLayout.findViewById(R.id.phase2_bar);
                            bar2.setImageResource(R.drawable.progress_indicate_bar);
                        }

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

                cusname = etName.getText().toString();
                if (cusname != null && !cusname.isEmpty()) {

                    if(spinner.getSelectedItem().toString().equals("先生"))
                        sex = "Male";
                    else
                        sex = "Female";


                    if (flag == 0) {
                        // Save the user info in sharepreference
                        saveInfo(editor);
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.qb_container, new QBProgressWheelFragment()).commit();
                    }
                    else
                    {
                        RelativeLayout commitDialog = (RelativeLayout)getActivity().getLayoutInflater().inflate(R.layout.dialog_commit,null);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle("提交订单");   //  make a dialog for commit function
                        TextView et_hair = (TextView) commitDialog.findViewById(R.id.tv_dialog_hair);
                        TextView et_time = (TextView) commitDialog.findViewById(R.id.tv_dialog_time);
                        TextView et_add = (TextView) commitDialog.findViewById(R.id.tv_dialog_distance);
                        et_hair.setText(HairStyle.getHair(hairstyle));
                        et_time.setText(time);
                        et_add.setText(distance);
                        builder.setPositiveButton("提交",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Save the user info in sharepreference
                                saveInfo(editor);

                                FragmentManager fragmentManager = getFragmentManager();
                                Fragment orderFragment = new OrderSuccessFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("cusname",cusname);
                                bundle.putString("cusphone",cusphone);
                                bundle.putString("sex",sex);
                                bundle.putString("barphone",barphone);
                                bundle.putString("hairstyle",hairstyle);
                                bundle.putString("distance",distance);
                                bundle.putString("time",time);
                                bundle.putString("remark",remark);
                                bundle.putInt("flag_order",1);
                                bundle.putString("address",address);
                                bundle.putString("barberName",barberName);
                                orderFragment.setArguments(bundle);
                                fragmentManager.beginTransaction().replace(R.id.fragment_container,orderFragment).addToBackStack(null).commit();
                            }
                        });
                        builder.setView(commitDialog);
                        builder.show();

                    }
                }
                else
                    Toast.makeText(getActivity(),"还不知道你叫啥呢",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    private void setJpushAlias(){
        JPushInterface.setAlias(getActivity(), cusphone, new TagAliasCallback() {
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


    private boolean isPhoneValid(String phone){
        if (phone.length() != 11)
            return false;
        String[] prefix  = {"130", "131", "132",
                "133", "134", "135",
                "136", "137", "138",
                "139", "150", "151",
                "152", "153", "155",
                "156", "157", "158",
                "159", "170", "180",
                "181", "182", "183",
                "184", "185", "186",
                "187", "188", "189"};
        for (String pre : prefix){
            if (phone.startsWith(pre))
                return true;
        }
        return false;
    }

    /**Check whether the phone number has been register,and set the edit name to be visible and enable the button*/
    private boolean checkRegister(){
        cusphone = etPhone.getText().toString();

        mRequestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> paras = new HashMap<String, String>();
        paras.put("phone",cusphone);
        CustomRequest req = new CustomRequest(Request.Method.POST,ip+phone_url,paras,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject object) {

                try {
                    Log.d("FOCK",object.toString());
                        int code = object.getInt("code");
                    // success login
                        if (code == 100){

                            JSONObject data = new JSONObject(object.getString("data"));
                            AlphaAnimation();
                            etName.setText(data.getString("name"));
                            if (data.getString("sex").equals("Male"))
                                spinner.setSelection(0);
                            else
                                spinner.setSelection(1);
                        }
                        if (code == 403){
                            AlphaAnimation();
                        }else if (code == 404){
                            Toast.makeText(getActivity(),"电话号码错误,请出现输入!",Toast.LENGTH_LONG).show();
                            etPhone.setText("");
                        }



                    }catch(JSONException e){
                        e.printStackTrace();
                    }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VollyErrorHelper.getMessage(volleyError);
                Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_LONG).show();
                getActivity().setProgressBarIndeterminateVisibility(false);
            }
        });
        mRequestQueue.add(req);

        return false;
    }

    private void AlphaAnimation(){
        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        phoneTitle.animate().alpha(0.15f).setInterpolator(interpolator).setDuration(500);
        etPhone.animate().alpha(0.15f).setInterpolator(interpolator).setDuration(500);
        nameTitle.animate().alpha(1).setInterpolator(interpolator).setDuration(500);
        etName.setEnabled(true);
        etName.animate().alpha(1).setInterpolator(interpolator).setDuration(500);
        spinner.animate().alpha(1).setInterpolator(interpolator).setDuration(500);
        btn.setEnabled(true);
    }

    /** Save userinfo to SharedPreferences */
    private void saveInfo(SharedPreferences.Editor editor){

        editor.putString("USER_INFO",cusname+";"+cusphone+";"+sex);
        editor.commit();
    }


}
