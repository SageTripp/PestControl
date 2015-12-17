package com.okq.pestcontrol.dbDao;

import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * device数据库操作类
 * Created by Administrator on 2015/12/17.
 */
public class DeviceDao {
    private static final DbManager manager = x.getDb(App.getDaoConfig());

    /**
     * 保存一个设备到数据库
     *
     * @param device 要保存的设备
     * @throws DbException
     */
    public static void save(Device device) throws DbException {
        manager.saveOrUpdate(device);
    }

    /**
     * 从数据库获取所有的设备
     *
     * @return 数据库中所有的设备
     * @throws DbException
     */
    public static List<Device> findAll() throws DbException {
        return manager.findAll(Device.class);
    }
}
