package com.okq.pestcontrol.task;

import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.util.Config;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

/**
 * Created by zst on 2016/2/29. 检查更新
 */
public class UpdateTask extends HttpTask<String> {
    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "update");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("oldver", "");
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {
            try {
                JSONObject jsonObject = new JSONObject(r);
                String update = jsonObject.getString("update");
                if ("1".equals(update)) {
                    JSONObject aNew = jsonObject.getJSONObject("new");
                    String version = aNew.getString("version");
                    String url = aNew.getString("url");
                    info.onTaskFinish("success", url);
                } else if ("0".equals(update)) {
                    info.onTaskFinish("success", "isNew");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
