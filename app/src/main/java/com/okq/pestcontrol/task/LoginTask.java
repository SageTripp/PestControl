package com.okq.pestcontrol.task;

import android.content.Context;

import com.okq.pestcontrol.util.Config;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

/**
 * Created by zst on 2016/2/23. 登录
 */
public class LoginTask extends HttpTask<String> {

    private String userName = "";
    private String passWord = "";

    public LoginTask(Context context, String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        mContext = context;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL + "login");
        params.addQueryStringParameter("user", userName);
        params.addQueryStringParameter("pass", passWord);
        params.addBodyParameter("from", "1");
//        RequestParams params = new RequestParams("http://api.1-blog.com/biz/bizserver/news/list.do");
//        params.addQueryStringParameter("size", "8");
        return params;
    }

    @Override
    void finish(String r) {
        if (null != info) {

            if (!r.contains("ex:")) {
                try {
                    JSONObject jsonObject = new JSONObject(r);
                    String status = jsonObject.getString("status");
                    if ("success".equals(status)) {
                        String token = jsonObject.getString("accesstoken");
                        CrashReport.setUserId(userName);
                        info.onTaskFinish("success", token);
                        return;
                    } else if ("fail".equals(status)) {
                        String errmsg = jsonObject.getString("errmsg");
                        info.onTaskFinish("fail", errmsg);
                        return;
                    } else {
                        info.onTaskFinish("fail", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    info.onTaskFinish("fail", null);
                }
            } else {
//                Toast.makeText(mContext, r, Toast.LENGTH_LONG).show();
                String failReason = "连接服务器异常";
                if (r.contains("after"))
                    failReason = "连接超时";
                info.onTaskFinish("fail", failReason);
            }
        }
    }
}
