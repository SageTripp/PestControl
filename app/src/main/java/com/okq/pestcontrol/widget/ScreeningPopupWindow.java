package com.okq.pestcontrol.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.okq.pestcontrol.R;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ScreeningPopupWindow extends PopupWindow implements View.OnClickListener {

    private View mMenuView;
    private Button sureBtn;
    private TextInputLayout areaTil;
    private MultiAutoCompleteTextView areaTv;
    private TextInputLayout pestTil;
    private MultiAutoCompleteTextView pestTv;
    private TextInputLayout startTiemTil;
    private TextView startTimeTv;
    private TextInputLayout endTimeTil;
    private TextView endTimeTv;
    private OnScreeningFinishListener mListener;

    private Context mContext;

    private String area;
    private String pest;
    private String startTime;
    private String endTime;


    public ScreeningPopupWindow(Context context, OnScreeningFinishListener listener) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_screening, null);
        mListener = listener;
        mContext = context;
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
        findView();

    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        Bundle data = mListener.onOpen();
        area = data.getString("area");
        pest = data.getString("pest");
        startTime = data.getString("startTime");
        endTime = data.getString("endTime");
        areaTv.setText(area);
        pestTv.setText(pest);
        startTimeTv.setText(startTime);
        endTimeTv.setText(endTime);
    }

    private void findView() {
        sureBtn = (Button) mMenuView.findViewById(R.id.sure_btn);
        areaTil = (TextInputLayout) mMenuView.findViewById(R.id.area_til);
        areaTv = (MultiAutoCompleteTextView) mMenuView.findViewById(R.id.area_tv);
        pestTil = (TextInputLayout) mMenuView.findViewById(R.id.pest_til);
        pestTv = (MultiAutoCompleteTextView) mMenuView.findViewById(R.id.pest_tv);
        startTiemTil = (TextInputLayout) mMenuView.findViewById(R.id.start_time_til);
        startTimeTv = (TextView) mMenuView.findViewById(R.id.start_time_tv);
        endTimeTil = (TextInputLayout) mMenuView.findViewById(R.id.end_time_til);
        endTimeTv = (TextView) mMenuView.findViewById(R.id.end_time_tv);
        areaTil.setHint("区域");
        pestTil.setHint("害虫种类");
        startTiemTil.setHint("开始时间");
        endTimeTil.setHint("结束时间");
        sureBtn.setOnClickListener(this);
    }

    public ScreeningPopupWindow(Context context) {
        this(context, null);
    }

    public void setOnScreeningFinishListener(OnScreeningFinishListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        Bundle returnData = new Bundle();
        if (!returnData.isEmpty())
            returnData.clear();
        returnData.putString("area", areaTv.getText().toString());
        returnData.putString("pest", pestTv.getText().toString());
        returnData.putString("startTime", startTimeTv.getText().toString());
        returnData.putString("endTime", endTimeTv.getText().toString());
        mListener.onFinished(returnData);
    }

    public interface OnScreeningFinishListener {
        void onFinished(Bundle data);

        Bundle onOpen();
    }

}
