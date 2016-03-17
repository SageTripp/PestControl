package com.okq.pestcontrol.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.okq.pestcontrol.util.Config;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * Created by zst on 2016/3/2. 下载任务
 */
public class DownloadTask {

    private TaskInfo<Void> taskInfo;
    private RequestParams params;
    private ProgressDialog dialog;
    private Context mContext;
    private final String version;
    private String url;

    public DownloadTask(Context context, String url, String version) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在下载...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        mContext = context;
        this.url = url;
        this.version = version;
    }

    public void onPre() {
        if (null != taskInfo)
            taskInfo.onPreTask();
        doInBackground();
    }

    private void doInBackground() {
        params = new RequestParams(url);
        params.setAutoResume(true);
        params.setAutoRename(true);
        params.setSaveFilePath(Config.DOWNLOAD_PATH + File.separator + "pestControl.apk");
        Executor executor = new PriorityExecutor(1);
        params.setExecutor(executor);
        dialog.show();
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (null != dialog && dialog.isShowing()) {
                    dialog.setMax((int) total);
                    dialog.setProgress((int) (current));
                }
            }

            @Override
            public void onSuccess(File result) {
                LogUtil.i("下载成功" + result.getName());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(result),
                        "application/vnd.android.package-archive");
                mContext.startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("下载失败", ex);
                Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (null != dialog && dialog.isShowing())
                    dialog.dismiss();
                if (null != taskInfo)
                    taskInfo.onTaskFinish("success", null);
            }
        });
    }

    public void setListener(TaskInfo<Void> taskInfo) {
        this.taskInfo = taskInfo;
    }

    public void execute() {
        onPre();
    }
}

