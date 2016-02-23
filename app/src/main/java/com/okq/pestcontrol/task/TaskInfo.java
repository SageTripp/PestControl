package com.okq.pestcontrol.task;

/**
 * Created by Administrator on 2015/12/9.
 */
public interface TaskInfo<T> {
    void onPreTask();
    void onTaskFinish(String b,T result);
}
