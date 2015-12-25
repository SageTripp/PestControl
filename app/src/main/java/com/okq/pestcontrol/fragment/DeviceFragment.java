package com.okq.pestcontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.activity.DeviceDetailsActivity;
import com.okq.pestcontrol.adapter.DeviceAdapter;
import com.okq.pestcontrol.adapter.listener.OnItemClickListener;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.bean.Test;
import com.okq.pestcontrol.bean.param.TestParam;
import com.okq.pestcontrol.dbDao.DeviceDao;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 设备页面
 * Created by Administrator on 2015/12/3.
 */
@ContentView(value = R.layout.fragment_device)
public class DeviceFragment extends BaseFragment {

    @ViewInject(value = R.id.device_device_recy)
    private RecyclerView deviceRecy;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Device> devices = null;
        try {
            devices = DeviceDao.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        DeviceAdapter adapter = new DeviceAdapter(getContext(), devices);
        final List<Device> finalDevices = devices;
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), DeviceDetailsActivity.class);
                intent.putExtra("device", finalDevices != null ? finalDevices.get(position) : null);
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        deviceRecy.setAdapter(adapter);
        deviceRecy.setLayoutManager(manager);
    }

}
