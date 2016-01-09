package com.okq.pestcontrol.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.fragment.BaseFragment;
import com.okq.pestcontrol.fragment.ChartFragment;
import com.okq.pestcontrol.fragment.DataFragment;
import com.okq.pestcontrol.fragment.DeviceFragment;
import com.okq.pestcontrol.fragment.SettingFragment;
import com.okq.pestcontrol.service.TestSocketService;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.main_drawerLayout)
    private DrawerLayout mDrawerLayout;
    @ViewInject(value = R.id.slide_menu)
    private NavigationView mNavigationView;
    private HashMap<Integer, BaseFragment> fragmentHashMap = new HashMap<>();
    private int currentFragmentId;
    private boolean isExit = false;
    private Toast exitToast;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        exitToast = Toast.makeText(this, R.string.Exit_App, Toast.LENGTH_SHORT);
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.icons));
        setSupportActionBar(mToolbar);
        App.setToolbarHeignt(mToolbar.getMinimumHeight());
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置默认Fragment
        setDefaultPage();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                BaseFragment fragment = createFragment(item.getItemId());
                //更换界面
                setPage(fragment);
                mToolbar.setSubtitle(item.getTitle());
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        final TextView userHeader = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.slide_header_user);
        userHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(MainActivity.this, userHeader, Gravity.NO_GRAVITY, R.attr.popupMenuStyle, R.style.menuPopup);
                menu.inflate(R.menu.user);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_user_contact:
                                Toast.makeText(MainActivity.this, "我的账户信息", Toast.LENGTH_SHORT).show();
                                //TODO 显示我的账户详情
                                break;
                            case R.id.menu_user_logout:
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                                break;
                        }
                        return true;
                    }
                });
                menu.show();

//                MenuBuilder builder = new MenuBuilder(MainActivity.this);
//                builder.add("我的账户").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        return true;
//                    }
//                });
//                builder.add("注销").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        return true;
//                    }
//                });
//                MenuPopupHelper helper = new MenuPopupHelper(MainActivity.this, builder, userHeader,false,R.style.menuPopup,R.style.menuPopup);
//                helper.show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 设置默认的界面
     */
    private void setDefaultPage() {
        mNavigationView.setCheckedItem(R.id.menu_data);
        currentFragmentId = R.id.menu_data;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (transaction.isEmpty()) {
            mToolbar.setSubtitle(R.string.menu_data);
            transaction.add(R.id.frame_content, new DataFragment());
            transaction.commit();
        }
    }

    private BaseFragment createFragment(int id) {
        if (fragmentHashMap.containsKey(id)) {
            return fragmentHashMap.get(id);
        } else {
            BaseFragment fragment;
            boolean isOther = false;
            switch (id) {
                case R.id.menu_data:
                    fragment = new DataFragment();
                    break;
                case R.id.menu_chart:
                    fragment = new ChartFragment();
                    break;
                case R.id.menu_device:
                    fragment = new DeviceFragment();
                    break;
                case R.id.menu_setting:
                    fragment = new SettingFragment();
                    break;
                default:
                    isOther = true;
                    fragment = new BaseFragment();
                    break;
            }
            if (!isOther) {
                currentFragmentId = id;
                fragmentHashMap.put(id, fragment);
            }
            return fragment;
        }
    }

    /**
     * 设置界面
     *
     * @param fragment 界面
     */
    private void setPage(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else {
            if (isExit) {
                exitToast.cancel();
                this.finish();
                return;
            } else {
                exitToast.show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                        exitToast.cancel();
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.okq.pestcontrol.activity/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.okq.pestcontrol.activity/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
