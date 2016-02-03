package com.okq.pestcontrol.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.service.TestSocketService;

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
    @ViewInject(value = R.id.setting_about_us)
    private TextView aboutUs;
    @ViewInject(value = R.id.test)
    private EditText test;
    ServiceConnection conn;
    private TestSocketService testService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                testService = ((TestSocketService.SimpleBinder) service).getTestService();
                testService.setOnMsgBackListener(new TestSocketService.OnMsgBackListener() {
                    @Override
                    public void onMsgBack(final String msg) {
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        getContext().bindService(new Intent(getContext(), TestSocketService.class), conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unbindService(conn);
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
    private void aboutUs(View view){
        Intent view1 = new Intent();
        view1.setAction(Intent.ACTION_VIEW);
        view1.setData(Uri.parse("http://www.zzokq.cn/news/web/shtml/T-159.htm"));
        startActivity(view1);
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
