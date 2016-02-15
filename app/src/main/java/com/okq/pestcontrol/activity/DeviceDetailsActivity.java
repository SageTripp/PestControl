package com.okq.pestcontrol.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.Device;

import org.joda.time.DateTime;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

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
    @ViewInject(value = R.id.device_details_buy_time)
    private TextView buyTimeTv;
    @ViewInject(value = R.id.device_details_install_time)
    private TextView installTimeTv;
    @ViewInject(value = R.id.device_details_remove_time)
    private TextView removeTimeTv;
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
        collapsingToolbarLayout.setTitle(String.format("%s(%s)", device.getDeviceNum(), device.getDeviceModel()));
        collapsingToolbarLayout.setExpandedTitleColor(Color.BLUE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.primary_dark));
        map.showScaleControl(false);
        map.showZoomControls(false);
        baiduMap = map.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        setMap(device.getLat(), device.getLon());
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
                .zoom(14)
                .build();
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }

    private void setDeviceData() {
        areaTv.setText(device.getArea());
        addressTv.setText(device.getPlace());
        positionTv.setText(String.format("%s,%s", device.getLat(), device.getLon()));

        statusTv.setText(device.getStatus());
        buyTimeTv.setText(String.format("购买时间:%s", device.getBuyTime() == 0.0 ? "----/--/--" : new DateTime(device.getBuyTime()).toString(DATE_FORMAT)));
        installTimeTv.setText(String.format("安装时间:%s", device.getInstallTime() == 0.0 ? "----/--/--" : new DateTime(device.getInstallTime()).toString(DATE_FORMAT)));
        removeTimeTv.setText(String.format("拆除时间:%s", device.getRemoveTime() == 0.0 ? "----/--/--" : new DateTime(device.getRemoveTime()).toString(DATE_FORMAT)));
    }

}
