package com.okq.pestcontrol.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.param.PestScreeningParam;
import com.okq.pestcontrol.dbDao.PestInformationDao;
import com.okq.pestcontrol.task.DataTask;
import com.okq.pestcontrol.task.TaskInfo;
import com.okq.pestcontrol.util.SortUtil;
import com.okq.pestcontrol.widget.ScreeningDialog;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 图表页面 Created by Administrator on 2015/12/12.
 */
@ContentView(value = R.layout.fragment_chart)
public class ChartFragment extends BaseFragment implements OnChartValueSelectedListener {

    private static final String DATE_FORMAT = "YYYY-MM-dd";
    @ViewInject(value = R.id.chart_fra_chart)
    private LineChart mChart;
    private PestScreeningParam screeningParam;
    private ArrayList<PestInformation> informations;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // 没有数据时的文本
        mChart.setNoDataText("sorry,没有数据!!!");
//        mChart.setNoDataTextDescription("sorry,没有数据!!!");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setVisibleXRange(1, 10);
//        mChart.setAutoScaleMinMaxEnabled(true);

        mChart.setDrawMarkerViews(true);
        mChart.setMarkerView(new MyMarkerView(getContext()));

        // if disabled, scaling ca
        // n be done on x- and y-axis separately
//        mChart.setPinchZoom(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(getResources().getColor(R.color.primary));
        xAxis.setAxisLineWidth(3);
        xAxis.setAvoidFirstLastClipping(true); //设置x轴起点和终点label不超出屏幕

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisLineColor(getResources().getColor(R.color.accent));
        yAxis.setAxisLineWidth(3);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setEnabled(true);
        yAxis.setStartAtZero(true);
        yAxis.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return (int) value + "";
            }
        });

        mChart.getAxisRight().setEnabled(false);

        loadAll();

        setData();
        mChart.animateY(3000, Easing.EasingOption.EaseInCubic);
        mChart.invalidate();
    }

    private void setData() {
        List<String> xVals = new ArrayList<>();
        DateTime startVal;
        DateTime endVal;
        if (null != screeningParam) {
            startVal = new DateTime(screeningParam.getStartTime());
            endVal = new DateTime(screeningParam.getEndTime());
        } else {
            endVal = DateTime.now();
            startVal = endVal.plusMonths(-3);
        }
        DateTime time = new DateTime(startVal);
        List<Entry> yVals = new ArrayList<>();
        int i = 0;
        while (time.isEqual(startVal) || time.isEqual(endVal) || (time.isBefore(endVal) && time.isAfter(startVal))) {
            time = time.plusDays(1);
            xVals.add(time.toString("YYYY/MM/dd"));
            yVals.add(new Entry(0, i++, time.toString("YYYY/MM/dd")));
        }

        ArrayList<PestInformation> list = new ArrayList<>(informations);
        for (PestInformation information : list) {
            int y = information.getValue();
            int x = Days.daysBetween(startVal, DateTime.parse(information.getTime(), DateTimeFormat.forPattern("YYYY/MM/dd HH:mm:ss"))).getDays();
            if (x >= 0 && x < yVals.size()) {
                Entry entry = yVals.get(x);
                entry.setVal(entry.getVal() + y);
            }
        }

        LineDataSet set1 = new LineDataSet(yVals, "害虫数目");
//        set1.enableDashedLine(10f, 5f, 0f);
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLUE);
//        set1.setCircleColor(Color.BLUE);
        set1.setLineWidth(3f);
//        set1.setCircleSize(3f);
//        set1.setDrawCircleHole(true);
        set1.setDrawCircles(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.GREEN);
        set1.setDrawFilled(true);
        set1.setDrawValues(false);
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "";
            }
        });
