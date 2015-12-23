package com.okq.pestcontrol.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.okq.pestcontrol.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备详情页面
 * Created by Administrator on 2015/12/23.
 */
@ContentView(value = R.layout.activity_device_details)
public class DeviceDetailsActivity extends BaseActivity {

    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.colla)
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @ViewInject(value = R.id.listview)
    private ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState, persistentState);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbarLayout.setTitle("测试一下好玩不");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.accent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                android.R.layout.simple_list_item_1, new String[] {"title", "info" },
                new int[] {R.id.title, R.id.info });
        listView.setAdapter(adapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "小宗");
        map.put("info", "电台DJ");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "貂蝉");
        map.put("info", "四大美女");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "奶茶");
        map.put("info", "清纯妹妹");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "大黄");
        map.put("info", "是小狗");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "hello");
        map.put("info", "every thing");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "world");
        map.put("info", "hello world");
        list.add(map);

        return list;
    }

}
