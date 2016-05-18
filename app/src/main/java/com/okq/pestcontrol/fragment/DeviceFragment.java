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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Device> devices = new ArrayList<>();
        try {
            devices = DeviceDao.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (null != devices && devices.size() > 0) {
            new GetParams(devices).start();
        } else {
            Toast.makeText(getContext(), "您暂时没有设备", Toast.LENGTH_LONG).show();
        }

//        StringBuilder devs = new StringBuilder();
//        if (devices.size() > 0) {
//            if (null != devices) {
//                for (Device d : devices) {
//                    devs.append(d.getDeviceNum());
//                    devs.append(",");
//                }
//                if (devs.length() > 0) {
//                    devs.deleteCharAt(devs.lastIndexOf(","));
//                }
//            }
//
//            DeviceParamGetTask task = new DeviceParamGetTask(getContext(), devs.toString());
//            task.setTaskInfo(new TaskInfo<List<Device>>() {
//                ProgressDialog pd = new ProgressDialog(getContext());
//
//                @Override
//                public void onPreTask() {
//                    pd.setMessage("正在加载设备参数,请稍后...");
//                    pd.setCancelable(false);
//                    pd.show();
//                }
//
//                @Override
//                public void onTaskFinish(String b, List<Device> result) {
//                    if (null != pd && pd.isShowing())
//                        pd.dismiss();
//                    if (b.equals("success")) {
//                        if (result != null) {
//                            DeviceAdapter adapter = new DeviceAdapter(getContext(), result);
//                            final List<Device> finalDevices = result;
//                            adapter.setOnItemClickListener(new OnItemClickListener() {
//                                @Override
//                                public void onItemClick(int position) {
//                                    Intent intent = new Intent(getContext(), DeviceDetailsActivity.class);
//                                    intent.putExtra("device", finalDevices != null ? finalDevices.get(position) : null);
//                                    startActivity(intent);
//                                }
//                            });
//                            RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//                            deviceRecy.setAdapter(adapter);
//                            deviceRecy.setLayoutManager(manager);
//                        } else {
//                            Toast.makeText(getContext(), "加载设备参数失败!", Toast.LENGTH_LONG).show();
//                        }
//
//                    } else {
//                        Toast.makeText(getContext(), "加载设备参数失败!", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//            task.execute();
//        } else {
//            Toast.makeText(getContext(), "没有获取到设备!", Toast.LENGTH_LONG).show();
//        }


    }

    private class GetParams extends Thread {

        private List<Device> params;
        private List<Device> devs = new ArrayList<>();
        private CountDownLatch count;
        ProgressDialog pd = new ProgressDialog(getContext());

        public GetParams(List<Device> devs) {
            this.params = devs;
            count = new CountDownLatch(devs.size());
        }

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.setMessage("正在加载设备参数,请稍后...");
                    pd.setCancelable(false);
                    pd.show();
                }
            });
            System.out.println("params.size = " + params.size());
            for (final Device dev : params) {
                System.out.println("dev = " + dev.getDeviceNum());
                DeviceParamGetTask task = new DeviceParamGetTask(getContext(), dev.getDeviceNum());
                task.setTaskInfo(new TaskInfo<Device>() {
                    @Override
                    public void onPreTask() {

                    }

                    @Override
                    public void onTaskFinish(String b, Device result) {
                        count.countDown();
                        System.out.println("GetParams.onTaskFinish");
                        System.out.println("count = " + count.getCount());
                        if (null != result) {
                            System.out.println("b = [" + b + "], result = [" + result.getDeviceNum() + "]");
                            result.setDeviceNum(dev.getDeviceNum());
                            result.setStatus(dev.getStatus());
                            devs.add(result);
                            System.out.println("devs.size==" + devs.size());
                        }
                    }
                });
                task.execute();
            }

            try {
                count.await();
                System.out.println("加载结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("异常");
            } finally {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != pd && pd.isShowing())
                            pd.dismiss();
                    }
                });
                System.out.println("没有获取到设备");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (devs.size() > 0) {
                            System.out.println("获取到设备");
                            DeviceAdapter adapter = new DeviceAdapter(getContext(), devs);
                            adapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(getContext(), DeviceDetailsActivity.class);
                                    intent.putExtra("device", devs != null ? devs.get(position) : null);
                                    startActivity(intent);
                                }
                            });
                            RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            deviceRecy.setAdapter(adapter);
                            deviceRecy.setLayoutManager(manager);
                        } else {
                            Toast.makeText(getContext(), "没有获取到设备!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            super.run();
        }
    }

}
