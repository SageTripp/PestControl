package com.okq.pestcontrol.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.widget.CustomLayoutManager;

import org.joda.time.DateTime;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
import java.util.List;

/**
 * 虫害信息详情页面
 * Created by Administrator on 2015/12/18.
 */
@ContentView(value = R.layout.activity_pest_info_details)
public class PestInfoDetailsActivity extends BaseActivity {
    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.pest_info_details_name)
    private TextView nameTv;
    @ViewInject(value = R.id.pest_info_details_device)
    private TextView deviceTv;
    @ViewInject(value = R.id.pest_info_details_date)
    private TextView dateTv;
    @ViewInject(value = R.id.pest_info_details_environments)
    private RecyclerView environments;
    private PestInformation pestInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setSubtitle("虫害信息");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        pestInfo = (PestInformation) getIntent().getSerializableExtra("pestInfo");
        nameTv.setText(pestInfo.getName());
        deviceTv.setText(String.format("采集设备:%s", pestInfo.getDevice()));
        dateTv.setText(pestInfo.getSendTime());
//        environments.setHasFixedSize(true);
        environments.setAdapter(new Adapter(pestInfo.getEnvironments()));
        environments.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private List<String> list;

        public Adapter(String envirs) {
            envirs.split(",");
            list = Arrays.asList(envirs.split(","));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(PestInfoDetailsActivity.this, R.layout.holder_environment, parent));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setEnvironment(list.get(position).replace("=", ":"));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView environmentTv;

            public ViewHolder(View itemView) {
                super(itemView);
                environmentTv = (TextView) itemView.findViewById(R.id.pest_info_details_holder_environment);
            }

            public void setEnvironment(String environment) {
                environmentTv.setText(environment);
            }

        }
    }

}
