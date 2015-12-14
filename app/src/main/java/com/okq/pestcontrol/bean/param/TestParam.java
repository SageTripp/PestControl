package com.okq.pestcontrol.bean.param;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by Administrator on 2015/12/14.
 */
@HttpRequest(host = "http://webservice.webxml.com.cn", path = "WebServices/WeatherWS.asmx/getWeather")
public class TestParam extends RequestParams {
}
