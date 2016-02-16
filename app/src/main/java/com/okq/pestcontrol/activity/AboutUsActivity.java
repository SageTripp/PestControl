package com.okq.pestcontrol.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
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
    private WebView aboutUsTv;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebSettings webSettings = aboutUsTv.getSettings();

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webSettings.setMediaPlaybackRequiresUserGesture(true);

//        aboutUsTv.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果
        aboutUsTv.loadUrl("file:///android_asset/html/about_us.html");
    }
}
