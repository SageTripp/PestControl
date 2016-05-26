package com.okq.pestcontrol.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.activity.DeviceDetailsActivity;
import com.okq.pestcontrol.adapter.DeviceAdapter;
import com.okq.pestcontrol.adapter.listener.OnItemClickListener;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.dbDao.DeviceDao;
import com.okq.pestcontrol.task.DeviceParamGetTask;
import com.okq.pestcontrol.task.DeviceTask;
import com.okq.pestcontrol.task.TaskInfo;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 设备页面
 * Created by Administrator on 2015/12/3.
 */
@ContentView(value = R.layout.fragment_device)
public class DeviceFragment extends BaseFragment {

    @ViewInject(value = R.id.device_device_recy)
    private RecyclerView deviceRecy;
    private List<Device> devices = new ArrayList<>();
    private DeviceAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            devices = DeviceDao.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        adapter = new DeviceAdapter(getContext(), devices);
        adapter.setOnItemClickListener(new DeviceAdapter.OnDeviceClick() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onItemClick(int position, Device device) {
                if (device.getStatus() == 1) {
                    Intent intent = new Intent(getContext(), DeviceDetailsActivity.class);
                    intent.putExtra("device", device);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "离线设备不能修改参数", Toast.LENGTH_LONG).show();
                }
            }
        });
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        deviceRecy.setAdapter(adapter);
        deviceRecy.setLayoutManager(manager);

        if (null != devices && devices.size() > 0) {
            new GetParams().start();
        } else {
            Toast.makeText(getContext(), "您暂时没有设备", Toast.LENGTH_LONG).show();
        }

    }

    private class GetParams extends Thread {

        ProgressDialog pd = new ProgressDialog(getContext());
        int n = -1;

        @Override
        public void run() {
            DeviceTask deviceTask = new DeviceTask(getContext(), DeviceTask.SCOPE_ONLINE);
            deviceTask.setTaskInfo(new TaskInfo<List<Device>>() {
                @Override
                public void onPreTask() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.setMessage("正在加载设备参数,请稍后...");
                            pd.setCancelable(false);
                            pd.show();
                        }
                    });
                }

                @Override
                public void onTaskFinish(String b, List<Device> result) {
                    if (b.equals("success") && null != result && result.size() > 0) {
                        n = result.size();
                        for (final Device dev : result) {
                            System.out.println("dev = " + dev.getDeviceNum());
                            DeviceParamGetTask task = new DeviceParamGetTask(getContext(), dev.getDeviceNum());
                            task.setTaskInfo(new TaskInfo<Device>() {
                                @Override
                                public void onPreTask() {

                                }

                                @Override
                                public void onTaskFinish(String b, Device result) {
                                    n--;
                                    if (null != result) {
                                        result.setStatus(1);
                                        adapter.notify(result);
                                    }
                                    if (n == 0)
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (null != pd && pd.isShowing())
                                                    pd.dismiss();
                                            }
                                        });
                                }
                            });
                            task.execute();
                        }
                    } else {
                        Toast.makeText(getContext(), "当前没有设备在线", Toast.LENGTH_LONG).show();
                    }

                }
            });
            deviceTask.execute();
            super.run();
        }
    }

}
