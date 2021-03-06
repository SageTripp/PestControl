package com.okq.pestcontrol.task;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.util.Config;
import com.tencent.bugly.crashreport.BuglyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by zst on 2016/2/29. 历史数据查询
 */
public class DataTask extends HttpTask<List<PestInformation>> {

    private String devs;
    private String begin;
    private String end;
    private String dataType;

    /**
     * 历史数据查询
     *
     * @param devs  设备列表,以逗号间隔
     * @param begin 开始日期
     * @param end   结束日期
     */
    public DataTask(Context context, String devs, String begin, String end, String dataType) {
        mContext = context;
        this.devs = devs;
        this.begin = begin;
        this.end = end;
        this.dataType = dataType;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "view");
        params.addQueryStringParameter("token", App.getToken());
        params.addQueryStringParameter("devtype", "4");
        params.addQueryStringParameter("devs", devs);//设备列表,多个设备用逗号间隔
        params.addQueryStringParameter("begin", begin);//开始日期 2016-10-10
        params.addQueryStringParameter("end", end);//结束日期 2016-10-10
        params.addQueryStringParameter("datatype", dataType);
        BuglyLog.d("task", params.getUri());
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {
            if (!r.contains("ex:") || TextUtils.isEmpty(r)) {
                try {
                    JSONObject object = new JSONObject(r);
//                    String total = object.getString("total");
                    JSONArray rows = object.getJSONArray("rows");
                    Gson gson = new Gson();
                    List<PestInformation> list = gson.fromJson(rows.toString(), new TypeToken<List<PestInformation>>() {
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
                    info.onTaskFinish("fail", null);
                }
            } else {
                info.onTaskFinish("fail", null);
            }
        }

    }
}
