package com.okq.pestcontrol.task;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Administrator on 2015/12/9.
 */
public class BaseTask extends AsyncTask<Void, Void, Object> {

    TaskInfo info;
    Context mContext;

    @Override
    protected Object doInBackground(Void... params) {
        return null;
    }

    public void setTaskInfo(TaskInfo info) {
        this.info = info;
    }
}
