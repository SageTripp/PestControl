package com.okq.pestcontrol.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.activity.AboutUsActivity;
import com.okq.pestcontrol.task.DownloadTask;
import com.okq.pestcontrol.task.TaskInfo;
import com.okq.pestcontrol.task.UpdateTask;

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
        final UpdateTask task = new UpdateTask(getContext(), getVersion());
        task.setTaskInfo(new TaskInfo<String>() {
            ProgressDialog dialog = new ProgressDialog(getContext());

            @Override
            public void onPreTask() {
                dialog.setMessage("正在检查更新");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            public void onTaskFinish(String b, String result) {
                if (null != dialog && dialog.isShowing())
                    dialog.dismiss();
                if (b.equals("success")) {
                    String[] split = result.split("##");
                    final String url = split[0];
                    final String version = split[1];
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("检查更新")
                            .setMessage("发现新版本:" + version)
                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DownloadTask task1 = new DownloadTask(getContext(), url, version);
                                    task1.setListener(new TaskInfo<Void>() {
                                        @Override
                                        public void onPreTask() {

                                        }

                                        @Override
                                        public void onTaskFinish(String b, Void result) {

                                        }
                                    });
                                    task1.execute();
                                }
                            })
                            .create().show();
                }
            }
        });
        task.execute();
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
