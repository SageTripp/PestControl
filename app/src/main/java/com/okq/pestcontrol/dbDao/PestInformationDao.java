package com.okq.pestcontrol.dbDao;

import android.text.TextUtils;

import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;
import com.okq.pestcontrol.bean.param.PestScreeningParam;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
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

    public static List<PestInformation> find(PestScreeningParam param) {
        List<PestInformation> returnList = new ArrayList<>();
        try {
            Selector<PestInformation> selector = manager.selector(PestInformation.class);
            selector.where("sendTime", ">", 0);
            if (!TextUtils.isEmpty(param.getDevice()))
                selector.and("area", "in", param.getDevice().split(","));
            if (!TextUtils.isEmpty(param.getDevice()))
                selector.and("kindLink", "in", param.getDataType().split(","));
//            if (null != param.getDataType() && param.getDataType().size() > 0) {
//                StringBuilder sb = new StringBuilder();
//                for (PestKind kind : param.getDataType()) {
//                    sb.append(kind.id);
//                    sb.append(",");
//                }
//                sb.deleteCharAt(sb.length() - 1);
//                selector.and("kindLink", "in", sb.toString().split(","));
//            }
            if (param.getStartTime() > 0 && param.getEndTime() > 0)
                selector.and("sendTime", "between", new long[]{param.getStartTime(), param.getEndTime()});
            List<PestInformation> all = selector.findAll();
            for (PestInformation information : all) {
                PestKind kind = manager.findById(PestKind.class, information.kindLink);
                information.setPestKind(kind);
                returnList.add(information);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return returnList;
    }

}
