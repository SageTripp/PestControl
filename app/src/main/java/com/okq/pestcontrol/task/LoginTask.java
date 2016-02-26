package com.okq.pestcontrol.task;

import android.content.Context;

import com.okq.pestcontrol.bean.Test;

import org.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by zst on 2016/2/23. 登录
 */
public class LoginTask extends HttpTask<List<Test>> {

    private String userName = "";
    private String passWord = "";

    public LoginTask(Context context, String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        mContext = context;
    }

    @Override
    RequestParams setParams() {
//        RequestParams params = new RequestParams(Config.URL+"login");
//        params.addQueryStringParameter("user", userName);
//        params.addQueryStringParameter("pass", passWord);
        RequestParams params = new RequestParams("http://api.1-blog.com/biz/bizserver/news/list.do");
        params.addQueryStringParameter("size", "8");
        return params;
    }
}
