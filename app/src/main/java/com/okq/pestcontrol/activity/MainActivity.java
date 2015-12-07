package com.okq.pestcontrol.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

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
    private int currentFragmentId;

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
                BaseFragment fragment = createFragment(item.getItemId());
                //更换界面
                setPage(fragment);
                mToolbar.setSubtitle(item.getTitle());
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
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
                    isShow = true;//设置显示toolBar上的菜单
                    fragment = new DataFragment();
                    break;
                case R.id.menu_device:
                    isShow = false;//设置不显示toolBar上的菜单
                    fragment = new DeviceFragment();
                    break;
                default:
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
}
