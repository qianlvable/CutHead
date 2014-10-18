/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aliyun.android.oss.model.query.OSSQuery;
import com.aliyun.android.util.Pagination;

/**
 * @author Ruici
 */
public abstract class PaginationAdapter<T> extends BaseAdapter {

    public interface NextPageListener {
        void noNext();

        void hasNext();
    }

    public class PaginationAsyncTask extends AsyncTask<Void, Void, Pagination<T>> {

        /* (non-Javadoc) * @see android.os.AsyncTask#doInBackground(Params[]) */
        @Override
        protected Pagination<T> doInBackground(Void... params) {
            if (pagination == null) {
                return query.paginate();
            } else {
                return pagination.next();
            }
        }

        /* (non-Javadoc) * @see android.os.AsyncTask#onPostExecute(java.lang.Object) */
        @Override
        protected void onPostExecute(Pagination<T> result) {
            items.addAll(result.getContents());
            notifyDataSetChanged();
            pagination = result;
            if (result.hasNext()) {
                hasNextPage();
            } else {
                noNextPage();
            }
        }
        
    }
    
    private OSSQuery<T> query;

    private Pagination<T> pagination;
    
    NextPageListener nextPageListener;

    boolean autoLoading = false;
    
    List<T> items;
    
    PaginationAsyncTask task;

    public PaginationAdapter(OSSQuery<T> query) {
        this.query = query;
        this.pagination = null;
        this.items = new ArrayList<T>();
        hasNextPage();
        onNextPageRequest();
    }

    public void setNextPageListener(NextPageListener nextPageListener) {
        this.nextPageListener = nextPageListener;
    }

    
    /* (non-Javadoc) * @see android.widget.Adapter#getCount() */
    @Override
    public int getCount() {
        return items.size();
    }

    /* (non-Javadoc) * @see android.widget.Adapter#getItem(int) */
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    /* (non-Javadoc) * @see android.widget.Adapter#getItemId(int) */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /* (non-Javadoc) * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup) */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View res = getPaginationView(position, convertView, parent);
        if (position == getCount() - 1 && autoLoading) {
            onNextPageRequest();
        }
        
        return res;
    }

    public void onNextPageRequest() {
        if (task != null) {
            task.cancel(false);
        }
        
        task = new PaginationAsyncTask();
        task.execute();
    }

    public abstract View getPaginationView(int position, View convertView, ViewGroup parent);
    
    public void noNextPage() {
        autoLoading = false;
        if (nextPageListener != null) {
            nextPageListener.noNext();
        }
    }
    
    public void hasNextPage() {
        autoLoading = true;
        if (nextPageListener != null) {
            nextPageListener.hasNext();
        }
    }
}
