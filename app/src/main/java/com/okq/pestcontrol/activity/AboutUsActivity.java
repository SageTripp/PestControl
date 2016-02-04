package com.okq.pestcontrol.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.okq.pestcontrol.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 关于我们 Created by zst on 2016/2/4.
 */
@ContentView(value = R.layout.activity_about_us)
public class AboutUsActivity extends BaseActivity {

    @ViewInject(value = R.id.about_us_wv)
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView.loadUrl("http://www.zzokq.cn/news/web/shtml/T-159.htm");
    }
}
