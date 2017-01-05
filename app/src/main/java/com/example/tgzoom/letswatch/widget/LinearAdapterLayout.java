package com.example.tgzoom.letswatch.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by tgzoom on 11/18/16.
 */

public class LinearAdapterLayout extends LinearLayout {
    private BaseAdapter mAdapter;
    private View mEmptyView;

    public LinearAdapterLayout(Context context) {
        super(context);
    }

    public LinearAdapterLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
    }

    public LinearAdapterLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    private DataSetObserver mDataObserver = new DataSetObserver(){
        @Override
        public void onChanged() {
            setupChildren();
        }

        @Override
        public void onInvalidated() {
            setupChildren();
        }
    };


    private void setupChildren() {

        removeAllViews();

        updateEmptyStatus((mAdapter == null) || mAdapter.isEmpty());

        if (mAdapter == null) {
            return;
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View child = mAdapter.getView(i, null, this);
            addViewInLayout(child, -1, child.getLayoutParams(), true);
        }
    }

    private void updateEmptyStatus(boolean empty) {
        if (empty) {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            } else {
                // If the caller just removed our empty view, make sure the list
                // view is visible
                setVisibility(View.VISIBLE);
            }
        } else {
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }

        mAdapter = adapter;

        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataObserver);
        }

        setupChildren();
    }

}
