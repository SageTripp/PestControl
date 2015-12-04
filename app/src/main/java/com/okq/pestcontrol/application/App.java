package com.okq.pestcontrol.application;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2015/12/3.
 */
public class App extends Application {
    private static DbManager.DaoConfig daoConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }

    public static DbManager.DaoConfig getDaoConfig() {
        if (null != daoConfig)
            return daoConfig;
        else
            return setDaoConfig();
    }

    private static DbManager.DaoConfig setDaoConfig() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName("test")
                .setDbDir(new File("/sdcard/testDb"))
                .setDbVersion(1)
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                    }
                });
        return daoConfig;
    }
}
