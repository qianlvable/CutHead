package com.cuthead.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.cuthead.app.R;

/**
 * Created by Jiaqi Ning on 14/2/2015.
 */
public class ReviewDialog extends DialogFragment{

    private EditText mReviewEdittext;
    private RatingBar mRatingBar;
    private int rate;

    public interface ReviewDialogListener{
            public void onDialogPositiveClick();
            public void passReviewData(int rate,String review);

    }

    ReviewDialogListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            mListener = (ReviewDialogListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException("Calling fragment must implement ReviewDialogListner");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.review_dialog_layout,null);

        mReviewEdittext = (EditText)view.findViewById(R.id.et_comment);
        mRatingBar = (RatingBar)view.findViewById(R.id.ratingBar);

        builder.setView(view)

                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.passReviewData(mRatingBar.getNumStars(),mReviewEdittext.getText().toString());
                        mListener.onDialogPositiveClick();
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                           ReviewDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

