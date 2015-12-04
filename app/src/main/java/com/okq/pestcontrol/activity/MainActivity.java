package com.okq.pestcontrol.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.PopupWindow;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.fragment.BaseFragment;
import com.okq.pestcontrol.fragment.DataFragment;
import com.okq.pestcontrol.fragment.DeviceFragment;
import com.okq.pestcontrol.widget.ScreeningPopupWindow;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.main_drawerLayout)
    private DrawerLayout mDrawerLayout;
    @ViewInject(value = R.id.slide_menu)
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean isShow = true;
    private HashMap<Integer, BaseFragment> fragmentHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.icons));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.icons));
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置默认Fragment
        setDefaultPage();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                BaseFragment fragment = new BaseFragment();
                if (!fragmentHashMap.containsKey(item.getItemId())) {
                    if (item.getItemId() == R.id.menu_data) {
                        fragment = new DataFragment();//新建一个fragment
                        isShow = true;//设置显示toolBar上的菜单
                    } else if (item.getItemId() == R.id.menu_device) {
                        fragment = new DeviceFragment();
                        isShow = false;//设置不显示toolBar上的菜单
                    }

                }
                //更新toolBar上的菜单
                MainActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
                invalidateOptionsMenu();
                //更换界面
                setPage(fragment);
                mToolbar.setSubtitle(item.getTitle());
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_sort://排序

                break;
            case R.id.main_menu_screening://筛选
                final ScreeningPopupWindow screen = new ScreeningPopupWindow(this);
                screen.setOnScreeningFinishListener(new ScreeningPopupWindow.OnScreeningFinishListener() {
                    @Override
                    public void onFinished(Bundle data) {
                        screen.dismiss();
                    }
                });
//                screen.showAtLocation(MainActivity.this.findViewById(R.id.frame_content), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mToolbar.getHeight());
                screen.showAsDropDown(mToolbar);
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //设置是否显示
        menu.findItem(R.id.main_menu_screening).setVisible(isShow);
        menu.findItem(R.id.main_menu_sort).setVisible(isShow);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 设置默认的界面
     */
    private void setDefaultPage() {
        mNavigationView.setCheckedItem(R.id.menu_data);
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
            return new BaseFragment();
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
}
