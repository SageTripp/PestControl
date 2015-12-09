package com.okq.pestcontrol.task;

import android.content.Context;
import android.util.Log;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;

import org.joda.time.DateTime;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

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
            for (int i = 0; i < kinds.length; i++) {
                String kind = kinds[i];
                PestKind pestKind = new PestKind();
                pestKind.setKindFlag(i);
                pestKind.setKindName(kind);
                dbManager.save(pestKind);
            }
            List<PestKind> kindList = dbManager.selector(PestKind.class).findAll();
            for (int i = 0; i < 123; i++) {
                PestInformation pestInformation = new PestInformation();
                pestInformation.setArea(areas[i % areas.length]);
                pestInformation.kindLink = kindList.get(i % (areas.length - 1)).id;
                pestInformation.setTemperature(30 + i % 9);
                pestInformation.setHumidity(80 + i % 9);
                pestInformation.setStartTime(new DateTime(2015, i % 12, i % 30, i % 24, i % 60).getMillis());
                pestInformation.setEndTime(new DateTime(2015, i + 5 % 12, i + 5 % 30, i + 5 % 24, i + 5 % 60).getMillis());
                pestInformation.setSendTime(new DateTime(2015, i + 2 % 12, i + 4 % 30, i + 5 % 24, i + 2 % 60).getMillis());
                dbManager.save(pestInformation);
            }
            Log.i("test", "doInBackground: " + dbManager.selector(PestInformation.class).findAll().size());
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
