package com.cuthead.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cuthead.app.R;
import com.cuthead.models.LauncherIcon;

/**
 * Created by Jiaqi Ning on 21/3/2015.
 */
public class CardDashboardAdapter extends BaseAdapter {
    private Context mContext;
    private LauncherIcon[] iconSet;
    public CardDashboardAdapter(Context context,LauncherIcon[] icons){
        mContext = context;
        iconSet = icons;
    }
    @Override
    public int getCount() {
        return iconSet.length;
    }

    @Override
    public LauncherIcon getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public ImageView icon;
        public TextView text;
        public LinearLayout layout;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dashboard_card, null);
            holder = new ViewHolder();
            holder.icon = (ImageView)view.findViewById(R.id.dashboard_card_img);
            holder.text = (TextView)view.findViewById(R.id.dashboard_card_text);
            holder.layout = (LinearLayout)view.findViewById(R.id.card_layout);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.icon.setImageResource(iconSet[i].getImgId());
        holder.text.setText(iconSet[i].getName());
        holder.layout.setBackgroundColor(iconSet[i].getBackgroundColor());
        holder.text.setBackgroundColor(iconSet[i].getBottomBarColor());
        return view;
    }
}
