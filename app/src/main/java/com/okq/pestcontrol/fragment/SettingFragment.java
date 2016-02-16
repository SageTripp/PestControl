package com.okq.pestcontrol.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.activity.AboutUsActivity;
import com.okq.pestcontrol.service.TestSocketService;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 设置页面 Created by Administrator on 2015/12/15.
 */
@ContentView(value = R.layout.fragment_setting)
public class SettingFragment extends BaseFragment {

    @ViewInject(value = R.id.setting_current_version)
    private TextView currentVersion;
    @ViewInject(value = R.id.setting_about_us)
    private TextView aboutUs;
    private TestSocketService testService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentVersion.setText(String.format("当前版本:V%s", getVersion()));
    }

    @Event(value = R.id.setting_check_update)
    private void checkUpdateEvent(View view) {
        //TODO 检查更新网络请求代码
    }

    @Event(value = R.id.setting_about_us)
    private void aboutUs(View view) {
        //TODO 关于我们页面
        startActivity(new Intent(getContext(), AboutUsActivity.class));
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersion() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
