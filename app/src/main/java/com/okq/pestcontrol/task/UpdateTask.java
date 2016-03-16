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
    private String oldVer;

    public UpdateTask(String oldVer) {
        this.oldVer = oldVer;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "update");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("oldver", oldVer);
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {
            if (!r.contains("ex:")) {
                try {
                    JSONObject jsonObject = new JSONObject(r);
                    String update = jsonObject.getString("update");
                    if ("1".equals(update)) {
                        JSONObject aNew = jsonObject.getJSONObject("new");
                        String version = aNew.getString("betsion");
                        String url = aNew.getString("url");
                        info.onTaskFinish("success", url + "##" + version);
                    } else if ("0".equals(update)) {
                        info.onTaskFinish("fail", "isNew");
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    info.onTaskFinish("fail", "解析错误");
                }
            } else {
                info.onTaskFinish("fail", r);
            }
        }
    }
}
