package com.okq.pestcontrol.adapter.listener;

/**
 * Created by zst on 2016/5/27 0027.
 * 描述:
 */

public interface ItemTouchAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
