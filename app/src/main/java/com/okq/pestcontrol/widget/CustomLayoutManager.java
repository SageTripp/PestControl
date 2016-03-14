package com.okq.pestcontrol.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by zst on 2016/3/14 0014.
 */
public class CustomLayoutManager extends RecyclerView.LayoutManager {

    private int mWidth;

    public CustomLayoutManager() {
        mWidth = getWidth();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
