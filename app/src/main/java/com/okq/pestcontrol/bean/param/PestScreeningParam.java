package com.okq.pestcontrol.bean.param;

import com.okq.pestcontrol.bean.PestKind;

import java.io.Serializable;

/**
 * 害虫筛选参数
 * Created by Administrator on 2015/12/9.
 */
public class PestScreeningParam implements Serializable {
    /**
     * 区域
     */
    private String area;
    /**
     * 种类
     */
    private PestKind kind;
    /**
     * 开始时间
     */
    private long startTime;
    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 获取区域
     *
     * @return 区域
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区域
     *
     * @param area 区域
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取害虫种类
     *
     * @return 害虫种类
     */
    public PestKind getKind() {
        return kind;
    }

    /**
     * 设置害虫种类
     *
     * @param kind 害虫种类
     */
    public void setKind(PestKind kind) {
        this.kind = kind;
    }

    /**
     * 获取开始时间
     *
     * @return 开始时间
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return 结束时间
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
