package com.okq.pestcontrol.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;

import org.joda.time.DateTime;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 虫害信息详情页面
 * Created by Administrator on 2015/12/18.
 */
@ContentView(value = R.layout.activity_pest_info_details)
public class PestInfoDetailsActivity extends BaseActivity {
    @ViewInject(value = R.id.details_pest_kind)
    private TextView pestKindTv;
    @ViewInject(value = R.id.details_area)
    private TextView areaTv;
    @ViewInject(value = R.id.details_send_time)
    private TextView sendTimeTv;
    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setSubtitle("虫害信息");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        PestInformation pestInfo = (PestInformation) getIntent().getSerializableExtra("pestInfo");
        pestKindTv.setText(pestInfo.getPestKind().getKindName());
        areaTv.setText(pestInfo.getDevice());
        sendTimeTv.setText(new DateTime(pestInfo.getSendTime()).toString("YYYY/MM/dd HH:mm"));
    }

}
