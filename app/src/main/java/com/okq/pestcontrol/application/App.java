package com.okq.pestcontrol.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by Administrator on 2015/12/3.
 */
public class App extends Application {
    private static DbManager.DaoConfig daoConfig;
    private static int mToolbarHeignt;
    private static String mToken;


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        SDKInitializer.initialize(getApplicationContext());
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
//                .setDbDir(new File("/data/data/" + APP_PACKAGE + "/database"))  //可以自定义数据库位置
                .setDbVersion(1)
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                    }
                });
        return daoConfig;
    }

    public static void setToolbarHeignt(int toolbarHeignt) {
        mToolbarHeignt = toolbarHeignt;
    }

    public static int getToolbarHeignt() {
        return mToolbarHeignt;
    }

    public static void saveToken(String token) {
        if (token != null) {
            mToken = token;
        }
    }

    public static String getToken() {
        return null == mToken ? "" : mToken;
    }
}
