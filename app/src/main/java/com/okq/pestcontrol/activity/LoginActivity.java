package com.okq.pestcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;

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

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState, persistentState);
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setSubtitle("登录");
        setSupportActionBar(mToolbar);
        if (isRemember()) {
            userTv.setText((String) getParam(this, "userName", ""));
            passTv.setText((String) getParam(this, "passWord", ""));
        }
        rememberPassCb.setChecked((Boolean) getParam(this, "isRemember", false));
    }

    private boolean isRemember() {
        return "".equals(getParam(this, "userName", "")) && "".equals(getParam(this, "passWord", ""));
    }

    @Event(value = R.id.login_btn)
    private void login(View view) {
        if (checked()) {
            setParam(this, "isRemember", rememberPassCb.isChecked());
            if (rememberPassCb.isChecked()) {
                setParam(this, "userName", userTv.getText().toString());
                setParam(this, "passWord", passTv.getText().toString());
            }
            setParam(this, "test", new PestInformation());
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
