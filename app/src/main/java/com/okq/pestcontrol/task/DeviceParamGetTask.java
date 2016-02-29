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
 * Created by zst on 2016/2/29. 获取设备参数
 */
public class DeviceParamGetTask extends HttpTask<List<Device>> {

    private String devs;

    /**
     * 获取设备参数
     *
     * @param devs 要获取的设备
     */
    public DeviceParamGetTask(String devs) {
        this.devs = devs;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "param");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("devices", devs);
        params.addQueryStringParameter("type", "get");//设备列表,多个设备用逗号间隔
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {
            try {
                JSONObject jsonObject = new JSONObject(r);
                String status = jsonObject.getString("status");
                if ("success".equals(status)) {
                    JSONArray content = jsonObject.getJSONArray("content");
                    Gson gson = new Gson();
                    List<Device> list = gson.fromJson(content.toString(), new TypeToken<List<Device>>() {
                    }.getType());
                    if (list != null) {
                        info.onTaskFinish("success", list);
                        return;
                    } else {
                        info.onTaskFinish("fail", null);
                        return;
                    }
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
