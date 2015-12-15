package com.okq.pestcontrol.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.okq.pestcontrol.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import static com.okq.pestcontrol.util.Sharepreference.Key;
import static com.okq.pestcontrol.util.Sharepreference.getParam;

/**
 * 加载页面
 * Created by Administrator on 2015/12/15.
 */
@ContentView(value = R.layout.activity_load)
public class LoadActivity extends BaseActivity {

    @ViewInject(value = R.id.load_img)
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.FullScreen);
        super.onCreate(savedInstanceState);
        ObjectAnimator animator = ObjectAnimator.ofFloat(img, "alpha", 0.5f, 1.0f);
        animator.setDuration(1000).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent();
                if ((boolean) getParam(LoadActivity.this, Key.REMEMBER_PASSWORD, false)) {
                    //TODO 在这里加上登录操作,登录完成后进入主界面,登录失败后进入登录页面
                    intent.setClass(LoadActivity.this, MainActivity.class);
                } else {//如果没有记住密码则直接进入登录页面
                    intent.setClass(LoadActivity.this, LoginActivity.class);
                }

                LoadActivity.this.finish();
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
