package com.okq.pestcontrol.task;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by zst on 2016/2/23.
 */
public abstract class HttpTask<R> {

    TaskInfo<R> info;

    void onPre() {
        if (null != info) {
            info.onPreTask();
        }
        doInBackground();
    }

    void doInBackground() {
        RequestParams params = setParams();
        x.http().get(params, new Callback.CommonCallback<R>() {
            @Override
            public void onSuccess(R result) {
                if (null != info)
                    info.onTaskFinish("success", result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (null != info)
                    info.onTaskFinish("fail:", null);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (null != info)
                    info.onTaskFinish("cancel", null);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void setTaskInfo(TaskInfo<R> taskInfo) {
        this.info = taskInfo;
    }

    abstract RequestParams setParams();

    public void execute() {
        onPre();
    }
}
