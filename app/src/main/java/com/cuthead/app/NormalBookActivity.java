package com.cuthead.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class NormalBookActivity extends Activity implements NBChoiceFragment.GetDate,
        NBChoiceFragment.GetStyle,NBChoiceFragment.GetLocation,NBTimeFragment.GetFinalTime{
    ProgressBar pb;
    Button btn;
    int pageCount = 1;
    public int year;
    private TextView tv_prog;
    private NBChoiceFragment nbChoiceFragment;
    private NBBaberListFragment nbBarberFragment;
    private NBCommitFragment commitFragment;
    private NBTimeFragment nbTimeFragment;
    private NBProgressBarFragment nbProgressBarFragment;
    private RequestQueue mRequestQuene;
    private int Year,Month,Day;
    private int Style;
    Fragment tempFragment;
    String url;
    double longitude;
    double lantitude;
    String orderID;
    String finalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_book);
        mRequestQuene = Volley.newRequestQueue(this);

        final FragmentManager fragmentManager = getFragmentManager();
        pb = (ProgressBar)findViewById(R.id.progressBar);
        btn = (Button)findViewById(R.id.btn_nxt);
        tv_prog = (TextView) findViewById(R.id.tv_prog);

        nbChoiceFragment = new NBChoiceFragment();
        nbBarberFragment = new NBBaberListFragment();
        commitFragment = new NBCommitFragment();
        nbTimeFragment = new NBTimeFragment();
        nbProgressBarFragment = new NBProgressBarFragment();

        LinearLayout commitDialog = (LinearLayout)getLayoutInflater().inflate(R.layout.dialog_commit,null);
        TextView dialog_tvTime = (TextView) commitDialog.findViewById(R.id.tv_dialogTime);
        dialog_tvTime.setText("请您在"+finalTime+"分到XX地尽享服务，感谢您的使用");  //final处应该填写最终时间
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("提交订单");   //  make a dialog for commit function
        builder.setPositiveButton("提交",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new OrderSuccessFragment()).addToBackStack(null).commit();
                tv_prog.setText("感谢使用");
                btn.setText("完成");                                                                //表示订单结束     设置text为 完成  是监听能够识别 从而回到Activity
                //pageCount = 4;
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pageCount--;
                Log.e("countcancle",""+pageCount);
                FragmentTransaction ft  = getFragmentManager().beginTransaction();
            }
        });
        builder.setView(commitDialog);

        FragmentTransaction inittransaction = fragmentManager.beginTransaction();                //add the first fragment
        inittransaction.add(R.id.fragment_container,nbChoiceFragment,"1").commit();
        tv_prog.setText("选择发型与日期");
        Log.d("初始化",Integer.toBinaryString(pageCount));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn.getText() == "完成")                                                   //当Button显示 完成时  启动到MainActivity
                {
                    FragmentManager fragmentManager1 = getFragmentManager();
                    fragmentManager.popBackStack();                                    //这里要清除原来的fragment   还不知道怎么解决
                    Intent intent = new Intent(NormalBookActivity.this,MainActivity.class);
                    startActivity(intent);
                    return;
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                pb.incrementProgressBy(33);
                switch (pageCount){
                    case 1:                       //*****  case1   *****
                        if(isDateWrong(Year,Month,Day) || isHairWrong(Style) || isLocationWrong(lantitude,longitude))
                        {
                            Toast toast = Toast.makeText(NormalBookActivity.this,"信息不完整或未打开网络",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            break;
                        }

                        pageCount++;                                                        //更新pageCount
                        Log.e("count",""+pageCount);
                        Bundle bundle = new Bundle();
                        //NBProgressBarFragment nbProgressBarFragment = new NBProgressBarFragment();
                        bundle.putString("date",Year+"."+Month+"."+Day);
                        bundle.putString("hairstyle",Style+"");
                        bundle.putString("phone","186");
                        bundle.putString("name","lilei");
                        bundle.putString("sexy","Male");
                        bundle.putString("longitude",longitude+"");
                        bundle.putString("latitude",lantitude+"");
                        nbProgressBarFragment.setArguments(bundle);
                        transaction.replace( R.id.fragment_container,nbProgressBarFragment,"2" ).addToBackStack("fragment1").commit();//点击后会传送   发型和日期  并接受 返回的 理发师列表 到 barberFragment里面
                        tv_prog.setText("选择理发师");
                        break;
                    case 2:                         //*****  case2   *****
                        pageCount++;                                                         //更新pageCount
                        transaction.replace(R.id.fragment_container,nbTimeFragment,"3").addToBackStack("fragment2").commit();
                        tv_prog.setText("选择预约时间");
                        Log.e("count", "" + pageCount);
                        //接受数据
                        //点击后会
                        break;
                    case 3:                         //*****  case3   *****
                        pageCount++;
                        builder.create().show();
                        Log.e("count", "" + pageCount);
                    /*case 4:
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.activity_normal_book, null);
                        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.dialog_commit,null);
                        parent.removeView(child);*/
                }

            }
        });


    }
    public void getDate(int year,int month,int day){Year = year;Month = month;Day = day;};
    public void getStyle(int style){Style = style;};
    public void getLocation(double mlongitude,double mlantitude){longitude = mlongitude;lantitude = mlantitude;};
    public void getFinalTime(String time){finalTime = time;};
    public void onBackPressed() {

        super.onBackPressed();
        pageCount--;
        Log.e("count",""+pageCount);

    }
    public boolean isHairWrong(int style){if(style == 0) return true;else return false;}
    public boolean isDateWrong(int year,int month,int day){if(year*month*day == 0)return true;else return false;}
    public boolean isLocationWrong(double lantitude,double longitude){if(lantitude*longitude == 0.0) return true;else return false;}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

