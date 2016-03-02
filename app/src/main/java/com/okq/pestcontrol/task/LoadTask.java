package com.okq.pestcontrol.task;

import android.content.Context;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;
import com.okq.pestcontrol.dbDao.DeviceDao;
import com.okq.pestcontrol.dbDao.PestInformationDao;

import org.joda.time.DateTime;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;

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

            //
            ArrayList<PestKind> kindList = new ArrayList<>();
            for (int i = 0; i < kinds.length; i++) {
                String kind = kinds[i];
                PestKind pestKind = new PestKind();
                pestKind.setKindFlag(i);
                pestKind.setKindName(kind);
                kindList.add(pestKind);
            }

            //虫害信息/历史数据
            for (int i = 0; i < 300; i++) {
                PestInformation pestInformation = new PestInformation();
                pestInformation.setDevice(areas[((int) (Math.random() * areas.length))]);
                pestInformation.setPestKind(kindList.get((int) (Math.random() * (kinds.length - 1))));
                pestInformation.setTemperature((int) (30 + Math.random() * 9));
                pestInformation.setHumidity((int) (80 + Math.random() * 9));
                long start = new DateTime(2015, i % 12 + 1, i % 30 + 1, i % 24, i % 60).getMillis();
//                long send = new DateTime(2015, (int) ((Math.random() * 3 + 10)), (int) (Math.random() * 30 + 1), (i + 5) % 24, (i + 5) % 60).getMillis();
                long end = new DateTime(2015, (i + 2) % 12 + 1, (i + 4) % 30 + 1, (i + 5) % 24, (i + 2) % 60).getMillis();
                long send = DateTime.now().plusDays((int) -(Math.random() * 90)).getMillis();
                pestInformation.setStartTime(start);
                pestInformation.setEndTime(end);
                pestInformation.setSendTime(send);
                pestInformation.setPestNum((int) (Math.random() * 5 + 3));
                PestInformationDao.save(pestInformation);
            }

            //设备信息
            double[] lons = new double[]{113.601556, 113.584308, 113.478524, 113.565151};
            double[] lats = new double[]{34.918237, 34.826311, 34.682055, 34.814058};

            for (int i = 0; i < 5; i++) {
                Device device = new Device();
                device.setStatus(i % 2);
                device.setDeviceNum("0000000" + i);
                device.setDeviceModel("Q" + i);
                device.setCollectInterval(2 * i);
                device.setUploadInterval(3 * i);
                device.setTels("15912345678,15887654321");
                device.setPestThreshold("棉铃虫=12,马陆=13");
                device.setLat(lats[i % 4]);
                device.setLon(lons[i % 4]);
                DeviceDao.save(device);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

}
