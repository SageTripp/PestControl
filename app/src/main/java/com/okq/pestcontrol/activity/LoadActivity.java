package com.okq.pestcontrol.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.task.DeviceTask;
import com.okq.pestcontrol.task.LoginTask;
import com.okq.pestcontrol.task.TaskInfo;
import com.okq.pestcontrol.util.Sharepreference;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.okq.pestcontrol.util.Sharepreference.Key;
import static com.okq.pestcontrol.util.Sharepreference.getParam;
import static com.okq.pestcontrol.util.Sharepreference.setParam;

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
        animator.setDuration(3000).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent();
                final String name = (String) Sharepreference.getParam(LoadActivity.this, Key.USER_NAME, "");
                final String pass = (String) Sharepreference.getParam(LoadActivity.this, Key.PASSWORD, "");
                if ((boolean) getParam(LoadActivity.this, Key.REMEMBER_PASSWORD, false) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
                    //TODO 在这里加上登录操作,登录完成后进入主界面,登录失败后进入登录页面
                    final ProgressDialog pd = new ProgressDialog(LoadActivity.this);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setCancelable(false);
                    pd.setTitle("登录");
                    pd.setMessage("正在登录,请稍后...");
                    LoginTask task = new LoginTask(LoadActivity.this, name, pass);
                    task.setTaskInfo(new TaskInfo<String>() {
                        @Override
                        public void onPreTask() {
                            pd.show();
                        }

                        @Override
                        public void onTaskFinish(String b, String result) {
                            if ("success".equals(b)) {
                                if (result != null) {
                                    App.saveToken(result);
                                    //登录成功之后加载数据并跳转到主页面
                                    setParam(LoadActivity.this, Key.REMEMBER_PASSWORD, true);
//                            if (rememberPassCb.isChecked()) {
                                    setParam(LoadActivity.this, Key.USER_NAME, name);
                                    setParam(LoadActivity.this, Key.PASSWORD, pass);
//                            } else {
//                                setParam(LoginActivity.this, Key.USER_NAME, "");
//                                setParam(LoginActivity.this, Key.PASSWORD, "");
//                            }
                                    DeviceTask task = new DeviceTask(LoadActivity.this, 1);
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
                                                            x.getDb(App.getDaoConfig()).save(device);
                                                        } catch (DbException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                            } else {
                                                Toast.makeText(LoadActivity.this, "获取设备列表失败!", Toast.LENGTH_LONG).show();
                                            }
                                            startActivity(new Intent(LoadActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            } else if ("fail".equals(b)) {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (!TextUtils.isEmpty(result)) {
                                    Toast.makeText(LoadActivity.this, result, Toast.LENGTH_LONG).show();
                                }
                                finish();
                            }
                        }
                    });
                    task.execute();
                } else {//如果没有记住密码则直接进入登录页面
                    intent.setClass(LoadActivity.this, LoginActivity.class);
                    LoadActivity.this.finish();
                    startActivity(intent);
                }

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
