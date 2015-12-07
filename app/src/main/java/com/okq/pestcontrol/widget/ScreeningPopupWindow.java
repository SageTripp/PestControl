package com.okq.pestcontrol.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 筛选window
 * Created by Administrator on 2015/12/4.
 */
public class ScreeningPopupWindow extends PopupWindow implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    private static final String DATE_PICKER_START_TIME = "startTime";
    private static final String DATE_PICKER_END_TIME = "endTime";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    private View mMenuView;
    private Button sureBtn;
    private TextInputLayout areaTil;
    private EditText areaTv;
    private TextInputLayout pestTil;
    private CloudEditText pestTv;
    private TextInputLayout startTimeTil;
    private EditText startTimeTv;
    private TextInputLayout endTimeTil;
    private EditText endTimeTv;
    private OnScreeningFinishListener mListener;

    private Context mContext;

    private String area;
    private String pest;
    private String startTime;
    private String endTime;
    private FragmentManager mManager;

    private DateTime startTimeDt;
    private DateTime endTimeDt;


    public ScreeningPopupWindow(Context context, FragmentManager manager) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_screening, null);
        mManager = manager;
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
        PestInformation pi = (PestInformation) data.getSerializable("data");
        area = pi.getArea();
        pest = pi.getPestKind().getKindName();
        startTime = new DateTime(pi.getStartTime()).toString(DATE_FORMAT);
        endTime = new DateTime(pi.getEndTime()).toString(DATE_FORMAT);
        areaTv.setText(area);
        pestTv.setText(pest);
        startTimeTv.setText(startTime);
        endTimeTv.setText(endTime);
    }

    private void findView() {
        sureBtn = (Button) mMenuView.findViewById(R.id.sure_btn);
        areaTil = (TextInputLayout) mMenuView.findViewById(R.id.area_til);
        areaTv = (EditText) mMenuView.findViewById(R.id.area_tv);
        pestTil = (TextInputLayout) mMenuView.findViewById(R.id.pest_til);
        pestTv = (CloudEditText) mMenuView.findViewById(R.id.pest_tv);
        startTimeTil = (TextInputLayout) mMenuView.findViewById(R.id.start_time_til);
        startTimeTv = (EditText) mMenuView.findViewById(R.id.start_time_tv);
        endTimeTil = (TextInputLayout) mMenuView.findViewById(R.id.end_time_til);
        endTimeTv = (EditText) mMenuView.findViewById(R.id.end_time_tv);
        areaTil.setHint("区域");
        pestTil.setHint("害虫种类");
        startTimeTil.setHint("开始时间");
        endTimeTil.setHint("结束时间");
        sureBtn.setOnClickListener(this);
        startTimeTv.setOnClickListener(this);
        endTimeTv.setOnClickListener(this);
        areaTv.setOnClickListener(this);
        pestTv.setOnClickListener(this);
    }

    /**
     * 筛选窗体
     *
     * @param context 上下文对象
     */
    public ScreeningPopupWindow(Context context) {
        this(context, null);
    }

    /**
     * 设置筛选完成监听器
     *
     * @param listener 筛选完成监听器
     */
    public void setOnScreeningFinishListener(OnScreeningFinishListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_btn:
                onSureBtnClicked();
                break;
            case R.id.start_time_tv:
                createDatePicker(DATE_PICKER_START_TIME);
                break;
            case R.id.end_time_tv:
                createDatePicker(DATE_PICKER_END_TIME);
                break;
            case R.id.area_tv:
                PopupWindow popupWindow = new PopupWindow(mContext);

                break;
            case R.id.pest_tv:
                break;
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        switch (dialog.getTag()) {
            case DATE_PICKER_START_TIME:
                startTimeDt = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0, 0);
                createTimePicker(DATE_PICKER_START_TIME);
                break;
            case DATE_PICKER_END_TIME:
                endTimeDt = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0, 0);
                createTimePicker(DATE_PICKER_END_TIME);
                break;
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        switch (dialog.getTag()) {
            case DATE_PICKER_START_TIME:
                startTimeTv.setText(startTimeDt.plusHours(hourOfDay).plusMinutes(minute).toString(DATE_FORMAT));
                break;
            case DATE_PICKER_END_TIME:
                endTimeTv.setText(endTimeDt.plusHours(hourOfDay).plusMinutes(minute).toString(DATE_FORMAT));
                break;
        }
    }

    /**
     * 创建日期选择器
     *
     * @param tag 标示
     */
    private void createDatePicker(String tag) {
        DateTime now = DateTime.now();
        CalendarDatePickerDialogFragment dialog = CalendarDatePickerDialogFragment
                .newInstance(this, now.getYear(), now.getMonthOfYear() - 1,
                        now.getDayOfMonth());
        dialog.show(mManager, tag);
    }

    /**
     * 创建时间选择器
     *
     * @param tag 标示
     */
    private void createTimePicker(String tag) {
        DateTime now = DateTime.now();
        RadialTimePickerDialogFragment time = RadialTimePickerDialogFragment
                .newInstance(this, now.getHourOfDay(),
                        now.getMinuteOfHour(), true);
        time.show(mManager, tag);
    }

    /**
     * 筛选完成监听器
     */
    public interface OnScreeningFinishListener {
        /**
         * 筛选完成
         *
         * @param data 筛选的数据
         */
        void onFinished(Bundle data);

        /**
         * 打开筛选窗口
         *
         * @return 已选定的筛选数据
         */
        Bundle onOpen();
    }

    /**
     * 确认按钮点击事件
     */
    private void onSureBtnClicked() {
        Bundle returnData = new Bundle();
        if (!returnData.isEmpty())
            returnData.clear();
        PestInformation pi = new PestInformation();
        PestKind pk = new PestKind();
        pk.setKindFlag(1);
        pk.setKindName("种类1");
        pi.setArea("area");
        pi.setPestKind(pk);
        pi.setStartTime(System.currentTimeMillis());
        pi.setEndTime(System.currentTimeMillis());
        returnData.putSerializable("data", pi);
        mListener.onFinished(returnData);
    }

}
