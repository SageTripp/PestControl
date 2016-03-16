package com.okq.pestcontrol.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 虫害信息
 * Created by Administrator on 2015/12/7.
 */
@Table(name = "PestInformation")
public class PestInformation implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private long id;
    /**
     * 害虫名称
     */
    @Column(name = "pest")
    private String pest;
    /**
     * 设备
     */
    @Column(name = "deviceid")
    private String deviceid;
    /**
     * 害虫数量
     */
    @Column(name = "value")
    private int value;
    /**
     * 发送时间
     */
    @Column(name = "time")
    private String time;
    /**
     * 环境
     */
    @Column(name = "environment")
    private String environments;

    /**
     * 获取设备
     *
     * @return 设备
     */
    public String getDeviceid() {
        return deviceid;
    }

    /**
     * 设置设备
     *
     * @param deviceid 设备
     */
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    /**
     * 获取害虫数量
     *
     * @return 害虫数量
     */
    public int getValue() {
        return value;
    }

    /**
     * 设置害虫数量
     *
     * @param value 害虫数量
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 获取发送时间
     *
     * @return 发送时间
     */
    public String getTime() {
        return time;
    }

    /**
     * 设置发送时间
     *
     * @param time 发送时间
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 获取害虫名称
     *
     * @return 害虫名称
     */
    public String getPest() {
        return pest;
    }

    /**
     * 设置害虫名称
     *
     * @param pest 害虫名称
     */
    public void setPest(String pest) {
        this.pest = pest;
    }

    /**
     * 获取环境
     *
     * @return 所有环境
     */
    public String getEnvironments() {
        return environments;
    }

    /**
     * 设置环境
     *
     * @param environments 环境
     */
    public void setEnvironments(String environments) {
        this.environments = environments;
    }
}
