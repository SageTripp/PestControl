package com.okq.pestcontrol.task;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Administrator on 2015/12/9.
 */
public abstract class BaseTask<R> extends AsyncTask<Void, Void, R> {

    TaskInfo info;
    Context mContext;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(null != info){
            info.onPreTask();
        }
    }

    @Override
    protected R doInBackground(Void... params) {
        return myDoInBackground();
    }
    protected abstract R myDoInBackground();

    @Override
    protected void onPostExecute(R r) {
        super.onPostExecute(r);
        if(null != info){
            info.onTaskFinish("",r);
        }
    }

    public void setTaskInfo(TaskInfo info) {
        this.info = info;
    }
}
