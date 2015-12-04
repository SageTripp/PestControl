package com.okq.pestcontrol.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.okq.pestcontrol.R;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ScreeningPopupWindow extends PopupWindow implements View.OnClickListener {

    private View mMenuView;
    private Button btn;
    private OnScreeningFinishListener mListener;

    public ScreeningPopupWindow(Context context, OnScreeningFinishListener listener) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_screening, null);
        mListener = listener;
        btn = (Button) mMenuView.findViewById(R.id.btn);
        btn.setOnClickListener(this);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        this.setAnimationStyle(R.style.PopupWindowStyle);
        this.setFocusable(true);
    }

    public ScreeningPopupWindow(Context context) {
        this(context, null);
    }

    public void setOnScreeningFinishListener(OnScreeningFinishListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mListener.onFinished(new Bundle());
    }

    public interface OnScreeningFinishListener {
        void onFinished(Bundle data);
    }

}
