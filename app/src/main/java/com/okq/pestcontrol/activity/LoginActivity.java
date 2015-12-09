package com.okq.pestcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.task.LoadTask;
import com.okq.pestcontrol.task.TaskInfo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.okq.pestcontrol.util.Sharepreference.getParam;
import static com.okq.pestcontrol.util.Sharepreference.setParam;

/**
 * 登录界面
 * Created by Administrator on 2015/12/8.
 */
@ContentView(value = R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    @ViewInject(value = R.id.login_password_til)
    private TextInputLayout passTil;
    @ViewInject(value = R.id.login_user_til)
    private TextInputLayout userTil;
    @ViewInject(value = R.id.login_forget_password)
    private TextView forgetPassTv;
    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.login_user)
    private EditText userTv;
    @ViewInject(value = R.id.login_password)
    private EditText passTv;
    @ViewInject(value = R.id.login_remember_password)
    private CheckBox rememberPassCb;
    @ViewInject(value = R.id.login_btn)
    private Button loginBtn;

    private String savedUser = "";
    private String savedPass = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.icons));
//        mToolbar.setTitle(R.string.app_name);
        mToolbar.setSubtitle("登录");
        setSupportActionBar(mToolbar);
        if (isRemember()) {
            userTv.setText(savedUser);
            passTv.setText(savedPass);
            loginBtn.setFocusable(true);
            loginBtn.setFocusableInTouchMode(true);
            loginBtn.requestFocus();
            loginBtn.requestFocusFromTouch();
            Log.i(TAG, "onCreate: " + savedUser + ":" + savedPass);
        }
        rememberPassCb.setChecked((Boolean) getParam(this, "isRemember", false));
    }

    /**
     * 判断是否记住了密码
     *
     * @return 是否记住了密码
     */
    private boolean isRemember() {
        savedUser = (String) getParam(this, "userName", "");
        savedPass = (String) getParam(this, "passWord", "");
        return !"".equals(savedUser) && !"".equals(savedPass);
    }

    @Event(value = R.id.login_btn)
    private void login(View view) {
        if (checked()) {
            setParam(this, "isRemember", rememberPassCb.isChecked());
            if (rememberPassCb.isChecked()) {
                setParam(this, "userName", userTv.getText().toString());
                setParam(this, "passWord", passTv.getText().toString());
            } else {
                setParam(this, "userName", "");
                setParam(this, "passWord", "");
            }

            LoadTask task = new LoadTask(this);
            task.execute();
            task.setTaskInfo(new TaskInfo() {
                @Override
                public void onTaskFinish(Object result) {

                }
            });

            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }

    /**
     * 进行登录校验
     *
     * @return 校验结果
     */
    private boolean checked() {
        userTil.setErrorEnabled(false);
        passTil.setErrorEnabled(false);
        if (TextUtils.isEmpty(userTv.getText())) {
            userTil.setErrorEnabled(true);
            userTil.setError("请输入用户名");
            return false;
        }
        if (TextUtils.isEmpty(passTv.getText())) {
            passTil.setErrorEnabled(true);
            passTil.setError("请输入密码");
            return false;
        }
        return true;
    }

}
