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
    @Column(name = "name")
    private String name;
    /**
     * 设备
     */
    @Column(name = "device")
    private String device;
    /**
     * 害虫数量
     */
    @Column(name = "pestNum")
    private int pestNum;
    /**
     * 发送时间
     */
    @Column(name = "sendTime")
    private String sendTime;
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
    public String getDevice() {
        return device;
    }

    /**
     * 设置设备
     *
     * @param device 设备
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * 获取害虫数量
     *
     * @return 害虫数量
     */
    public int getPestNum() {
        return pestNum;
    }

    /**
     * 设置害虫数量
     *
     * @param pestNum 害虫数量
     */
    public void setPestNum(int pestNum) {
        this.pestNum = pestNum;
    }

    /**
     * 获取发送时间
     *
     * @return 发送时间
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取害虫名称
     *
     * @return 害虫名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置害虫名称
     *
     * @param name 害虫名称
     */
    public void setName(String name) {
        this.name = name;
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
