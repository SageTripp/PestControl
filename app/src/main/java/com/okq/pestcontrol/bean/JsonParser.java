package com.okq.pestcontrol.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2015/12/14.
 */
public class JsonParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        Gson gson = new Gson();
//        JSONObject jo = new JSONObject(result);
//        JSONObject weatherinfo = (JSONObject) jo.get("Test");
        Object o = gson.fromJson(result, resultType);
        return o;
    }
}
