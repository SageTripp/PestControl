package com.okq.pestcontrol.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;
import com.okq.pestcontrol.bean.param.PestScreeningParam;

import org.joda.time.DateTime;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/11.
 */
public class ScreeningDialog extends AlertDialog implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    private static final String DATE_PICKER_START_TIME = "startTime";
    private static final String DATE_PICKER_END_TIME = "endTime";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    private Button sureBtn;
    private TextInputLayout areaTil;
    private CloudEditText areaTv;
    private TextInputLayout pestTil;
    private CloudEditText pestTv;
    private TextInputLayout startTimeTil;
    private EditText startTimeTv;
    private TextInputLayout endTimeTil;
    private EditText endTimeTv;
    private OnScreeningFinishListener mListener;
    private Context mContext;
    private DateTime startTimeDt;
    private DateTime endTimeDt;
    private FragmentManager mManager;
    private String area;
    private String pest;
    private String startTime;
    private String endTime;
    private CalendarDatePickerDialogFragment dialog;

    protected ScreeningDialog(Context context) {
        this(context, R.style.dialog);
        this.mContext = context;
    }

    protected ScreeningDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public ScreeningDialog(Context context, FragmentManager manager) {
        this(context);
        this.mManager = manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_screening);
        WindowManager.LayoutParams a = getWindow().getAttributes();
        a.width = WindowManager.LayoutParams.MATCH_PARENT;
        a.height = WindowManager.LayoutParams.WRAP_CONTENT;
        a.gravity = Gravity.TOP | Gravity.LEFT;
        a.y = App.getToolbarHeignt();
        getWindow().setAttributes(a);
        getWindow().setWindowAnimations(R.style.DialogStyle);
        setCanceledOnTouchOutside(false);
        findView();
    }

    @Override
    public void show() {
        super.show();
        PestScreeningParam psp = mListener.onOpen();
        if (null == psp)
            psp = new PestScreeningParam();
        area = null == psp.getArea() ? "" : psp.getArea();
        pest = null == psp.getKind() ? "" : psp.getKind().getKindName();
        startTimeDt = psp.getStartTime() == 0 ? DateTime.now() : new DateTime(psp.getStartTime());
        endTimeDt = psp.getEndTime() == 0 ? DateTime.now() : new DateTime(psp.getEndTime());
        areaTv.addSpan(area, area);
        pestTv.addSpan(pest, pest);
        startTimeTv.setText(startTimeDt.toString(DATE_FORMAT));
        endTimeTv.setText(endTimeDt.toString(DATE_FORMAT));
    }

    private void findView() {
        sureBtn = (Button) findViewById(R.id.sure_btn);
        areaTil = (TextInputLayout) findViewById(R.id.area_til);
        areaTv = (CloudEditText) findViewById(R.id.area_tv);
        pestTil = (TextInputLayout) findViewById(R.id.pest_til);
        pestTv = (CloudEditText) findViewById(R.id.pest_tv);
        startTimeTil = (TextInputLayout) findViewById(R.id.start_time_til);
        startTimeTv = (EditText) findViewById(R.id.start_time_tv);
        endTimeTil = (TextInputLayout) findViewById(R.id.end_time_til);
        endTimeTv = (EditText) findViewById(R.id.end_time_tv);
        areaTil.setHint("区域");
        pestTil.setHint("害虫种类");
        startTimeTil.setHint("开始时间");
        endTimeTil.setHint("结束时间");
        sureBtn.setOnClickListener(this);
        startTimeTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    createDatePicker(DATE_PICKER_START_TIME);
            }
        });
        endTimeTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    createDatePicker(DATE_PICKER_END_TIME);
            }
        });
        areaTv.setOnClickListener(this);
        pestTv.setOnClickListener(this);
        ArrayList<String> areaItems = new ArrayList<>();
        String[] areaArray = getContext().getResources().getStringArray(R.array.pest_area);
        for (String area : areaArray) {
            areaItems.add(area);
        }
        ArrayList<String> pestItems = new ArrayList<>();
        DbManager db = x.getDb(App.getDaoConfig());
        try {
            List<PestKind> all = db.findAll(PestKind.class);
            for (PestKind kind : all) {
                pestItems.add(kind.getKindName());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        pestTv.setItems(pestItems);
        areaTv.setItems(areaItems);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_btn:
                onSureBtnClicked();
                break;
            case R.id.area_tv:
                break;
            case R.id.pest_tv:
                break;
        }
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
                startTimeDt = startTimeDt.plusHours(hourOfDay).plusMinutes(minute);
                startTimeTv.setText(startTimeDt.toString(DATE_FORMAT));
                break;
            case DATE_PICKER_END_TIME:
                endTimeDt = endTimeDt.plusHours(hourOfDay).plusMinutes(minute);
                endTimeTv.setText(endTimeDt.toString(DATE_FORMAT));
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
        dialog = CalendarDatePickerDialogFragment
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
        void onFinished(PestScreeningParam data);

        /**
         * 打开筛选窗口
         *
         * @return 已选定的筛选数据
         */
        PestScreeningParam onOpen();
    }

    /**
     * 确认按钮点击事件
     */
    private void onSureBtnClicked() {
        PestScreeningParam psp = new PestScreeningParam();
        PestKind pk = new PestKind();
        try {
            pk = x.getDb(App.getDaoConfig()).selector(PestKind.class).where("kindName", "=", pestTv.getText().toString()).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        psp.setArea(areaTv.getText().toString());
        psp.setKind(pk);
        psp.setStartTime(startTimeDt.getMillis());
        psp.setEndTime(endTimeDt.getMillis());
        mListener.onFinished(psp);
    }

}
