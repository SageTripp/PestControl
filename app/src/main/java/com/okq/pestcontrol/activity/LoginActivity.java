package com.okq.pestcontrol.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.task.DeviceTask;
import com.okq.pestcontrol.task.LoginTask;
import com.okq.pestcontrol.task.TaskInfo;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.okq.pestcontrol.util.Sharepreference.Key;
import static com.okq.pestcontrol.util.Sharepreference.getParam;
import static com.okq.pestcontrol.util.Sharepreference.setParam;

/**
 * 登录界面 Created by Administrator on 2015/12/8.
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
        rememberPassCb.setChecked((Boolean) getParam(this, Key.REMEMBER_PASSWORD, false));
    }

    /**
     * 判断是否记住了密码
     *
     * @return 是否记住了密码
     */
    private boolean isRemember() {
        savedUser = savedPass = "";
        if ((boolean) getParam(this, Key.REMEMBER_PASSWORD, false)) {
            savedUser = (String) getParam(this, Key.USER_NAME, "");
            savedPass = (String) getParam(this, Key.PASSWORD, "");
        }
        return !"".equals(savedUser) && !"".equals(savedPass);
    }

    @Event(value = R.id.login_btn)
    private void login(View view) {
        if (checked()) {

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.setTitle("登录");
            pd.setMessage("正在登录,请稍后...");
//            pd.show();

            LoginTask loginTask = new LoginTask(this, userTv.getText().toString(), passTv.getText().toString());
            loginTask.setTaskInfo(new TaskInfo<String>() {
                public void onPreTask() {
                    pd.show();
                }

                public void onTaskFinish(String b, String result) {
                    if ("success".equals(b)) {
                        if (result != null) {
                            App.saveToken(result);
                            //登录成功之后加载数据并跳转到主页面
                            setParam(LoginActivity.this, Key.REMEMBER_PASSWORD, rememberPassCb.isChecked());
//                            if (rememberPassCb.isChecked()) {
                            setParam(LoginActivity.this, Key.USER_NAME, userTv.getText().toString());
                            setParam(LoginActivity.this, Key.PASSWORD, passTv.getText().toString());
//                            } else {
//                                setParam(LoginActivity.this, Key.USER_NAME, "");
//                                setParam(LoginActivity.this, Key.PASSWORD, "");
//                            }
                            DeviceTask task = new DeviceTask(LoginActivity.this, DeviceTask.SCOPE_ALL);
                            task.execute();
                            task.setTaskInfo(new TaskInfo<List<Device>>() {
                                @Override
                                public void onPreTask() {
                                }

                                @Override
                                public void onTaskFinish(String b, List<Device> result) {
                                    if (pd.isShowing()) {
                                        pd.dismiss();
                                    }
                                    if (b.equals("success")) {
                                        if (null != result)
                                            for (Device device : result) {
                                                try {
                                                    device.setStatus(0);
                                                    x.getDb(App.getDaoConfig()).saveOrUpdate(device);
                                                } catch (DbException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "获取设备列表失败!", Toast.LENGTH_LONG).show();
                                    }
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                        }
                    } else if ("fail".equals(b)) {
                        if (pd.isShowing())
                            pd.dismiss();
                        if (!TextUtils.isEmpty(result)) {
                            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            loginTask.execute();


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
