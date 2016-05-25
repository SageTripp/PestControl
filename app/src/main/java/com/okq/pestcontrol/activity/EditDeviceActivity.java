package com.okq.pestcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.adapter.EnvironmentAdapter;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.task.DeviceParamUpdateTask;
import com.okq.pestcontrol.task.TaskInfo;
import com.okq.pestcontrol.widget.ThresholdCloudEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by zst on 2016/3/1.
 */
@ContentView(value = R.layout.activity_device_edit)
public class EditDeviceActivity extends BaseActivity {

    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.device_edit_pest_threshold)
    private ThresholdCloudEditText pestThresholdEt;
    @ViewInject(value = R.id.device_edit_environment_threshold)
    private RecyclerView environmentThresholdRv;
    @ViewInject(value = R.id.device_edit_collect_interval)
    private EditText collectEt;
    @ViewInject(value = R.id.device_edit_upload_interval)
    private EditText uploadEt;
    @ViewInject(value = R.id.device_edit_alert_num_1)
    private EditText tel1Et;
    @ViewInject(value = R.id.device_edit_alert_num_2)
    private EditText tel2Et;
    @ViewInject(value = R.id.device_edit_alert_num_3)
    private EditText tel3Et;
    @ViewInject(value = R.id.device_edit_alert_num_4)
    private EditText tel4Et;
    @ViewInject(value = R.id.device_edit_alert_num_5)
    private EditText tel5Et;

    private EditText[] ets;

    private Device device;
    private EnvironmentAdapter adapter;
    private List<String> environments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        device = (Device) getIntent().getSerializableExtra("device");
        mToolbar.setTitle(device.getDeviceNum());
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
        String[] stringArray = getResources().getStringArray(R.array.pest_kind);
        ArrayList<String> pests = new ArrayList<>();
        Collections.addAll(pests, stringArray);
        pestThresholdEt.setItems(pests);
        init();
    }

    private void init() {
        ets = new EditText[]{tel1Et, tel2Et, tel3Et, tel4Et, tel5Et};
        collectEt.setText(String.format(Locale.getDefault(), "%d", device.getCjjg()));
        uploadEt.setText(String.format(Locale.getDefault(), "%d", device.getUpload()));
        if (!TextUtils.isEmpty(device.getTels())) {
            String[] tels = device.getTels().split(",");
            for (int i = 0; i < tels.length; i++) {
                ets[i].setText(tels[i]);
            }
        }
        if (!TextUtils.isEmpty(device.getUpvalue()))
            pestThresholdEt.addSpan(device.getUpvalue());
        if (!TextUtils.isEmpty(device.getBounds()))
            for (String envir : device.getBounds().split(","))
                environments.add(envir);
        adapter = new EnvironmentAdapter(this, environments);
        environmentThresholdRv.setAdapter(adapter);
        environmentThresholdRv.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("完成");
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("完成")) {
            int collectInterval = TextUtils.isEmpty(collectEt.getText()) ? 0 : Integer.parseInt(collectEt.getText().toString());
            int uploadInterval = TextUtils.isEmpty(uploadEt.getText()) ? 0 : Integer.parseInt(uploadEt.getText().toString());
            StringBuilder tels = new StringBuilder();
            for (EditText ed : ets) {
                if (!TextUtils.isEmpty(ed.getText())) {
                    tels.append(ed.getText());
                    tels.append(",");
                }
            }
            if (tels.toString().endsWith(","))
                tels.deleteCharAt(tels.length() - 1);
            DeviceParamUpdateTask task = new DeviceParamUpdateTask(this, device.getDeviceNum(),
                    collectInterval,
                    uploadInterval,
                    tels.toString(),
                    pestThresholdEt.getAllReturnString()
                    , adapter.getLimit());
            task.setTaskInfo(new TaskInfo<String>() {
                @Override
                public void onPreTask() {

                }

                @Override
                public void onTaskFinish(String b, String result) {
                    if (b.equals("success")) {//修改成功
                        Toast.makeText(EditDeviceActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("result", b);
                        setResult(2, intent);
                        finish();
                    }

                }
            });
            task.execute();
        }
        return true;
    }
}
