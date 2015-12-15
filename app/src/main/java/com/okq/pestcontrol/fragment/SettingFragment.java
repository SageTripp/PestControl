package com.okq.pestcontrol.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.okq.pestcontrol.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 设置页面
 * Created by Administrator on 2015/12/15.
 */
@ContentView(value = R.layout.fragment_setting)
public class SettingFragment extends BaseFragment {

    @ViewInject(value = R.id.setting_current_version)
    private TextView currentVersion;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentVersion.setText(String.format("当前版本:V%s", getVersion()));
    }

    @Event(value = R.id.setting_check_update)
    private void checkUpdateEvent(View view) {
        //TODO 检查更新网络请求代码
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

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号 或 -1:表示获取失败,出现异常
     */
    private int getVersionCode() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
