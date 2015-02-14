package com.cuthead.app;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.aliyun.android.oss.OSSClient;

import com.aliyun.android.oss.asynctask.UploadObjectAsyncTask;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cuthead.controller.CustomRequest;
import com.cuthead.controller.HistoryCustomCard;
import com.cuthead.controller.OthersUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {

    @InjectView(R.id.im_set)
    ImageButton im_set;
    @InjectView(R.id.im_done)
    ImageButton im_done;
    @InjectView(R.id.et_user_name)
    EditText et_username;
    @InjectView(R.id.et_user_phone)
    EditText et_userphone;
    @InjectView(R.id.btn_add_usericon)
    ImageButton mBtnAddUsericon;
    @InjectView(R.id.history_list)
    CardListView listView;
    @InjectView(R.id.iv_user_icon)
    ImageView mImageUserIcon;
    @InjectView(R.id.userinfo_area)
    RelativeLayout userInfoLayout;

    private String username;
    private String userphone;


    final String ip = "http://123.57.13.137/";
    final String updatePhoneUrl = "update/customer/phone/";
    final String updateNameUrl = "update/customer/name/";
    final String uploadUserImageUrl = "update/customer/profile/";

    private static int GALLERY_REQUEST = 0;
    private static int CAMERA_REQUEST = 1;
    private RequestQueue mRequestQueue;
    private boolean isPhoneModifed = false;
    private boolean isNameModifed = false;
    private Map<String,String> paras = new HashMap<String, String>();

    private String accessKeyID;
    private String accessKeySecret;
    private String bucketName;
    private String fileKey;


    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity();
        View view = inflater.inflate(R.layout.userinfo_fragment, container, false);
        ButterKnife.inject(this, view);

        setUpCardList(context, view);
        getLocalUserInfo();

        if (!username.equals("empty")){
            userInfoLayout.setVisibility(View.VISIBLE);
        }



        // Set up the add image button dialog
        mBtnAddUsericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence options[] = {"从相册里挑", "马上拍照"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("上传头像");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        if (options[item].equals("从相册里挑")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_REQUEST);
                        } else if (options[item].equals("马上拍照")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            File f = new File(Environment.getExternalStorageDirectory(), "user_icon.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            Log.d("test", "image path" + f.getAbsolutePath());
                            startActivityForResult(intent, CAMERA_REQUEST);
                        }
                    }
                });
                builder.show();
            }
        });

        et_username.setEnabled(false);
        et_username.setText(username);
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    isNameModifed = true;

            }
        });

        et_userphone.setEnabled(false);
        et_userphone.setText(userphone);
        et_userphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    isPhoneModifed = true;
            }
        });



        im_done.setVisibility(View.GONE);
        im_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setEnabled(true);
                et_userphone.setEnabled(true);
                im_set.setVisibility(View.GONE);
                im_done.setVisibility(View.VISIBLE);
            }
        });
        im_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog updateInfoDialog = new ProgressDialog(getActivity());
                updateInfoDialog.setCancelable(true);
                updateInfoDialog.setIndeterminate(true);
                updateInfoDialog.setTitle("正在更新");

                if (isPhoneModifed){
                    final String newPhone = et_userphone.getText().toString();
                    if (OthersUtil.isPhoneValid(newPhone)){
                        updateInfoDialog.show();

                        paras.clear();
                        paras.put("phone",userphone);
                        paras.put("phone_u",newPhone);
                        final CustomRequest request = new CustomRequest(Request.Method.POST,ip+ updatePhoneUrl,paras,new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                isPhoneModifed = false;
                                if (isNameModifed){
                                    addModifedNameRequest(updateInfoDialog);
                                }
                                updateInfoDialog.dismiss();
                                Log.d("testMod","success:"+jsonObject.toString());
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.cuthead.app.sp", 0).edit();
                                editor.putString("userphone",newPhone);
                                editor.apply();
                            }
                        },new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                   updateInfoDialog.dismiss();
                                   et_userphone.setText(userphone);
                                   Toast.makeText(getActivity(),"修改失败,请稍后重试",Toast.LENGTH_SHORT).show();
                                   Log.d("testMod",volleyError.toString());
                            }
                        });
                        MyApplication.getInstance().getRequestQueue().add(request);
                    }

                }else if (isNameModifed){
                    updateInfoDialog.show();
                    addModifedNameRequest(updateInfoDialog);
                }else {
                    return;
                }

                im_done.setVisibility(View.GONE);
                im_set.setVisibility(View.VISIBLE);
                et_username.setEnabled(false);
                et_userphone.setEnabled(false);
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        return view;
    }

    private void getLocalUserInfo() {
        SharedPreferences userfile = getActivity().getSharedPreferences("com.cuthead.app.sp", 0);
        username = userfile.getString("username","empty");
        userphone = userfile.getString("userphone","empty");
    }

    private void setUpCardList(Context context, View view) {
        ArrayList<Card> cards = new ArrayList<Card>();
        SharedPreferences countfile = getActivity().getSharedPreferences("countfile", 0);
        int orderCount = countfile.getInt("time", 0);
        for (int i = 1; i <= orderCount; i++) {
            HistoryCustomCard card = new HistoryCustomCard(context, R.layout.custom_history_card);
            TextView tv = (TextView) view.findViewById(R.id.userinfo_tv);
            tv.setVisibility(View.GONE);
            card.setFile(i, getActivity());
            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
        listView.setAdapter(mCardArrayAdapter);
        Log.d("test", "adapter set");
    }

    private void addModifedNameRequest(final Dialog dialog){
        final String newName = et_username.getText().toString();
        String phone = et_userphone.getText().toString();
        if (newName.equals("")) {
            Toast.makeText(getActivity(), "用户名为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        isNameModifed = false;
        paras.put("phone",phone);
        paras.put("name",newName);
        CustomRequest nameReq = new CustomRequest(Request.Method.POST,ip+updateNameUrl,paras,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"修改完毕",Toast.LENGTH_SHORT).show();
                Log.d("testMod","success:"+jsonObject.toString());
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.cuthead.app.sp", 0).edit();
                editor.putString("username",newName);
                editor.apply();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                et_username.setText(username);
                Toast.makeText(getActivity(),"修改失败,请稍后重试",Toast.LENGTH_SHORT).show();
                Log.d("testMod",volleyError.toString());
            }
        });
        MyApplication.getInstance().getRequestQueue().add(nameReq);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {

                Bitmap thumbnail = getGalleryImageFromIntent(data);
                mImageUserIcon.setImageBitmap(thumbnail);
                mBtnAddUsericon.setVisibility(View.INVISIBLE);

                final byte[] bytes = convertBitmapToBytes(thumbnail);
                uploadImageToOSS(bytes);


            }else if (requestCode == CAMERA_REQUEST){
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("user_icon.jpg")) {
                        f = temp;
                        break;
                    }
                }

                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                mImageUserIcon.setImageBitmap(bitmap);
                mBtnAddUsericon.setVisibility(View.INVISIBLE);
                byte[] imageData = convertBitmapToBytes(bitmap);
                uploadImageToOSS(imageData);

                String path = android.os.Environment
                        .getExternalStorageDirectory().getAbsolutePath();
                f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,85,outFile);
                    outFile.flush();
                    outFile.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } else {
            Toast.makeText(getActivity(), "抱歉,获取照片失败", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImageToOSS(final byte[] imagebytes) {
        paras.clear();
        paras.put("phone",userphone);
        CustomRequest request = new CustomRequest(Request.Method.POST,ip+uploadUserImageUrl,paras,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    accessKeyID = data.getString("access_key_id");
                    fileKey = data.getString("key");
                    accessKeySecret = data.getString("access_key_secret");
                    bucketName = data.getString("bucket_name");

                   new UploadObjectAsyncTask<Void>(accessKeyID,accessKeySecret,bucketName,fileKey){
                       @Override
                       protected void onPostExecute(String s) {
                           Toast.makeText(getActivity(), "头像上传完毕", Toast.LENGTH_LONG).show();
                           super.onPostExecute(s);
                       }
                   }.execute(imagebytes);

                    Log.d("testOSS", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"上传照片失败,请稍后重试",Toast.LENGTH_LONG).show();
                Log.d("testOSS",volleyError.toString());
            }
        });
        MyApplication.getInstance().getRequestQueue().add(request);
    }

    private byte[] convertBitmapToBytes(Bitmap thumbnail) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG,60,stream);
        return stream.toByteArray();
    }

    private Bitmap getGalleryImageFromIntent(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String picturePath = c.getString(columnIndex);
        c.close();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        return (BitmapFactory.decodeFile(picturePath));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
