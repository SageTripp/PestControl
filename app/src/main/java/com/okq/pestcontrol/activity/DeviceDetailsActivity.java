package com.okq.pestcontrol.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.task.DeviceParamGetTask;
import com.okq.pestcontrol.task.TaskInfo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Locale;

/**
 * 设备详情页面 Created by Administrator on 2015/12/23.
 */
@ContentView(value = R.layout.activity_device_details)
public class DeviceDetailsActivity extends BaseActivity {

    @ViewInject(value = R.id.device_details_toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.device_details_collapsing)
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @ViewInject(value = R.id.device_details_map)
    private MapView map;

    @ViewInject(value = R.id.device_details_area)
    private TextView areaTv;
    @ViewInject(value = R.id.device_details_address)
    private TextView addressTv;
    @ViewInject(value = R.id.device_details_position)
    private TextView positionTv;

    @ViewInject(value = R.id.device_details_status)
    private TextView statusTv;
    @ViewInject(value = R.id.device_details_collect_interval)
    private TextView collectIntervalTv;
    @ViewInject(value = R.id.device_details_upload_interval)
    private TextView upIntervalTv;
    @ViewInject(value = R.id.device_details_tels)
    private TextView telsTv;
    @ViewInject(value = R.id.device_details_pest_threshold)
    private TextView pestThresholdTv;
    @ViewInject(value = R.id.device_details_environment_threshold)
    private TextView environmentThresholdTv;
    @ViewInject(value = R.id.device_details_edit_btn)
    private Button editBtn;
    @ViewInject(value = R.id.device_details_param_card)
    private CardView paramCard;
    private BaiduMap baiduMap;
    private Device device;

    private static final String DATE_FORMAT = "YYYY/MM/dd";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        device = (Device) getIntent().getSerializableExtra("device");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbarLayout.setTitle(device.getDeviceNum());
        collapsingToolbarLayout.setExpandedTitleColor(Color.BLUE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.primary_dark));
        map.showScaleControl(false);
        map.showZoomControls(false);
        baiduMap = map.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        setMap(device.getWd(), device.getJd());
        UiSettings uiSettings = baiduMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(false);
        setDeviceData();
    }

    private void setMap(double lat, double lon) {
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_map_location);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .zIndex(9)
                .draggable(false);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
        MapStatus mapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(15)
                .build();
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }

    private void setDeviceData() {
//        areaTv.setText(device.getDeviceid());
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult != null && reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR)
                    areaTv.setText(reverseGeoCodeResult.getAddressDetail().district);
                addressTv.setText(reverseGeoCodeResult.getAddress());
            }
        });
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(new LatLng(device.getWd(), device.getJd()));
        geoCoder.reverseGeoCode(option);
        positionTv.setText(String.format("%s,%s", device.getWd(), device.getJd()));

        statusTv.setText(device.getStatus() == 1 ? "在线" : "离线");
        collectIntervalTv.setText(String.format(Locale.getDefault(),"采集间隔:%d", device.getCjjg()));
        upIntervalTv.setText(String.format(Locale.getDefault(), "上传间隔:%d", device.getUpload()));
        telsTv.setText(String.format("报警号码:%s", device.getTels()));
        pestThresholdTv.setText(String.format("害虫阈值:%s", device.getPestThreshold()));
        environmentThresholdTv.setText(String.format("环境阈值:%s", device.getEnvironmentThreshold()));
        if (device.getStatus() == 1) {
            editBtn.setVisibility(View.VISIBLE);
            paramCard.setCardBackgroundColor(getResources().getColor(R.color.TEAL));
        }
    }

    /**
     * 刷新设备参数信息
     */
    private void refresh() {
        statusTv.setText(device.getStatus() == 1 ? "在线" : "离线");
        collectIntervalTv.setText(String.format(Locale.getDefault(), "采集间隔:%d小时", device.getCjjg()));
        upIntervalTv.setText(String.format(Locale.getDefault(), "上传间隔:%d小时", device.getUpload()));
        telsTv.setText(String.format("报警号码:%s", device.getTels()));
        pestThresholdTv.setText(String.format("害虫阈值:%s", device.getPestThreshold()));
        environmentThresholdTv.setText(String.format("环境阈值:%s", device.getEnvironmentThreshold()));
    }

    @Event(value = R.id.device_details_edit_btn)
    private void editDevice(View view) {
        Intent intent = new Intent(this, EditDeviceActivity.class);
        intent.putExtra("device", device);
        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {//修改成功
            DeviceParamGetTask task = new DeviceParamGetTask(this, device.getDeviceNum());
            task.setTaskInfo(new TaskInfo<Device>() {
                @Override
                public void onPreTask() {

                }

                @Override
                public void onTaskFinish(String b, Device result) {
                    if (b.equals("success")) {
                        device = result;
                        refresh();
                    }
                }
            });
            task.execute();
        }
    }
}
