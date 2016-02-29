package com.okq.pestcontrol.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by zst on 2016/2/29. 请求设备列表
 */
public class DeviceTask extends HttpTask<List<Device>> {
    public static final int SCOPE_ONLINE = 2;
    public static final int SCOPE_ALL = 1;

    private int scope = SCOPE_ALL;

    /**
     * 请求设备列表
     *
     * @param scope 设备状态
     */
    public DeviceTask(int scope) {
        this.scope = scope;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "devices");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("devType", "4");
        params.addQueryStringParameter("scope", scope + "");
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {
            try {
                JSONObject object = new JSONObject(r);
                JSONArray devices = object.getJSONArray("devices");
                Gson gson = new Gson();
                List<Device> list = gson.fromJson(devices.toString(), new TypeToken<List<Device>>() {
                }.getType());
                if (list != null) {
                    info.onTaskFinish("success", list);
                    return;
                } else {
                    info.onTaskFinish("fail", null);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
