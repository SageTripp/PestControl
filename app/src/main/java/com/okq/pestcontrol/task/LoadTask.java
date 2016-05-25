package com.okq.pestcontrol.task;

import android.content.Context;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;
import com.okq.pestcontrol.dbDao.DeviceDao;

import org.joda.time.DateTime;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by Administrator on 2015/12/9.
 */
public class LoadTask extends BaseTask<Boolean> {

    public LoadTask(Context context) {
        mContext = context;
    }


    @Override
    protected Boolean myDoInBackground() {
        DbManager dbManager = x.getDb(App.getDaoConfig());
        String[] kinds = mContext.getResources().getStringArray(R.array.pest_kind);
        String[] areas = mContext.getResources().getStringArray(R.array.pest_device);
        try {
            dbManager.dropTable(PestKind.class);
            dbManager.dropTable(PestInformation.class);
            dbManager.dropTable(Device.class);

            //设备信息
            double[] lons = new double[]{113.601556, 113.584308, 113.478524, 113.565151};
            double[] lats = new double[]{34.918237, 34.826311, 34.682055, 34.814058};

                Device device = new Device();
                device.setStatus(1);
                device.setDeviceNum("4369");
//                device.setDeviceModel("Q" + i);
                device.setCjjg(2);
                device.setUpload(3);
                device.setTels("15912345678,15887654321");
                device.setUpvalue("棉铃虫=12,马陆=13");
                device.setWd(34.918237);
                device.setJd(113.601556);
                DeviceDao.save(device);

            //虫害信息/历史数据
            for (int i = 0; i < 300; i++) {
                PestInformation pestInformation = new PestInformation();
                pestInformation.setDeviceid("4369");
                pestInformation.setPest(kinds[(int) (Math.random() * (kinds.length - 1))]);
                String send = DateTime.now().plusDays((int) -(Math.random() * 90)).toString("YYYY/MM/dd HH:mm:ss");
                pestInformation.setCjtime(send);
                pestInformation.setCount((int) (Math.random() * 5 + 3));
                pestInformation.setEnvironments("温度=12.3,湿度=32,露点=1.2,温度=12.3,湿度=32,露点=1.2,温度=12.3,湿度=32,露点=1.2,温度=12.3,湿度=32,露点=1.2,温度=12.3,湿度=32,露点=1.2,温度=12.3,湿度=32,露点=1.2,温度=12.3,湿度=32,露点=1.2,温度=12.3,湿度=32,露点=1.2");
                dbManager.save(pestInformation);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

}
