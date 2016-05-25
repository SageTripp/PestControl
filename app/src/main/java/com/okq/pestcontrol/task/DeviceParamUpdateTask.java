package com.okq.pestcontrol.task;

import android.content.Context;

import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.util.Config;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

/**
 * Created by zst on 2016/2/29. 更新/设置设备参数
 */
public class DeviceParamUpdateTask extends HttpTask<String> {

    private String devices;
    private int collectInterval;
    private int updateInterval;
    private String tels;
    private String pestWarnVal;
    private String envirWarnVal;

    /**
     * 更新/设置设备参数
     *
     * @param devices         要设置的设备
     * @param collectInterval 采集间隔
     * @param updateInterval  上传间隔
     * @param tels            报警号码
     * @param pestWarnVal     害虫阈值
     * @param envirWarnVal    环境阈值
     */
    public DeviceParamUpdateTask(Context context, String devices, int collectInterval, int updateInterval, String tels, String pestWarnVal, String envirWarnVal) {
        mContext = context;
        this.devices = devices;
        this.collectInterval = collectInterval;
        this.updateInterval = updateInterval;
        this.tels = tels;
        this.pestWarnVal = pestWarnVal;
        this.envirWarnVal = envirWarnVal;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "param");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("devices", devices);
        params.addQueryStringParameter("type", "set");//设备列表,多个设备用逗号间隔
        params.addQueryStringParameter("cjjg", collectInterval + "");//开始日期 2016-10-10
        params.addQueryStringParameter("upload", updateInterval + "");//结束日期 2016-10-10
        params.addQueryStringParameter("tels", tels);
        params.addQueryStringParameter("upvalue", pestWarnVal);
        params.addQueryStringParameter("bounds", envirWarnVal);
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {
            try {
                JSONObject jsonObject = new JSONObject(r);
                String status = jsonObject.getString("status");
                info.onTaskFinish(status, status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
