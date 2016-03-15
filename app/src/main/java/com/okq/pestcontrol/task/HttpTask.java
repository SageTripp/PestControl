package com.okq.pestcontrol.task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.WindowManager;

import com.okq.pestcontrol.util.NetUtil;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;

/**
 * Created by zst on 2016/2/23. http通信任务
 */
public abstract class HttpTask<R> {

    TaskInfo<R> info;
    Context mContext;

    void onPre() {
        if (null != mContext) {
            //检测网络,如果没有网络,跳转到网络设置页面
            if (!NetUtil.isNetworkAvailable(mContext)) {
                AlertDialog dialog = new AlertDialog.Builder(mContext).create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.setMessage("当前网络不可用,请检查您的网络连接");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                return;
            }
        }
        if (null != info) {
            info.onPreTask();
        }
        doInBackground();
    }

    void doInBackground() {
        RequestParams params = setParams();
        params.setConnectTimeout(15 * 1000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                finish(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("fail:" + ex);
                if(ex instanceof SocketTimeoutException)
                    finish("ex:请求设备超时");
                else
                    finish("ex:"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("cancel:" + cex);
            }

            @Override
            public void onFinished() {
//                LogUtil.e("finish");
//                if (null != info) {
//                    info.onTaskFinish("", null);
//                }
            }
        });
    }

    public void setTaskInfo(TaskInfo<R> taskInfo) {
        this.info = taskInfo;
    }

    abstract RequestParams setParams();

    abstract void finish(String r);

    public void execute() {
        onPre();
    }
}
