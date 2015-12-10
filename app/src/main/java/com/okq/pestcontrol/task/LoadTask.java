package com.okq.pestcontrol.task;

import android.content.Context;
import android.util.Log;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;
import com.okq.pestcontrol.dbDao.PestInformationDao;

import org.joda.time.DateTime;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class LoadTask extends BaseTask {

    public LoadTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {
        DbManager dbManager = x.getDb(App.getDaoConfig());
        String[] kinds = mContext.getResources().getStringArray(R.array.pest_kind);
        String[] areas = mContext.getResources().getStringArray(R.array.pest_area);
        try {
            dbManager.dropTable(PestKind.class);
            dbManager.dropTable(PestInformation.class);
            ArrayList<PestKind> kindList = new ArrayList<>();
            for (int i = 0; i < kinds.length; i++) {
                String kind = kinds[i];
                PestKind pestKind = new PestKind();
                pestKind.setKindFlag(i);
                pestKind.setKindName(kind);
                kindList.add(pestKind);
            }
            for (int i = 0; i < 123; i++) {
                PestInformation pestInformation = new PestInformation();
                pestInformation.setArea(areas[i % areas.length]);
                pestInformation.setPestKind(kindList.get(i % (kinds.length - 1)));
                pestInformation.setTemperature(30 + i % 9);
                pestInformation.setHumidity(80 + i % 9);
                long start = new DateTime(2015, i % 12 + 1, i % 30 + 1, i % 24, i % 60).getMillis();
                long end = new DateTime(2015, (i + 5) % 12 + 1, (i + 5) % 30 + 1, (i + 5) % 24, (i + 5) % 60).getMillis();
                long send = new DateTime(2015, (i + 2) % 12 + 1, (i + 4) % 30 + 1, (i + 5) % 24, (i + 2) % 60).getMillis();
                pestInformation.setStartTime(start);
                pestInformation.setEndTime(end);
                pestInformation.setSendTime(send);
                PestInformationDao.save(pestInformation);
            }
            new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        info.onTaskFinish(o);
    }
}
