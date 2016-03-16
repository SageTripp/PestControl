package com.okq.pestcontrol.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.bean.param.PestScreeningParam;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/11.
 */
public class ScreeningDialog extends AlertDialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String DATE_PICKER_START_TIME = "startTime";
    private static final String DATE_PICKER_END_TIME = "endTime";
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private Button sureBtn;
    private TextInputLayout deviceTil;
    private CloudEditText deviceTv;
    private TextInputLayout dataTypeTil;
    private EditText dataTypeTv;
    private TextInputLayout startTimeTil;
    private EditText startTimeTv;
    private TextInputLayout endTimeTil;
    private EditText endTimeTv;
    private OnScreeningFinishListener mListener;
    private Context mContext;
    private DateTime startTimeDt;
    private DateTime endTimeDt;
    private FragmentManager mManager;
    private String device;
    private String dataType;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, DateTime.now().getYear(), DateTime.now().getMonthOfYear() - 1, DateTime.now().getDayOfMonth(), false);
    private final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), true, false);
    private String dateFlag = "";

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
        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) mManager.findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) mManager.findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }
    }

    @Override
    public void show() {
        super.show();
        PestScreeningParam psp = mListener.onOpen();
        if (null == psp)
            psp = new PestScreeningParam();
        dataType = null == psp.getDataType() ? "" : psp.getDataType();
        String type = "全部";
        switch (dataType) {
            case "1":
                type = "全部";
                break;
            case "2":
                type = "报警数据";
                break;
            case "3":
                type = "超限数据";
                break;
        }
        device = null == psp.getDevice() ? "" : psp.getDevice();
        startTimeDt = psp.getStartTime() == 0 ? DateTime.now() : new DateTime(psp.getStartTime());
        endTimeDt = psp.getEndTime() == 0 ? DateTime.now() : new DateTime(psp.getEndTime());
        deviceTv.addSpan(device);
        dataTypeTv.setText(type);
        startTimeTv.setText(startTimeDt.toString(DATE_FORMAT));
        endTimeTv.setText(endTimeDt.toString(DATE_FORMAT));
    }

    private void findView() {
        sureBtn = (Button) findViewById(R.id.sure_btn);
        deviceTil = (TextInputLayout) findViewById(R.id.device_til);
        deviceTv = (CloudEditText) findViewById(R.id.device_tv);
        dataTypeTil = (TextInputLayout) findViewById(R.id.data_type_til);
        dataTypeTv = (EditText) findViewById(R.id.data_type_tv);
        startTimeTil = (TextInputLayout) findViewById(R.id.start_time_til);
        startTimeTv = (EditText) findViewById(R.id.start_time_tv);
        endTimeTil = (TextInputLayout) findViewById(R.id.end_time_til);
        endTimeTv = (EditText) findViewById(R.id.end_time_tv);
        deviceTil.setHint("设备");
        dataTypeTil.setHint("数据类型");
        startTimeTil.setHint("开始时间");
        endTimeTil.setHint("结束时间");
        sureBtn.setOnClickListener(this);
        startTimeTv.setOnClickListener(this);
        endTimeTv.setOnClickListener(this);
        deviceTv.setOnClickListener(this);
        dataTypeTv.setOnClickListener(this);
        ArrayList<String> deviceItems = new ArrayList<>();
        DbManager db = x.getDb(App.getDaoConfig());
        try {
            List<Device> all = db.findAll(Device.class);
            if (null != all)
                for (Device device : all) {
                    deviceItems.add(device.getDeviceNum());
                }
        } catch (DbException e) {
            e.printStackTrace();
        }
        deviceTv.setItems(deviceItems);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_btn:
                onSureBtnClicked();
                break;
            case R.id.device_tv:
                break;
            case R.id.data_type_tv:
                AlertDialog.Builder b = new Builder(mContext);
                b.setTitle("请选择数据类型");
                final String[] array = mContext.getResources().getStringArray(R.array.pest_data_type);
                b.setItems(R.array.pest_data_type, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataTypeTv.setText(array[which]);
                        dataType = which + "";
                    }
                });
                b.create().show();
                break;
            case R.id.start_time_tv:
                if (!TextUtils.isEmpty(startTimeTv.getText())) {
                    DateTime dateTime = new DateTime(new Date(startTimeTv.getText().toString()).getTime());
                    datePickerDialog = DatePickerDialog.newInstance(this, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth(), false);
                }
                createDatePicker(DATE_PICKER_START_TIME);
                break;
            case R.id.end_time_tv:
                if (!TextUtils.isEmpty(endTimeTv.getText())) {
                    DateTime dateTime = new DateTime(new Date(endTimeTv.getText().toString()).getTime());
                    datePickerDialog = DatePickerDialog.newInstance(this, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth(), false);
                }
                createDatePicker(DATE_PICKER_END_TIME);
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
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        switch (datePickerDialog.getTag()) {
            case DATE_PICKER_START_TIME:
                startTimeDt = new DateTime(year, month + 1, day, 0, 0, 0);
                startTimeTv.setText(startTimeDt.toString(DATE_FORMAT));
                break;
            case DATE_PICKER_END_TIME:
                endTimeDt = new DateTime(year, month + 1, day, 0, 0, 0);
                endTimeTv.setText(endTimeDt.toString(DATE_FORMAT));
                break;
        }
        dateFlag = datePickerDialog.getTag();
        timePickerDialog.setStartTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour());
//        timePickerDialog.show(mManager, datePickerDialog.getTag());
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        switch (dateFlag) {
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
        datePickerDialog.setYearRange(now.getYear() - 10, now.getYear());
//        datePickerDialog.setCloseOnSingleTapDay(true);
        datePickerDialog.setCancelable(false);
        datePickerDialog.show(mManager, tag);
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
        psp.setDevice(deviceTv.getAllReturnString(true));
        psp.setDataType(dataType);
        psp.setStartTime(startTimeDt.getMillis());
        psp.setEndTime(endTimeDt.getMillis());
        if (startTimeDt.isBefore(endTimeDt))
            mListener.onFinished(psp);
        else {
            Toast.makeText(mContext, "开始时间要小于结束时间", Toast.LENGTH_LONG).show();
        }
    }

}
