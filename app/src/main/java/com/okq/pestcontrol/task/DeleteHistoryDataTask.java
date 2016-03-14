package com.okq.pestcontrol.task;

import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.util.Config;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

/**
 * Created by zst on 2016/2/29. 删除历史数据
 */
public class DeleteHistoryDataTask extends HttpTask<String> {
    private String ids;

    /**
     * 删除历史数据
     *
     * @param ids 要删除的数据的id集合
     */
    public DeleteHistoryDataTask(String ids) {
        this.ids = ids;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "operator");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("type", "delete");
        params.addQueryStringParameter("ids", ids);//设备列表,多个设备用逗号间隔
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
                info.onTaskFinish("success", "");
            }
        }
    }
}
