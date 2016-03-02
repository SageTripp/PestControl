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
                        String version = aNew.getString("version");
                        String url = aNew.getString("url");
                        info.onTaskFinish("success", url + "##" + version);
                    } else if ("0".equals(update)) {
                        info.onTaskFinish("fail", "isNew");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                info.onTaskFinish("success", "http://dl42.yunpan.360.cn/intf.php?method=Download.downloadFile&qid=345576200&fname=%2Fapk%2Fapp-debug.apk&fhash=ec3fad865433cec7a4dc6edf5b4da21969ead518&dt=42_42.21f9e4f9cbf0458037d021e68c1397da&v=1.0.1&rtick=14569065717383&open_app_id=0&devtype=web&sign=f59dd10c6572b17ee22daa76514fcf02&"+"##1.0");
            }
        }
    }
}
