package com.okq.pestcontrol.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);

        aboutUsTv.setWebChromeClient(new WebChromeClient());
        aboutUsTv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (url.startsWith("tel:")) {
                    //TODO 打电话
                    AlertDialog dialog = new AlertDialog.Builder(AboutUsActivity.this).create();
                    dialog.setTitle("拨打电话");
                    dialog.setMessage("拨打:" + url.replace("tel:", ""));
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                            startActivity(intent);
                        }
                    });
                    dialog.show();
//                    Toast.makeText(AboutUsActivity.this, url, Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        });

//        aboutUsTv.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果
        aboutUsTv.loadUrl("file:///android_asset/html/about_us.html");
    }

    @Override
    public void onBackPressed() {
        if (aboutUsTv.canGoBack())
            aboutUsTv.goBack();
        else
            super.onBackPressed();
    }

}
