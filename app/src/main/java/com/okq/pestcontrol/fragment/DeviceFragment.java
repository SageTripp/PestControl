package com.okq.pestcontrol.fragment;

import android.os.Bundle;
import android.view.View;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.Test;
import com.okq.pestcontrol.bean.param.TestParam;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.List;

/**
 * 设备页面
 * Created by Administrator on 2015/12/3.
 */
@ContentView(value = R.layout.fragment_device)
public class DeviceFragment extends BaseFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Event(value = R.id.test_http_btn)
    private void testHttpEvent(View v) {
        TestParam param = new TestParam();
//        param.addBodyParameter("op", "getWeather");
        param.addBodyParameter("theCityCode", "郑州");
        param.addBodyParameter("theUserId", "");
        x.http().post(param, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.v(result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(cex.toString());
            }

            @Override
            public void onFinished() {

            }
        });
    }

}
