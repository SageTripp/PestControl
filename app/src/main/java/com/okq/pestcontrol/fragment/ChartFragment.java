package com.okq.pestcontrol.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.param.PestScreeningParam;
import com.okq.pestcontrol.dbDao.PestInformationDao;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/12/12.
 */
@ContentView(value = R.layout.fragment_chart)
public class ChartFragment extends BaseFragment implements OnChartValueSelectedListener {

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
        mChart.setDescription("");
        mChart.setNoDataTextDescription("sorry,没有数据!!!");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling ca
        // n be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(getResources().getColor(R.color.primary));
        xAxis.setAxisLineWidth(3);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisLineColor(getResources().getColor(R.color.accent));
        yAxis.setAxisLineWidth(3);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setEnabled(true);
        yAxis.setStartAtZero(true);

        mChart.getAxisRight().setEnabled(false);

        loadAll();

        setData();
        mChart.animateY(3000, Easing.EasingOption.EaseInCubic);
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
            startVal = endVal.plusMonths(-6);
        }
        DateTime time = new DateTime(startVal);
        List<Entry> yVals = new ArrayList<>();
        int i = 0;
        while (time.isEqual(startVal) || time.isEqual(endVal) || (time.isBefore(endVal) && time.isAfter(startVal))) {
            time = time.plusDays(1);
            xVals.add(time.toString("YYYY/MM/dd"));
            yVals.add(new Entry(0, i++));
        }

        ArrayList<PestInformation> list = new ArrayList<>(informations);
        for (PestInformation information : list) {
            int y = information.getPestNum();
            int x = Days.daysBetween(startVal, new DateTime(information.getSendTime())).getDays();
            if (x >= 0 && x < yVals.size()) {
                Entry entry = yVals.get(x);
                entry.setVal(entry.getVal() + y);
            }
        }

        LineDataSet set1 = new LineDataSet(yVals, "害虫总数");
//        set1.enableDashedLine(10f, 5f, 0f);
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.GREEN);
        set1.setCircleColor(Color.BLUE);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(true);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.GREEN);
        set1.setDrawFilled(true);

        LineData data = new LineData(xVals, set1);
        mChart.setData(data);
    }

    /**
     * 加载所有数据
     */
    private void loadAll() {
        try {
            if (null == screeningParam)
                informations = new ArrayList<>(PestInformationDao.findAll());
            else
                informations = new ArrayList<>(PestInformationDao.find(screeningParam));
        } catch (DbException e) {
            e.printStackTrace();
        }
        //按照发送时间对数据进行排序
        Collections.sort(informations, new Comparator<PestInformation>() {
            @Override
            public int compare(PestInformation lhs, PestInformation rhs) {
                return (int) (lhs.getSendTime() - rhs.getSendTime());
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
