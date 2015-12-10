package com.okq.pestcontrol.dbDao;

import android.util.Log;

import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class PestInformationDao {


    private static final DbManager manager = x.getDb(App.getDaoConfig());

    /**
     * 保存虫害信息
     *
     * @param information 虫害信息
     * @throws DbException
     */
    public static void save(PestInformation information) throws DbException {
        PestKind pestKind = information.getPestKind();
        manager.saveOrUpdate(pestKind);
        information.kindLink = pestKind.id;
        manager.saveBindingId(information);
    }

    /**
     * 查询所有虫害信息
     *
     * @return 所欲哦虫害信息
     * @throws DbException
     */
    public static List<PestInformation> findAll() throws DbException {
        List<PestInformation> all = manager.findAll(PestInformation.class);
        PestKind pestKind = all.get(1).getPestKind();
        for (PestInformation information : all) {
            PestKind kind = manager.findById(PestKind.class, information.kindLink);
            information.setPestKind(kind);
        }
        return all;
    }

}