//        set1.setDrawCubic(true);

        LineData data = new LineData(xVals, set1);
        mChart.setData(data);
        mChart.setVisibleXRange(4, xVals.size());//页面内最少显示3个数据,最多展示所有数据
    }

    /**
     * 加载所有数据
     */
    private void loadAll() {
        informations = new ArrayList<>();

        DataTask task;
        if (null == screeningParam) {//默认查询最近3个月内的所有设备的全部数据
            screeningParam = new PestScreeningParam();
            String dataType = "1";
            long start = DateTime.now().plusMonths(-3).getMillis();
            long end = DateTime.now().getMillis();

            StringBuilder devs = new StringBuilder();
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
            for (String s : deviceItems) {
                devs.append(s);
                devs.append(",");
            }
            if (devs.length() > 0)
                devs.deleteCharAt(devs.length() - 1);

            screeningParam.setDataType(dataType);
            screeningParam.setDevice(devs.toString());
            screeningParam.setStartTime(start);
            screeningParam.setEndTime(end);
        }

        String start = new DateTime(screeningParam.getStartTime()).toString(DATE_FORMAT);
        String end = new DateTime(screeningParam.getEndTime()).toString(DATE_FORMAT);
        task = new DataTask(screeningParam.getDevice(), start, end, screeningParam.getDataType());
        task.setTaskInfo(new TaskInfo<List<PestInformation>>() {
            ProgressDialog pd = new ProgressDialog(getContext());

            @Override
            public void onPreTask() {
                pd.setMessage("正在加载数据");
                pd.setCancelable(false);
                pd.show();
//                dataFreshLayout.setRefreshing(true);
            }

            @Override
            public void onTaskFinish(String b, List<PestInformation> result) {
                if (null != pd && pd.isShowing())
                    pd.dismiss();
//                dataFreshLayout.setRefreshing(false);
                if (b.equals("fail")) {
                    Toast.makeText(getContext(), "获取数据失败!", Toast.LENGTH_LONG).show();
                } else if (b.equals("success")) {
                    informations = new ArrayList<>(result);
//                    adapter.refreshData(informations);
                }
            }
        });
        task.execute();

    }

    /**
     * 加载所有数据
     */
    private void loadAll1() {
        try {
            if (null == screeningParam)
                informations = new ArrayList<>(PestInformationDao.findAll());
            else
                informations = new ArrayList<>(PestInformationDao.find(screeningParam));
        } catch (DbException e) {
            e.printStackTrace();
        }
        //按照发送时间对数据进行排序
        if (null != informations && informations.size() > 1)
            SortUtil.sortList(informations, "sendTime", false);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!menu.hasVisibleItems())
            inflater.inflate(R.menu.data_menu, menu);
        menu.getItem(0).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.data_menu_sort://排序
                break;
            case R.id.data_menu_screening://筛选
                final ScreeningDialog screen = new ScreeningDialog(getContext(), getFragmentManager());
                screen.setOnScreeningFinishListener(new ScreeningDialog.OnScreeningFinishListener() {
                    @Override
                    public void onFinished(PestScreeningParam data) {
                        screeningParam = data;
                        screen.dismiss();
                        loadAll();
//                        loadData();
                        setData();
                        mChart.invalidate();
                    }

                    @Override
                    public PestScreeningParam onOpen() {
                        return screeningParam;
                    }
                });
                screen.show();
                break;
        }
        return true;
    }

    private class MyMarkerView extends MarkerView {

        private TextView date;
        private TextView content;

        /**
         * Constructor. Sets up the MarkerView with a custom layout resource.
         *
         * @param context 上下文对象
         */
        public MyMarkerView(Context context) {
            super(context, R.layout.marker);
            date = (TextView) findViewById(R.id.marker_date);
            content = (TextView) findViewById(R.id.marker_content);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            date.setText(e.getData().toString());
            content.setText("数目:" + e.getVal());
        }

        @Override
        public int getXOffset(float xpos) {
            return 0;
        }

        @Override
        public int getYOffset(float ypos) {
            return -getHeight();
        }
    }
}
