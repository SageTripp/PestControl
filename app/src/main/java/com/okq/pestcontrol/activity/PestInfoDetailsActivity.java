package com.okq.pestcontrol.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
import java.util.List;

/**
 * 虫害信息详情页面
 * Created by Administrator on 2015/12/18.
 */
@ContentView(value = R.layout.activity_pest_details)
public class PestInfoDetailsActivity extends BaseActivity {
    @ViewInject(value = R.id.pest_details_toolbar)
    private Toolbar mToolbar;
    //    @ViewInject(value = R.id.pest_details_name)
//    private TextView nameTv;
    @ViewInject(value = R.id.pest_details_device)
    private TextView deviceTv;
    @ViewInject(value = R.id.pest_details_date)
    private TextView dateTv;
    @ViewInject(value = R.id.pest_details_environments)
    private RecyclerView environments;
    @ViewInject(value = R.id.pest_details_collapsing)
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private PestInformation pestInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        pestInfo = (PestInformation) getIntent().getSerializableExtra("pestInfo");
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
        collapsingToolbarLayout.setTitle(pestInfo.getPest()+"("+pestInfo.getValue()+"只)");
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.primary_dark));
        if (supportActionBar != null) {
            supportActionBar.setTitle(pestInfo.getPest());
        }
//        nameTv.setText(pestInfo.getPest());
        deviceTv.setText(String.format("采集设备:%s", pestInfo.getDeviceid()));
        dateTv.setText(pestInfo.getTime());
//        environments.setHasFixedSize(true);
        environments.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        environments.setAdapter(new Adapter(pestInfo.getEnvironments()));

    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private List<String> list;

        public Adapter(String envirs) {
            envirs.split(",");
            list = Arrays.asList(envirs.split(","));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(PestInfoDetailsActivity.this).inflate(R.layout.holder_environment, parent, false));
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
