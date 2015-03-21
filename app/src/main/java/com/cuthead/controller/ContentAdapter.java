package com.cuthead.controller;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cuthead.app.R;
import com.cuthead.models.Comment;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by zsx on 2015/2/23.
 */
public class ContentAdapter extends ArrayAdapter<Comment> {
    int resourceId;
    public ContentAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    public View getView(int position,View convertView,ViewGroup parent){
        Comment comment = getItem(position);  //获取当前Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView name = (TextView)view.findViewById(R.id.comment_name);
        TextView content = (TextView)view.findViewById(R.id.comment_content);
        TextView time = (TextView)view.findViewById(R.id.comment_time);
        TextView rank = (TextView)view.findViewById(R.id.comment_rank);
        ImageView profile = (ImageView)view.findViewById(R.id.comment_profile);
        name.setText(comment.getContent());
        content.setText(comment.getName());
        time.setText(comment.getTime());
        //rank.setText("等级："+comment.getRank());
        profile.setImageResource(comment.getProfile());
        return view;
    }
}
