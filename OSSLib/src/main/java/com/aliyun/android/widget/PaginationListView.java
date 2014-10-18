/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.widget;

import com.aliyun.android.widget.PaginationAdapter.NextPageListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author Ruici
 */
public class PaginationListView extends ListView implements NextPageListener {

    boolean footerViewAttached = false;

    private View loadingView;

    private PaginationAdapter<?> adapter;

    public PaginationListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PaginationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaginationListView(Context context) {
        super(context);
    }

    public void setLoadingView(View view) {
        this.loadingView = view;
    }

    public View getLoadingView() {
        return this.loadingView;
    }

    /*
     * (non-Javadoc) * @see
     * android.widget.ListView#setAdapter(android.widget.ListAdapter)
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (!(adapter instanceof PaginationAdapter<?>)) {
            throw new IllegalArgumentException("the type of adapter must be "
                    + PaginationAdapter.class.getSimpleName());
        }
        
        if (this.adapter != null) {
            this.adapter.setNextPageListener(null);
            this.setOnScrollListener(null);
        }
        
        this.adapter = (PaginationAdapter<?>) adapter;
        this.adapter.setNextPageListener(this);
        
        View view = new View(getContext());
        super.addFooterView(view);
        super.setAdapter(adapter);
        super.removeFooterView(view);
    }


    /* (non-Javadoc) * @see android.widget.ListView#getAdapter() */
    @Override
    public ListAdapter getAdapter() {
        return this.adapter;
    }

    /*
     * (non-Javadoc) * @see
     * com.aliyun.android.widget.PaginationAdapter.NextPageListener#noNext()
     */
    @Override
    public void noNext() {
        if (loadingView != null) {
            this.removeFooterView(loadingView);
        }
        footerViewAttached = false;
    }
    /*
     * (non-Javadoc) * @see
     * com.aliyun.android.widget.PaginationAdapter.NextPageListener#hasNext()
     */
    @Override
    public void hasNext() {
        if (!footerViewAttached && loadingView != null) {
            this.addFooterView(loadingView);
            footerViewAttached = true;
        }
    }
    
    public boolean isLoadingViewVisible() {
        return this.footerViewAttached;
    }

}
