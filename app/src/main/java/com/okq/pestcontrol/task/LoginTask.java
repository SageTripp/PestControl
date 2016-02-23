package com.okq.pestcontrol.task;

import com.okq.pestcontrol.util.Config;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;

/**
 * Created by zst on 2016/2/23.
 */
public class LoginTask extends HttpTask<String> {

    private String userName = "";
    private String passWord = "";

    public LoginTask(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    @Override
    RequestParams setParams() {
        RequestParams params = new RequestParams(Config.URL+"login");
        params.addQueryStringParameter("user", userName);
        params.addQueryStringParameter("pass", passWord);
        return params;
    }
}
