package com.okq.pestcontrol.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 害虫种类
 * Created by Administrator on 2015/12/7.
 */
@Table(name = "PestKind")
public class PestKind implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    public long id;
    /**
     * 害虫种类名称
     */
    @Column(name = "kindName")
    private String kindName;
    /**
     * 害虫种类标示
     */
    @Column(name = "kindFlag")
    private int kindFlag;
    /**
     * 关联的虫害信息
     */
    @Column(name = "pestLink", property = "")
    public long pestLink;

    /**
     * 获取害虫种类名称
     *
     * @return 害虫种类名称
     */
    public String getKindName() {
        return kindName;
    }

    /**
     * 设置害虫种类名称
     *
     * @param kindName 害虫种类名称
     */
    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    /**
     * 获取害虫种类标示
     *
     * @return 害虫种类标示
     */
    public int getKindFlag() {
        return kindFlag;
    }

    /**
     * 设置害虫种类标示
     *
     * @param kindFlag 害虫种类标示
     */
    public void setKindFlag(int kindFlag) {
        this.kindFlag = kindFlag;
    }
}
