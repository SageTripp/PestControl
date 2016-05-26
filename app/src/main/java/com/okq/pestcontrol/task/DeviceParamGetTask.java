package com.okq.pestcontrol.task;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.kotlin.Trans;
import com.okq.pestcontrol.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by zst on 2016/2/29. 获取设备参数
 */
public class DeviceParamGetTask extends HttpTask<Device> {

    private String devs;

    /**
     * 获取设备参数
     *
     * @param devs 要获取的设备
     */
    public DeviceParamGetTask(Context context, String devs) {
        mContext = context;
        this.devs = devs;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "param");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("devices", devs);//设备列表,多个设备用逗号间隔
        params.addQueryStringParameter("type", "get");
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {
            try {
                JSONObject jsonObject = new JSONObject(r);
                if (r.contains("status")) {
                    String status = jsonObject.getString("status");
                    if ("success".equals(status)) {
//                        JSONArray content = jsonObject.getJSONArray("content");
                        JSONObject content = jsonObject.getJSONObject("content");
                        Gson gson = new Gson();
                        Device dev = gson.fromJson(content.toString(), new TypeToken<Device>() {
                        }.getType());
                        if (dev != null) {
                            dev.setDeviceNum(devs);
                            dev.setBounds(Trans.Companion.transEnvir(dev.getBounds(), Trans.TransDirection.INDEX2NAME));
                            dev.setUpvalue(Trans.Companion.transPest(dev.getUpvalue(), Trans.TransDirection.INDEX2NAME));
                            info.onTaskFinish("success", dev);
                            return;
                        } else {
                            info.onTaskFinish("fail", null);
                            return;
                        }
                    } else {
                        info.onTaskFinish("fail", null);
                        return;
                    }
                } else {
                    String errMsg = jsonObject.getString("errmsg");
                    Toast.makeText(mContext, errMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            info.onTaskFinish("fail", null);
        }
    }
}
