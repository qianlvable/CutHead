package com.cuthead.app;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cuthead.controller.CircularImageView;
import com.cuthead.controller.HistoryCustomCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {


    String username;
    String userphone;
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


    private static int GALLERY_REQUEST = 0;
    private static int CAMERA_REQUEST = 1;

    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity();
        View view = inflater.inflate(R.layout.userinfo_fragment, container, false);
        ButterKnife.inject(this, view);

        ArrayList<Card> cards = new ArrayList<Card>();

        SharedPreferences countfile = getActivity().getSharedPreferences("countfile", 0);
        SharedPreferences.Editor counteditor = countfile.edit();
        int n = countfile.getInt("time", 0);


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
        Log.d("haha","1号创建");
*/


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


        for (int i = 1; i <= n; i++) {                                                   /**from 1 to begin count */
            HistoryCustomCard card = new HistoryCustomCard(context, R.layout.custom_history_card);
            TextView tv = (TextView) view.findViewById(R.id.userinfo_tv);
            tv.setVisibility(View.GONE);                                             //  make the textview gone
            card.setFile(i, getActivity());
            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
        listView.setAdapter(mCardArrayAdapter);
        Log.d("test", "adapter set");
        SharedPreferences userfile = getActivity().getSharedPreferences("com.cuthead.app.sp", 0);
        String userinfo = userfile.getString("USER_INFO", "empty");
        if (!userinfo.equals("empty")) {
            String[] val = userinfo.split(";");
            username = val[0];
            userphone = val[1];
        }
        final SharedPreferences.Editor editor = userfile.edit();
        /*
        final String username = userfile.getString("username","新用户");
        final String userphone = userfile.getString("userphone","00000000000");*/


        et_username.setEnabled(false);
        et_username.setText(username);

        et_userphone.setEnabled(false);
        et_userphone.setText(userphone);

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
            }
        });
        im_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String innerusername = et_username.getText().toString();
                String inneruserphone = et_userphone.getText().toString();
                if (innerusername.equals("")) {
                    innerusername = username;
                    Toast.makeText(getActivity(), "用户名为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inneruserphone.length() != 11) {
                    inneruserphone = userphone;
                    Toast.makeText(getActivity(), "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putString("username", innerusername);
                editor.putString("userphone", inneruserphone);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                mImageUserIcon.setImageBitmap(thumbnail);
                mBtnAddUsericon.setVisibility(View.INVISIBLE);

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
                String path = android.os.Environment
                        .getExternalStorageDirectory().getAbsolutePath();
                Log.d("test","save path "+path);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
