package com.okq.pestcontrol.task;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.util.Config;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
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
    public DeviceTask(Context context, int scope) {
        mContext = context;
        this.scope = scope;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "devices");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("devtype", "4");
        params.addQueryStringParameter("scope", scope + "");
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {
            if (!r.contains("ex:")) {
                try {
                    if (scope == 1)
                        try {
                            x.getDb(App.getDaoConfig()).dropTable(Device.class);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                    final List<Device> list = new ArrayList<>();
                    JSONObject object = new JSONObject(r);
                    String devices = object.getString("devices");
                    if (TextUtils.isEmpty(devices)) {
                        info.onTaskFinish("fail", null);
                        return;
                    } else {
                        String[] split = devices.split(",");
                        for (String s : split) {
                            Device device = new Device();
                            device.setDeviceNum(s);
                            list.add(device);
                        }
                    }
                    info.onTaskFinish("success", list);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "解析设备数据失败", Toast.LENGTH_LONG).show();
                    info.onTaskFinish("fail", null);
                }
            } else {
                Toast.makeText(mContext, r, Toast.LENGTH_LONG).show();
                info.onTaskFinish("fail", null);
            }
        }
    }
}
