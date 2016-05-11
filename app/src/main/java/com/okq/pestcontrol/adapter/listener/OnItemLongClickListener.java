package com.okq.pestcontrol.adapter.listener;

/**
 * Created by zst on 2016/2/29. 长按监听器
 */
public interface OnItemLongClickListener {
    void onSelect(int position, long id, boolean checked);

    void onLongClick(int position, long id, boolean checked);
}
