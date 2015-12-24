package com.okq.pestcontrol.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.okq.pestcontrol.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 设备详情页面
 * Created by Administrator on 2015/12/23.
 */
@ContentView(value = R.layout.activity_device_details)
public class DeviceDetailsActivity extends BaseActivity {

    @ViewInject(value = R.id.device_details_toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.device_details_collapsing)
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mToolbar.setTitle("测试");
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
        collapsingToolbarLayout.setTitle("测试一下好玩不");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.primary));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.LEFT);
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.primary_dark));
    }

}
